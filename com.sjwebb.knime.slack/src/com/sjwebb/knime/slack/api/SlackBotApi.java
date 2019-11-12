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
import com.github.seratch.jslack.api.methods.request.im.ImOpenRequest;
import com.github.seratch.jslack.api.methods.request.users.UsersListRequest;
import com.github.seratch.jslack.api.methods.response.auth.AuthTestResponse;
import com.github.seratch.jslack.api.methods.response.channels.ChannelsCreateResponse;
import com.github.seratch.jslack.api.methods.response.channels.ChannelsHistoryResponse;
import com.github.seratch.jslack.api.methods.response.channels.ChannelsListResponse;
import com.github.seratch.jslack.api.methods.response.chat.ChatDeleteResponse;
import com.github.seratch.jslack.api.methods.response.chat.ChatPostMessageResponse;
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

public class SlackBotApi {

	private Slack slack;
	private Shortcut shortcut;

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
	public List<Channel> getChannelList() throws IOException, SlackApiException {
		return getChannelList(false);
	}

	/**
	 * Get the list of channels
	 * 
	 * @param keepArchived should archived channels be kept
	 * @return
	 * @throws IOException
	 * @throws SlackApiException
	 */
	public List<Channel> getChannelList(boolean keepArchived) throws IOException, SlackApiException {
		
		ChannelsListResponse channelsResponse = slack.methods()
				.channelsList(ChannelsListRequest.builder().excludeArchived(!keepArchived).token(token).build());


		List<Channel> channels = channelsResponse.getChannels();
		
//		if(keepArchived)
//		{
//			channels = channelsResponse.getChannels();
//		} else
//		{
//			channels = channelsResponse.getChannels().stream().filter(v -> !v.isArchived()).collect(Collectors.toList());
//		}

		return channels;
	}
	
	public List<String> getChannelNamesViaConversations(boolean keepArchives) throws Exception
	{
		
		List<ConversationType> types = new ArrayList<ConversationType>();
		types.add(ConversationType.PRIVATE_CHANNEL);
		types.add(ConversationType.PUBLIC_CHANNEL);
		
		ConversationsListResponse listResponse = 
				  slack.methods().conversationsList(req -> req.token(token).types(types).excludeArchived(!keepArchives));
		
		if(!listResponse.isOk())
		{
			throw new Exception("API call failed: " + listResponse.getError());
		}
		
		List<Conversation> conversations = listResponse.getChannels();
		
		return conversations.stream().map(c -> c.getName()).collect(Collectors.toList());
	}
	
	public List<Conversation> getConversations(boolean keepArchives, boolean includePrivate) throws Exception
	{
		
		List<ConversationType> types = new ArrayList<ConversationType>();
		types.add(ConversationType.PUBLIC_CHANNEL);
		
		if(includePrivate)
			types.add(ConversationType.PRIVATE_CHANNEL);
		
		ConversationsListResponse listResponse = 
				  slack.methods().conversationsList(req -> req.token(token).types(types).excludeArchived(!keepArchives));
		
		if(!listResponse.isOk())
		{
			throw new Exception("API call failed: " + listResponse.getError());
		}
		
		
		return listResponse.getChannels();
	}
	
	/**
	 * Post a message to the given chanel
	 * 
	 * @param chanel
	 * @param message
	 * @return
	 * @throws IOException
	 * @throws SlackApiException
	 */
	public ChatPostMessageResponse postMessage(Channel chanel, String message, List<Attachment> attatchments) throws IOException, SlackApiException {
				
		ChatPostMessageResponse postResponse = slack.methods().chatPostMessage(

				ChatPostMessageRequest.builder().token(token).channel(chanel.getId()).text(message).attachments(attatchments).build());

		
		return postResponse;
		
	}
	
	/**
	 * Post a message to the given chanel
	 * 
	 * @param chanel
	 * @param message
	 * @return
	 * @throws IOException
	 * @throws SlackApiException
	 */
	public ChatPostMessageResponse postMessage(Channel chanel, String message) throws IOException, SlackApiException {
				
		ChatPostMessageResponse postResponse = slack.methods().chatPostMessage(

				ChatPostMessageRequest.builder().token(token).channel(chanel.getId()).text(message).build());
		
		return postResponse;
		
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
	public Optional<Channel> findChannelWithName(String name) throws IOException, SlackApiException
	{
		List<Channel> channels = getChannelList();
		
		Optional<Channel> channel = channels.stream()
		        .filter(c -> c.getName().equals(name)).findFirst();
		
		return channel;
	}
	
	/**
	 * Delete the specified message based on timestamp from the given channel 
	 * 
	 * @param channel				The channel the message is from
	 * @param timestamp				The timestamp of the message. See {@link ChatPostMessageResponse#getTs()}
	 * @return
	 * @throws IOException
	 * @throws SlackApiException
	 */
	public ChatDeleteResponse deleteMessage(Channel channel, String timestamp) throws IOException, SlackApiException
	{
		ChatDeleteResponse deleteResponse = slack.methods().chatDelete(
				  ChatDeleteRequest.builder()
				    .token(token)
				    .channel(channel.getId())
				    .ts(timestamp)
				    .build());
		
		return deleteResponse;
	}

	
	public ImOpenResponse openIm(String user) throws IOException, SlackApiException {
		
		ImOpenResponse respone = slack.methods().imOpen(ImOpenRequest.builder().token(token).user(user).build());
		
		return respone;
		
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
	
	
//	public List<Message> getChannelMessages(Channel channel, int limit) throws IOException, SlackApiException {
//		
//		Shortcut shortcut = slack.shortcut(ApiToken.of(token));
//		
//		return shortcut.findRecentMessagesByName(ChannelName.of(channel.getName()), limit);
//		
////		return shortcut.findRecentMessagesByName(channel.getName());
////		
////		ConversationsHistoryResponse historyResponse = slack.methods().conversationsHistory(
////                ConversationsHistoryRequest.builder()
////                        .token(token)
////                        .channel(channel.getId())
////                        .limit(limit)
////                        .build());
////		
////		if(historyResponse.isOk() != true)
////			throw new IOException("Failed to get history");
////		
////		return historyResponse.getMessages();
//	}

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
	
	public String sendMessageToChannel(String channel, String message) throws IOException, SlackApiException
	{
		List<ConversationType> types = new ArrayList<ConversationType>();
		types.add(ConversationType.PRIVATE_CHANNEL);
		types.add(ConversationType.PUBLIC_CHANNEL);
		
		ConversationsListResponse listResponse = 
				  slack.methods().conversationsList(req -> req.token(token).types(types).excludeArchived(true));
		
		Conversation conversation = listResponse.getChannels().stream()
				  .filter(c -> c.getName().equals(channel))
				  .findFirst().get();
		
		ChatPostMessageResponse postResponse =
				  slack.methods().chatPostMessage(req -> req.token(token).channel(conversation.getId()).text(message));
		
		
		if(!postResponse.isOk())
			throw new IOException("Failed to post message: " + postResponse.getError());
		
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
