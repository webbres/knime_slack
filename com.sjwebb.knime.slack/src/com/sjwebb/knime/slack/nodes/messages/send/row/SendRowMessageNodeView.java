package com.sjwebb.knime.slack.nodes.messages.send.row;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "SendRowMessage" Node.
 * Send a message based on contents selected from a table row.
 *
 * @author Samuel Webb
 */
public class SendRowMessageNodeView extends NodeView<SendRowMessageNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link SendRowMessageNodeModel})
     */
    protected SendRowMessageNodeView(final SendRowMessageNodeModel nodeModel) {
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

