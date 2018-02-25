
This repository contains a KNIME plugin which provides nodes interacting with Slack.

It uses the [jslack package](https://github.com/seratch/jslack) to interact with Slack. 

# Status

Current capabilities include:
* Get channel list
* Post single message to Slack from KNIME where channel and message are specified in the dialog
* Use values from a table to post multiple messages to slack


# Using the nodes

Create a bot in your Slack workspace see [https://api.slack.com/bot-users](https://api.slack.com/bot-users). Then provide the bots user OAuth token
in the KNIME preferences. See instructions from the [jslack library](https://github.com/seratch/jslack#setting-up-oauth--permissions-for-it).

Each node can override the preference with a new Bot token either allowing multiple bots in one Slack workspace or access to bots in different Slack workspaces.

## Preferences

The value specified in the KNIME / Slack preferences page will be used to auto populate the OAth token setting in each node.

![Preferences](documentation/preferences.png)


## Nodes



# Building and deployment

The nodes can be build manually in eclipse or via buckminster. The nodes are currently available in the KNIME community update site:

http://update.knime.com/community-contributions/trunk

![P2 Site](documentation/p2-site.PNG)


# Running tests

For the tests to execute an environment variable 'SLACK_USER_OATH_TOKEN' must be configured 
with the Bots OAuth token. 