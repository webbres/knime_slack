package com.sjwebb.knime.slack.api;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.sjwebb.knime.slack.exception.KnimeSlackException;
import com.slack.api.Slack;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.channels.ChannelsListRequest;
import com.slack.api.methods.response.auth.AuthTestResponse;
import com.slack.api.methods.response.channels.ChannelsHistoryResponse;
import com.slack.api.methods.response.channels.ChannelsListResponse;
import com.slack.api.model.Channel;
import com.slack.api.model.Message;

public class SlackBotLegacyApi 
{

	private Slack slack;

	private String token;

	public SlackBotLegacyApi(String token) {
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
	
	
	// Legacy
	
	/**
	 * Get the list of non archived channels
	 * 
	 * @return
	 * @throws IOException
	 * @throws SlackApiException
	 * @deprecated - this is old api and should be removed
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
	
	
}
