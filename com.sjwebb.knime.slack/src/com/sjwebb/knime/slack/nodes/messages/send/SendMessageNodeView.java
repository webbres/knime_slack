package com.sjwebb.knime.slack.nodes.messages.send;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "SendMessage" Node.
 * Send a slack message to a designated channel
 *
 * @author Samuel Webb
 */
public class SendMessageNodeView extends NodeView<SendMessageNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link SendMessageNodeModel})
     */
    protected SendMessageNodeView(final SendMessageNodeModel nodeModel) {
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

