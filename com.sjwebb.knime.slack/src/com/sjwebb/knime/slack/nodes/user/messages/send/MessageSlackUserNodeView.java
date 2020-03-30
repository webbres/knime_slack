package com.sjwebb.knime.slack.nodes.user.messages.send;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "MessageSlackUser" Node.
 * Send a direct message to a KNIME user
 *
 * @author Sam Webb
 */
public class MessageSlackUserNodeView extends NodeView<MessageSlackUserNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link MessageSlackUserNodeModel})
     */
    protected MessageSlackUserNodeView(final MessageSlackUserNodeModel nodeModel) {
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

