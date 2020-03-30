package com.sjwebb.knime.slack.nodes.user.messages.send;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "MessageSlackUser" Node.
 * Send a direct message to a KNIME user
 *
 * @author Sam Webb
 */
public class MessageSlackUserNodeFactory 
        extends NodeFactory<MessageSlackUserNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public MessageSlackUserNodeModel createNodeModel() {
        return new MessageSlackUserNodeModel();
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
    public NodeView<MessageSlackUserNodeModel> createNodeView(final int viewIndex,
            final MessageSlackUserNodeModel nodeModel) {
        return new MessageSlackUserNodeView(nodeModel);
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
        return new MessageSlackUserNodeDialog();
    }

}

