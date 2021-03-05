package com.sjwebb.knime.slack.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.knime.core.node.NodeLogger;

import com.sjwebb.knime.slack.exception.KnimeSlackException;
import com.slack.api.Slack;
import com.slack.api.SlackConfig;
import com.slack.api.app_backend.config.SlackAppConfig;
import com.slack.api.methods.metrics.MetricsDatastore;
import com.slack.api.methods.metrics.impl.MemoryMetricsDatastore;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.request.chat.ChatPostMessageRequest.ChatPostMessageRequestBuilder;
import com.slack.api.methods.request.conversations.ConversationsListRequest;
import com.slack.api.methods.request.conversations.ConversationsListRequest.ConversationsListRequestBuilder;
import com.slack.api.methods.request.users.UsersListRequest;
import com.slack.api.methods.request.users.UsersListRequest.UsersListRequestBuilder;
import com.slack.api.methods.request.users.UsersLookupByEmailRequest;
import com.slack.api.methods.request.users.UsersLookupByEmailRequest.UsersLookupByEmailRequestBuilder;
import com.slack.api.methods.response.auth.AuthTestResponse;
import com.slack.api.methods.response.channels.UsersLookupByEmailResponse;
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
	
	private static final int DEFAULT_LIMIT = 200;
	private static final int MAX_LIMIT = 1000;

	protected SlackBotApi(String token) {
		SlackConfig config = new SlackConfig();
//		config.getMethodsConfig().setMetricsDatastore(SlackBotApiFactory.getStoreForToken(token));
		this.slack = Slack.getInstance(config);
		this.token = token;
	}

