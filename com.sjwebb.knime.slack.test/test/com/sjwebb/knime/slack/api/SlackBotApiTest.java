package com.sjwebb.knime.slack.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.methods.response.chat.ChatDeleteResponse;
import com.github.seratch.jslack.api.methods.response.chat.ChatPostMessageResponse;
import com.github.seratch.jslack.api.methods.response.im.ImOpenResponse;
import com.github.seratch.jslack.api.model.Attachment;
import com.github.seratch.jslack.api.model.Channel;
import com.github.seratch.jslack.api.model.Field;
import com.github.seratch.jslack.api.model.User;

public class SlackBotApiTest {

	private static String TOKEN;
	private static SlackBotApi api;
	
	@BeforeClass
	public static void setupBefore()
	{
		TOKEN = System.getenv("SLACK_USER_OATH_TOKEN");
		// System.out.println("Token: " + TOKEN);
		api = new SlackBotApi(TOKEN);
		
	}
	
//	@Test
//	public void testCreateChanel() {
//		String name = "knime-test-channel";
//		
//		try {
//			ChannelsCreateResponse response = api.createChanel(name);
//			
//			System.out.println(response);
//			
//			assertEquals("Channel creation not ok", response.isOk());
//		} catch (IOException | SlackApiException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			fail(e.getMessage());
//		}
//	}
	
	
	@Test 
	public void testGetChannels()
	{
		
		try {
			List<Channel> channels = api.getChannelList();
			
			System.out.println(channels);
			
			assertEquals("Incorrect number of channels", 2, channels.size()); 
			
		} catch (IOException | SlackApiException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}
	
	@Test 
	public void testFindChannelWithName()
	{
		String name = "general";
		try {
			Optional<Channel> channel = api.findChannelWithName(name);
			
			assertTrue("Channel missing", channel.isPresent());
			assertEquals(name, channel.get().getName());
			
			
		} catch (IOException | SlackApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test 
	public void testPostMessage()
	{
		String name = "random";
		try {
			Optional<Channel> channel = api.findChannelWithName(name);
			
			assertTrue("Channel missing", channel.isPresent());
			assertEquals(name, channel.get().getName());
			
			
			ChatPostMessageResponse response = api.postMessage(channel.get(), "Hello there");
			
			assertTrue(response.isOk());
			
		} catch (IOException | SlackApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test 
	public void testPostMessageAttachment()
	{
		String name = "random";
		try {
			
			List<Field> fields = new ArrayList<Field>();
			fields.add(Field.builder().title("Col1").value("value1").valueShortEnough(true).build());
			fields.add(Field.builder().title("Col2").value("value2").valueShortEnough(true).build());
			
			Attachment attachment = Attachment.builder().fields(fields).build();
			List<Attachment> attachments = Arrays.asList(attachment);
			
			Optional<Channel> channel = api.findChannelWithName(name);
			
			assertTrue("Channel missing", channel.isPresent());
			assertEquals(name, channel.get().getName());
			
			
			ChatPostMessageResponse response = api.postMessage(channel.get(), "Hello there", attachments);
			
			assertTrue(response.isOk());
			
		} catch (IOException | SlackApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDeleteMessage()
	{
		String name = "random";
		
		try {
			Optional<Channel> channel = api.findChannelWithName(name);
			ChatPostMessageResponse response = api.postMessage(channel.get(), "Hello there");
			
			String ts = response.getTs();
			
			ChatDeleteResponse delResponse = api.deleteMessage(channel.get(), ts);
			
			assertTrue(delResponse.isOk());
			
		} catch (IOException | SlackApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	
	
	@Test 
	public void testGetUsers()
	{
		String name = "Sam";
		
		List<User> response = null;
		try {			
			
			response = api.getUsersListResponse();
			
			assertEquals("wrong number of users", 5, response.size());
			
		} catch (IOException | SlackApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
