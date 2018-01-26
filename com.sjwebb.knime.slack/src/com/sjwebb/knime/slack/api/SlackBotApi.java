package com.sjwebb.knime.slack.api;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.methods.request.channels.ChannelsCreateRequest;
import com.github.seratch.jslack.api.methods.request.channels.ChannelsListRequest;
import com.github.seratch.jslack.api.methods.request.chat.ChatDeleteRequest;
import com.github.seratch.jslack.api.methods.request.chat.ChatPostMessageRequest;
import com.github.seratch.jslack.api.methods.response.channels.ChannelsCreateResponse;
import com.github.seratch.jslack.api.methods.response.channels.ChannelsListResponse;
import com.github.seratch.jslack.api.methods.response.chat.ChatDeleteResponse;
import com.github.seratch.jslack.api.methods.response.chat.ChatPostMessageResponse;
import com.github.seratch.jslack.api.model.Attachment;
import com.github.seratch.jslack.api.model.Channel;

public class SlackBotApi {

	private Slack slack;

	private String token;

	public SlackBotApi(String token) {
		this.slack = Slack.getInstance();
		this.token = token;
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
	 * Get the list of channels
	 * 
	 * @return
	 * @throws IOException
	 * @throws SlackApiException
	 */
	public List<Channel> getChannelList() throws IOException, SlackApiException {
		ChannelsListResponse channelsResponse = slack.methods()
				.channelsList(ChannelsListRequest.builder().token(token).build());


		return channelsResponse.getChannels();
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
	 */
	public Optional<Channel> findChannelWithName(String name) throws IOException, SlackApiException
	{
		List<Channel> channels = getChannelList();
		
		Channel channel = channels.stream()
		        .filter(c -> c.getName().equals(name)).findFirst().get();
		
		return Optional.ofNullable(channel);
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


}