package com.sjwebb.knime.slack.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sjwebb.knime.slack.exception.KnimeSlackException;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.users.UsersListRequest;
import com.slack.api.methods.response.auth.AuthTestResponse;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.conversations.ConversationsHistoryResponse;
import com.slack.api.methods.response.conversations.ConversationsListResponse;
import com.slack.api.methods.response.conversations.ConversationsOpenResponse;
import com.slack.api.methods.response.users.UsersListResponse;
import com.slack.api.model.Conversation;
import com.slack.api.model.ConversationType;
import com.slack.api.model.User;

/**
 * Slack API calls
 * 
 *  
 * @author Sam Webb
 *
 */
public class SlackBotApi 
{

	private Slack slack;

	private String token;

	public SlackBotApi(String token) {
		this.slack = Slack.getInstance();
		this.token = token;
	}

	/**
	 * Call check auth: https://api.slack.com/methods/auth.test 
	 * @return	The user authenticated as
	 * @throws Exception 
	 */
	public String checkAuth() throws Exception
	{
		AuthTestResponse response = slack.methods().authTest(req -> req.token(token));
		
		if(response.isOk() != true)
		{
			throw new Exception(response.getError() + " needs: " + response.getNeeded());
		}
		
		return response.getUserId() + " : " + response.getUser();
	}
	
	
	/**
	 * Get the channel names (public and private) using the conversations API
	 * 
	 * @param keepArchives	whether archived channels should be kept
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getChannelNamesViaConversations(boolean keepArchives) throws Exception
	{
		
		List<ConversationType> types = new ArrayList<ConversationType>();
		types.add(ConversationType.PRIVATE_CHANNEL);
		types.add(ConversationType.PUBLIC_CHANNEL);
		
		ConversationsListResponse listResponse = 
				  slack.methods().conversationsList(req -> req.token(token).types(types).limit(1000).excludeArchived(!keepArchives));
		
		if(!listResponse.isOk())
		{
			throw new Exception("API call failed: " + listResponse.getError() + (listResponse.getNeeded() != null ? " scope needed: " + listResponse.getNeeded() : ""));
		}
		
		List<Conversation> conversations = listResponse.getChannels();
		
		return conversations.stream().map(c -> c.getName()).collect(Collectors.toList());
	}
	
	/**
	 * Get the channel names using the conversations API
	 * 
	 * @param keepArchives		whether archived channels should be kept
	 * @param includePrivate	whether to include private channels
	 * @return
	 * @throws Exception
	 */
	public List<Conversation> getConversations(boolean keepArchives, boolean includePrivate) throws Exception
	{
		
		List<ConversationType> types = new ArrayList<ConversationType>();
		types.add(ConversationType.PUBLIC_CHANNEL);
		
		if(includePrivate)
			types.add(ConversationType.PRIVATE_CHANNEL);
		
		ConversationsListResponse listResponse = 
				  slack.methods().conversationsList(req -> req.token(token).types(types).limit(1000).excludeArchived(!keepArchives));
		
		if(!listResponse.isOk())
		{
			throw new Exception("API call failed: " + listResponse.getError());
		}
		
		
		return listResponse.getChannels();
	}
	

	
	/**
	 * Get all of the users from the workspace
	 * 
	 * @return
	 * @throws Exception 
	 */
	public List<User> getUsers() throws Exception {
		
		UsersListResponse respone = slack.methods().usersList(UsersListRequest.builder().token(token).build());
		
		if(!respone.isOk())
		{
			throw new Exception(respone.getError() + " - needed: " + respone.getNeeded());
		}
		
		return respone.getMembers();
		
	}
	
	
	
	/**
	 * 
	 * @param channel
	 * @return
	 * @throws KnimeSlackException
	 * @throws Exception
	 */
	public ConversationsHistoryResponse getChannelHistory(String channel) throws KnimeSlackException, Exception
	{
		if(!channelExists(channel))
		{
			throw new KnimeSlackException("Channel " + channel + " was not found");
		}
		
		String channeId = getIdFromChannelName(channel);
		
		ConversationsHistoryResponse historyResponse = slack.methods().conversationsHistory(req -> req.token(token).channel(channeId));
		
		return historyResponse;
	}
	
