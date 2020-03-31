package com.sjwebb.knime.slack.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.methods.request.channels.ChannelsCreateRequest;
import com.github.seratch.jslack.api.methods.request.channels.ChannelsListRequest;
import com.github.seratch.jslack.api.methods.request.chat.ChatDeleteRequest;
import com.github.seratch.jslack.api.methods.request.chat.ChatPostMessageRequest;
import com.github.seratch.jslack.api.methods.request.conversations.ConversationsHistoryRequest;
import com.github.seratch.jslack.api.methods.request.im.ImOpenRequest;
import com.github.seratch.jslack.api.methods.request.users.UsersListRequest;
import com.github.seratch.jslack.api.methods.response.auth.AuthTestResponse;
import com.github.seratch.jslack.api.methods.response.channels.ChannelsCreateResponse;
import com.github.seratch.jslack.api.methods.response.channels.ChannelsHistoryResponse;
import com.github.seratch.jslack.api.methods.response.channels.ChannelsListResponse;
import com.github.seratch.jslack.api.methods.response.chat.ChatDeleteResponse;
import com.github.seratch.jslack.api.methods.response.chat.ChatPostMessageResponse;
import com.github.seratch.jslack.api.methods.response.conversations.ConversationsHistoryResponse;
import com.github.seratch.jslack.api.methods.response.conversations.ConversationsListResponse;
import com.github.seratch.jslack.api.methods.response.conversations.ConversationsOpenResponse;
import com.github.seratch.jslack.api.methods.response.im.ImOpenResponse;
import com.github.seratch.jslack.api.methods.response.users.UsersListResponse;
import com.github.seratch.jslack.api.model.Attachment;
import com.github.seratch.jslack.api.model.Channel;
import com.github.seratch.jslack.api.model.Conversation;
import com.github.seratch.jslack.api.model.ConversationType;
import com.github.seratch.jslack.api.model.Message;
import com.github.seratch.jslack.api.model.User;
import com.github.seratch.jslack.shortcut.Shortcut;
import com.sjwebb.knime.slack.exception.KnimeSlackException;

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
	 * Create a channel
	 * 
	 * @param channelName
	 *            The name of the new channel
	 * @return
	 * @throws IOException
	 * @throws SlackApiException
	 */
	public ChannelsCreateResponse createChanel(String channelName) throws IOException, SlackApiException {

		ChannelsCreateRequest channelCreation = ChannelsCreateRequest.builder().token(token).name(channelName).build();
		ChannelsCreateResponse response = slack.methods().channelsCreate(channelCreation);

		return response;
	}

	/**
	 * Get the list of non archived channels
	 * 
	 * @return
	 * @throws IOException
	 * @throws SlackApiException
	 */
	public List<Channel> getChannelList() throws IOException, KnimeSlackException, SlackApiException {
		return getChannelList(false);
	}

	/**
	 * Get the list of channels
	 * 
	 * @param keepArchived should archived channels be kept
	 * @return
	 * @throws SlackApiException 
	 * @throws IOException 
	 * @throws Exception 
	 * @deprecated - this is the old API and should be replaced with the conversations API
	 */
	public List<Channel> getChannelList(boolean keepArchived) throws KnimeSlackException, IOException, SlackApiException {
		
		ChannelsListResponse channelsResponse = slack.methods()
				.channelsList(ChannelsListRequest.builder().excludeArchived(!keepArchived).token(token).build());

		
		if(channelsResponse.isOk() != true)
		{
			throw new KnimeSlackException(channelsResponse.getError() + " needs: " + channelsResponse.getNeeded());
		}
		
		
		List<Channel> channels = channelsResponse.getChannels();
		
		
		return channels;
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
	 * Get the channel with the given name. 
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 * @throws SlackApiException
	 * @deprecated - this is old API and should be replaced with the conversations API
	 */
	public Optional<Channel> findChannelWithName(String name) throws IOException, KnimeSlackException, SlackApiException
	{
		List<Channel> channels = getChannelList();
		
		Optional<Channel> channel = channels.stream()
		        .filter(c -> c.getName().equals(name)).findFirst();
		
		return channel;
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
	 * @param limit
	 * @return
	 * @throws Exception
	 * @deprecated this is the old API and should be replaced
	 */
	public List<Message> getChannelMessages(Channel channel, int limit) throws Exception
	{
		ChannelsHistoryResponse history = slack.methods().channelsHistory(req -> req
                .token(token)
                .channel(channel.getId())
                .count(limit));
		
		if(!history.isOk())
		{
			throw new Exception(history.getError() + " - needed: " + history.getNeeded());
		}
		
		return history.getMessages();
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
				  slack.methods().conversationsList(req -> req.token(token).types(types).excludeArchived(true));
		
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
	 * @param user
	 * @param message
	 * @return
	 * @throws IOException
	 * @throws SlackApiException
	 */
	public ChatPostMessageResponse directMessage(String user, String message) throws IOException, SlackApiException
	{
//		List<ConversationType> types = new ArrayList<ConversationType>();
//		types.add(ConversationType.IM);
		
		ConversationsOpenResponse response = slack.methods().conversationsOpen(req -> req.token(token).users(Arrays.asList(user)));
		
		if(!response.isOk())
			throw new IOException("Failed to post message: " + response.getError());
		
		
		ChatPostMessageResponse postResponse =
				  slack.methods().chatPostMessage(req -> req.token(token).channel(response.getChannel().getId()).text(message));
			
		if(!postResponse.isOk())
			throw new IOException("Failed to post message: " + postResponse.getError());
				
		return postResponse;
	}
}
