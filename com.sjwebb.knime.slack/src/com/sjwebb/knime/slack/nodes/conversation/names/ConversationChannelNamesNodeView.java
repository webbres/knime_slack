package com.sjwebb.knime.slack.nodes.conversation.names;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "ConversationChannelNames" Node.
 * Use the conversations API to get the channel names
 *
 * @author Samuel Webb
 */
public class ConversationChannelNamesNodeView extends NodeView<ConversationChannelNamesNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link ConversationChannelNamesNodeModel})
     */
    protected ConversationChannelNamesNodeView(final ConversationChannelNamesNodeModel nodeModel) {
        super(nodeModel);
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {
        // TODO: generated method stub
    }

}

