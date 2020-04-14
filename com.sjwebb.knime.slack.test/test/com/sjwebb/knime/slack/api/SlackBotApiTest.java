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
import com.github.seratch.jslack.api.model.Attachment;
import com.github.seratch.jslack.api.model.Channel;
import com.github.seratch.jslack.api.model.Field;
import com.github.seratch.jslack.api.model.Message;
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
	

}
