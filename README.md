
This repository contains a KNIME plugin which provides nodes interacting with Slack.

It uses the [jslack package](https://github.com/seratch/jslack) to interact with Slack. 

# Status
These nodes still under development


# Using the nodes

Create a bot in your Slack workspace see [https://api.slack.com/bot-users](https://api.slack.com/bot-users). Then provide the bots user OAuth token
in the KNIME preferences.

Each noe can override the preference with a new Bot token either allowing multiple bots in one workspace or access to bots in different workspaces.


# Building
Currently the project is built with eclipse. A minimum target platform for KNIME 3.5 is provided. 


# Deployment

This plugin is not currently available via an Eclipse P2 site.

# Running tests

For the tests to execute an environment variable 'SLACK_USER_OATH_TOKEN' must be configured 
with the Bots OAuth token. 