//	public void setDataStore(MemoryMetricsDatastore storeForToken) {
//		this.slack.getConfig().getMethodsConfig().setMetricsDatastore(storeForToken);
//	}
	
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
		List<Conversation> conversations = getConversations(keepArchives, true);
		
		return conversations.stream().map(c -> c.getName()).collect(Collectors.toList());
	}
	
	/**
	 * Get the channel names using the conversations API. Processes the cursor to fetch all conversations
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
		
		ConversationsListRequestBuilder builder = ConversationsListRequest.builder().token(token).types(types).limit(DEFAULT_LIMIT).excludeArchived(!keepArchives);
		
		ConversationsListResponse listResponse = slack.methods().conversationsList(builder.build());
		
		if(!listResponse.isOk())
		{
			throw new Exception("API call failed: " + listResponse.getError());
		}
		
		List<Conversation> channels = listResponse.getChannels();
		
		// Now progress the cursor if exists
		String cursor = listResponse.getResponseMetadata().getNextCursor();
		
		while(cursor != null && !cursor.equals("")) {
			builder = builder.cursor(cursor);
			cursor = fetchNextConversation(channels, builder);
		}	
		
		return channels;
	}
	
	/**
	 * Make the next API call, update the conversations list and then return the next cursor
	 * @param conversations	the conversations collected so far
	 * @param builder		the request builder
	 * @return				the next cursor
	 * @throws Exception
	 */
	private String fetchNextConversation(List<Conversation> conversations, ConversationsListRequestBuilder builder) throws Exception {
		
		ConversationsListResponse listResponse = slack.methods().conversationsList(builder.build());
		
		if(!listResponse.isOk())
		{
			throw new Exception("API call failed: " + listResponse.getError());
		}
		
		conversations.addAll(listResponse.getChannels());
		
		return listResponse.getResponseMetadata().getNextCursor();
	}
	

	
	/**
	 * Get all of the users from the workspace
	 * 
	 * @return
	 * @throws Exception 
	 */
	public List<User> getUsers() throws Exception {
				
		UsersListRequestBuilder builder = UsersListRequest.builder().token(token).limit(DEFAULT_LIMIT);
		
		CompletableFuture<UsersListResponse> future = slack.methodsAsync().usersList(builder.build());
		
		UsersListResponse response = future.get();
		
		if(!response.isOk())
		{
			throw new Exception(response.getError() + " - needed: " + response.getNeeded());
		}
		
		List<User> users = response.getMembers();
		
		
		// Now progress the cursor if exists
		String cursor = response.getResponseMetadata().getNextCursor();
		
		while(cursor != null && !cursor.equals("")) {
			builder = builder.cursor(cursor);
			cursor = fetchNextUsers(users, builder);
		}	
		
		return users;
	}
	

	/**
	 * Progress the cursor
	 * @param users	the fetched users
	 * @param builder	the request builder
	 * @return	the next cursor
	 * @throws Exception
	 */
	private String fetchNextUsers(List<User> users, UsersListRequestBuilder builder) throws Exception {
		
		UsersListResponse response = slack.methods().usersList(builder.build());
		
		if(!response.isOk())
		{
			throw new Exception("API call failed: " + response.getError());
		}
		
		users.addAll(response.getMembers());
		
		return response.getResponseMetadata().getNextCursor();
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
		
		ConversationsHistoryResponse historyResponse = slack.methods().conversationsHistory(req -> req.token(token).limit(MAX_LIMIT).channel(channeId));
		
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
		List<Conversation> conversations = getConversations(false, true);
		
		Optional<Conversation> slackChannel =  conversations.stream().filter(c -> c.getName().equals(channel)).findFirst();
		
		if(!slackChannel.isPresent())
		{
			throw new KnimeSlackException("Channel ID could not be found for channel " + channel);
		}
		
		return slackChannel.get().getId();
	}

	
	public ChatPostMessageResponse sendMessageToChannel(String channel, String message, Optional<String> username, Optional<String> iconUrl, Optional<String> iconEmoji) throws KnimeSlackException, Exception
	{
		return sendMessageToChannel(channel, message, username, iconUrl, iconEmoji, true);
	}
	
	/**
	 * Send a message to the named channel returning the timestamp of the message
	 * 
	 * @param channel	the channel to send the message to
	 * @param message	the message to send
	 * @param username - if provided sets the username to display the bot as
	 * @return
	 * @throws Exception 
	 * @throws KnimeSlackException 
	 */
	public ChatPostMessageResponse sendMessageToChannel(String channel, String message, Optional<String> username, Optional<String> iconUrl, Optional<String> iconEmoji, boolean lookupConversation) throws KnimeSlackException, Exception
	{	
		ChatPostMessageRequestBuilder builder = configureChannelMessageBuilder(channel, message, username, iconUrl, iconEmoji, lookupConversation);
		
		ChatPostMessageResponse	postResponse = slack.methods().chatPostMessage(builder.build());

		return postResponse;
	}
	
	public CompletableFuture<ChatPostMessageResponse> sendMessageToChannelAsync(String channel, String message, Optional<String> username, Optional<String> iconUrl, Optional<String> iconEmoji, boolean lookupConversation) throws KnimeSlackException, Exception
	{	
		ChatPostMessageRequestBuilder builder = configureChannelMessageBuilder(channel, message, username, iconUrl, iconEmoji, lookupConversation);
		
		CompletableFuture<ChatPostMessageResponse>	postResponse = slack.methodsAsync().chatPostMessage(builder.build());

		return postResponse;
	}
	

	private ChatPostMessageRequestBuilder configureChannelMessageBuilder(String channel, String message, Optional<String> username, Optional<String> iconUrl, Optional<String> iconEmoji, boolean lookupConversation) throws Exception {
		String channelName;
		
		// If looking up a conversation we get all available conversations and then search the list based on the provided
		// channel name and then use the channels ID to post the message to. Otherwise we use the channel name directly.
		if(lookupConversation) 
		{			
			Conversation conversation = getConversations(false, true).stream()
					  .filter(c -> c.getName().equals(channel))
					  .findFirst().get();
			
			channelName = conversation.getId();
		} else
		{
			channelName = channel;
		}

		
		ChatPostMessageRequestBuilder builder = ChatPostMessageRequest.builder().token(token).channel(channelName).text(message);
		
		if(username.isPresent())
			builder.username(username.get());
		
		if(iconUrl.isPresent())
			builder.iconUrl(iconUrl.get());
		
		if(iconEmoji.isPresent())
			builder.iconEmoji(iconEmoji.get());
		
		return builder;
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
	 * @param user		 	a comma seperated list of users. Can be user ids of display names.
	 * @param message		the text to send
	 * @param username		the username to set for the bot, if unchanged provide Optional.empty()
	 * @param iconUrl		a url to an image to use as the bot icon, if unchanged provide Optional.empty()
	 * @param iconEmoji		a emoji to use for the icon image. If undesired provide Optional.empty(). This will overide an iconUrl if provided.
	 * @return
	 * @throws Exception 
	 */
	public ChatPostMessageResponse directMessage(String user, String message, Optional<String> username, Optional<String> iconUrl, Optional<String> iconEmoji) throws Exception
	{
		String[] users = user.trim().replace(", ", ",").split(",");
		
		List<String> usersIds = replaceUsernameWithId(users);
		
		ChatPostMessageRequestBuilder builder = buildChatMessageToUser(usersIds, message, username, iconUrl, iconEmoji);

		ChatPostMessageResponse	postResponse = slack.methods().chatPostMessage(builder.build());

		return postResponse;
	}
	
	/**
	 * Performs the same call as {@link #directMessage(String, String, Optional, Optional, Optional)} but uses the async method to attempt to respect API Rate Limits
	 * @param user
	 * @param message
	 * @param username
	 * @param iconUrl
	 * @param iconEmoji
	 * @return
	 * @throws Exception
	 * @deprecated Should call {@link #directMessageAsync(List, String, Optional, Optional, Optional)} having already handled the users string
	 */
	public CompletableFuture<ChatPostMessageResponse> directMessageAsync(String user, String message, Optional<String> username, Optional<String> iconUrl, Optional<String> iconEmoji) throws Exception
	{
		String[] users = user.trim().replace(", ", ",").split(",");
		
		List<String> usersIds = replaceUsernameWithId(users);

		return directMessageAsync(usersIds, message, username, iconUrl, iconEmoji);
	}
	
	/**
	 * Performs the same call as {@link #directMessage(String, String, Optional, Optional, Optional)} but uses the async method to attempt to respect API Rate Limits
	 * @param user
	 * @param message
	 * @param username
	 * @param iconUrl
	 * @param iconEmoji
	 * @return
	 * @throws Exception
	 */
	public CompletableFuture<ChatPostMessageResponse> directMessageAsync(List<String> usersIds, String message, Optional<String> username, Optional<String> iconUrl, Optional<String> iconEmoji) throws Exception
	{	
		ChatPostMessageRequestBuilder builder = buildChatMessageToUser(usersIds, message, username, iconUrl, iconEmoji);
		

		CompletableFuture<ChatPostMessageResponse>	postResponse = slack.methodsAsync().chatPostMessage(builder.build());

		return postResponse;
	}
	
	private ChatPostMessageRequestBuilder buildChatMessageToUser(List<String> usersIds, String message, Optional<String> username, Optional<String> iconUrl, Optional<String> iconEmoji) throws Exception
	{
		
		ConversationsOpenResponse response = slack.methods().conversationsOpen(req -> req.token(token).returnIm(true).users(usersIds));
		
		if(!response.isOk()) {
			String error = response.getError() + " - " + (response.getNeeded() != null ? " needed: " + response.getNeeded() : "");
			throw new IOException("Failed to open conversation with user (" +  usersIds.toString() + ")" + error);
		}
			
		ChatPostMessageRequestBuilder builder = ChatPostMessageRequest.builder().token(token).channel(response.getChannel().getId()).text(message);
		
		if(username.isPresent())
			builder.username(username.get());
		
		if(iconUrl.isPresent())
			builder.iconUrl(iconUrl.get());
		
		if(iconEmoji.isPresent())
			builder.iconEmoji(iconEmoji.get());
		
		return builder;
	}
	
	

	/**
	 * Return a new list of usernames having replace display names (@Name1, @Name2) with the unique slack username
	 * @param namedUsers
	 * @return
	 * @throws Exception
	 */
	public List<String> replaceUsernameWithId(String[] namedUsers) throws Exception 
	{
		List<String> userIds = new ArrayList<String>();
		
		if(needsReplacement(namedUsers)) 
		{
			// Get all users
			List<User> users = getUsers();
			
			// For each user identifier provided
			for(String userOriginal : namedUsers) {
				
				// Trip any leading or training white space as we don't validate this in he dialog
				String trimmed = userOriginal.trim();
				
				if (trimmed.startsWith("@")) 
				{
					List<String> ids = users.stream().filter(u -> u.getProfile().getDisplayName().equals(trimmed.substring(1))).map(u -> u.getId()).collect(Collectors.toList());
					
					if(ids.size() > 1) {
						throw new Exception("Multiple users with the display name " + trimmed + " were found, please run again using the desired users ID instead of name");
					}
					
					if(ids.size() == 0) {
						throw new Exception("No user with the display name  " + trimmed + " was found");
					}
					
					userIds.addAll(ids);
				}
				else
				{
					// If it doesn't start with an '@' we assume it's already a unique identifier
					userIds.add(trimmed);
				}
			}	
		} else {
			userIds = Arrays.asList(namedUsers);
		}
		
		
		
		return userIds;
	}
	
	/**
	 * Identify if the any display names need replacing with user ids
	 * @param namedUsers
	 * @return
	 */
	private boolean needsReplacement(String[] namedUsers) 
	{
		boolean foundDisplayName = false;
		
		for(int i = 0; i < namedUsers.length && !foundDisplayName; i++)
		{
			if(namedUsers[i].trim().startsWith("@"))
				foundDisplayName = true;
		}
		
		return foundDisplayName;
	}

	public MetricsDatastore getMetrics() 
	{
		return this.slack.getConfig().getMethodsConfig().getMetricsDatastore();
	}

	/**
	 * Lookup a user by email. Uses an async call and waits for the response.
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public UsersLookupByEmailResponse getUser(String email) throws Exception
	{
		
		UsersLookupByEmailRequestBuilder req = UsersLookupByEmailRequest.builder().token(token).email(email);
		
		CompletableFuture<UsersLookupByEmailResponse> future = slack.methodsAsync().usersLookupByEmail(req.build());
		
		UsersLookupByEmailResponse response = future.get();
		
		return response;
	}
}
