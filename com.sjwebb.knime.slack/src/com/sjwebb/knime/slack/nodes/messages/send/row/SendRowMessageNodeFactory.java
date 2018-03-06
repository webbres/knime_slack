package com.sjwebb.knime.slack.nodes.messages.send.row;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "SendRowMessage" Node.
 * Send a message based on contents selected from a table row.
 *
 * @author Samuel Webb
 */
public class SendRowMessageNodeFactory 
        extends NodeFactory<SendRowMessageNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public SendRowMessageNodeModel createNodeModel() {
        return new SendRowMessageNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<SendRowMessageNodeModel> createNodeView(final int viewIndex,
            final SendRowMessageNodeModel nodeModel) {
        return new SendRowMessageNodeView(nodeModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeDialogPane createNodeDialogPane() {
        return new SendRowMessageNodeDialog();
    }

}

