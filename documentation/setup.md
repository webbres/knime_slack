This document is a work in progress and is based on the nightly/trunk version of the nodes.

# Creating a slack app

Navigate to [https://api.slack.com/apps](https://api.slack.com/apps) and log into your workspace. You will probably need to log in as a workspace administrator.

* Select Create New App
 * Name the App e.g. KNIME
 * Select the workspace the App should belong to
* Select the Permissions tab under 'Add features and functionality'
 * Under Scopes select Add an OAuth Scope
  *** JSlack suggests Select all permission scopes except for identity.* however you probably only need: bot, channels:history, groups:history, channels:read, groups:read, users:read, users:read:email, chat:write:bot, im:write
* Navigate to Install App
** Make a note of your OAuth Access Token and your Bot User OAuth Access Token
** Run "Reinstall App" from the Install App section

## Configuring the permissions in your Slack workspace

Different actions require different permissions to be assigned to the Slack App you configure for the KNIME nodes to interacting with your workspace through. 

* Channel History: channel:history scope
* Get Users: users:read scope
* Channel Names: multiple scopes see https://api.slack.com/methods/conversations.list
* Get Channels: multiple scopes see https://api.slack.com/methods/conversations.list
* Message Slack channel: TODO
* Message Slack channel (row based): TODO


# Installing the KNIME nodes

The update site for the KNIME nodes is: 

KNIME Community Contributions (trunk) - https://update.knime.com/community-contributions/trunk/

The nodes can be found under the Other category. Install them as you would other KNIME plugins.

Once installed open the KNIME Preferences, navigate to Slack Preferences Page and enter your OAuth Access Token. For information on the OAuth Access token and configuration of permissions see the next section.

# Using the nodes

When switching from version 1 of the nodes to version 2 you will likely need to replace your OAuth Bot Token with the OAuth token. 

Currently it doesn't look like a bot user can post to a private channel, you will need to post ass the App via the OAuth token not as the bot via the bot OAuth token