	/**
	 * Get the ID of the channel with the given name. The ID is needed instead of the name for further API calls
	 * @param channel
	 * @return
	 * @throws Exception
	 */
	private String getIdFromChannelName(String channel) throws Exception 
	{
		List<ConversationType> types = new ArrayList<ConversationType>();
		types.add(ConversationType.PRIVATE_CHANNEL);
		types.add(ConversationType.PUBLIC_CHANNEL);
		
		ConversationsListResponse listResponse = 
				  slack.methods().conversationsList(req -> req.token(token).types(types).limit(1000).excludeArchived(false));
		
		if(!listResponse.isOk())
		{
			throw new Exception("API call failed: " + listResponse.getError() + (listResponse.getNeeded() != null ? " scope needed: " + listResponse.getNeeded() : ""));
		}
		
		List<Conversation> conversations = listResponse.getChannels();
		
		Optional<Conversation> slackChannel =  conversations.stream().filter(c -> c.getName().equals(channel)).findFirst();
		
		if(!slackChannel.isPresent())
		{
			throw new KnimeSlackException("Channel ID could not be found for channel " + channel);
		}
		
		return slackChannel.get().getId();
	}

	/**
	 * Send a message to the named channel returning the timestamp of the message
	 * 
	 * @param channel
	 * @param message
	 * @return
	 * @throws Exception 
	 * @throws KnimeSlackException 
	 */
	public String sendMessageToChannel(String channel, String message) throws KnimeSlackException, Exception
	{
		List<ConversationType> types = new ArrayList<ConversationType>();
		types.add(ConversationType.PRIVATE_CHANNEL);
		types.add(ConversationType.PUBLIC_CHANNEL);
		
		if(!channelExists(channel))
			throw new KnimeSlackException("Slack channel " + channel + " not found");
		
		// First find the conversation to get the ID
		ConversationsListResponse listResponse = 
				  slack.methods().conversationsList(req -> req.token(token).types(types).limit(1000).excludeArchived(true));
		
		Conversation conversation = listResponse.getChannels().stream()
				  .filter(c -> c.getName().equals(channel))
				  .findFirst().get();
		
		ChatPostMessageResponse postResponse =
				  slack.methods().chatPostMessage(req -> req.token(token).channel(conversation.getId()).text(message));
		
		
		if(!postResponse.isOk())
		{
			if(postResponse.getError().equals("missing_scope")) {
				throw new IOException("Failed to post message: " + postResponse.getError() + " " + postResponse.getNeeded());
			} else {
				throw new IOException("Failed to post message: " + postResponse.getError());
			}

		}
			
		
		String messageTs = postResponse.getMessage().getTs();
		
		return messageTs;
	}

	/**
	 * Check if the channel exists using the conversations API
	 * 
	 * @param channel
	 * @return
	 * @throws Exception
	 */
	public boolean channelExists(String channel) throws Exception 
	{
		return getChannelNamesViaConversations(false).contains(channel);
	}	
	
	
	/**
	 * Send a direct message to a user
	 * 
	 * @param user - a comma seperated list of users. Can be user ids of display names.
	 * @param message
	 * @return
	 * @throws Exception 
	 */
	public ChatPostMessageResponse directMessage(String user, String message) throws Exception
	{
		
		String[] users = user.trim().replace(", ", ",").split(",");
		
		List<String> usersIds = replaceWithId(users);
		
		ConversationsOpenResponse response = slack.methods().conversationsOpen(req -> req.token(token).returnIm(true).users(usersIds));
		
		if(!response.isOk()) {
			String error = response.getError() + " - " + (response.getNeeded() != null ? " needed: " + response.getNeeded() : "");
			throw new IOException("Failed to open conversation with user (" +  user + ")" + error);
		}
			
		
		
		ChatPostMessageResponse postResponse =
				  slack.methods().chatPostMessage(req -> req.token(token).channel(response.getChannel().getId()).text(message));
			
		if(!postResponse.isOk()) {
			String error = response.getError() + " - " + postResponse.getMessage() + (postResponse.getNeeded() != null ? " needed: " + postResponse.getNeeded() : "");
			throw new IOException("Failed to send message to user (" +  user + ")" + error);
		}
			
		return postResponse;
	}

	private List<String> replaceWithId(String[] namedUsers) throws Exception 
	{
		List<User> users = getUsers();
		
		List<String> userIds = new ArrayList<String>();
		
		for(String user : namedUsers) {
			if (user.startsWith("@")) 
			{
				List<String> ids = users.stream().filter(u -> u.getProfile().getDisplayName().equals(user.substring(1))).map(u -> u.getId()).collect(Collectors.toList());
				
				if(ids.size() > 1) {
					throw new Exception("Multiple users with the display name " + user + " were found, please run again using the desired users ID instead of name");
				}
				
				if(ids.size() == 0) {
					throw new Exception("No user with the display name  " + user + " was found");
				}
				
				userIds.addAll(ids);
			}
			else
			{
				userIds.add(user);
			}
		}
		
		return userIds;
	}
	
	
	
	
	

}
