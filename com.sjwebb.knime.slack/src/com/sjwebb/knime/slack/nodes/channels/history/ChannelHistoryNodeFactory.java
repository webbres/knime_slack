package com.sjwebb.knime.slack.nodes.channels.history;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "ChannelHistory" Node.
 * Get the channel history for the defined channel
 *
 * @author Samuel Webb
 */
public class ChannelHistoryNodeFactory 
        extends NodeFactory<ChannelHistoryNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ChannelHistoryNodeModel createNodeModel() {
        return new ChannelHistoryNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<ChannelHistoryNodeModel> createNodeView(final int viewIndex,
            final ChannelHistoryNodeModel nodeModel) {
        return new ChannelHistoryNodeView(nodeModel);
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
        return new ChannelHistoryNodeDialog();
    }

}

