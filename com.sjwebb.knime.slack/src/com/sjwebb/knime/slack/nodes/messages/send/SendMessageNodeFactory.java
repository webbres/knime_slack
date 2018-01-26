package com.sjwebb.knime.slack.nodes.messages.send;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "SendMessage" Node.
 * Send a slack message to a designated channel
 *
 * @author Samuel Webb
 */
public class SendMessageNodeFactory 
        extends NodeFactory<SendMessageNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public SendMessageNodeModel createNodeModel() {
        return new SendMessageNodeModel();
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
    public NodeView<SendMessageNodeModel> createNodeView(final int viewIndex,
            final SendMessageNodeModel nodeModel) {
        return new SendMessageNodeView(nodeModel);
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
        return new SendMessageNodeDialog();
    }

}

