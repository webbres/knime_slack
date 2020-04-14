package com.sjwebb.knime.slack.nodes.channels.history2;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "ChannelHistory" Node.
 * Get the channel history for the defined channel
 *
 * @author Samuel Webb
 */
public class ChannelHistoryNodeFactory2 
        extends NodeFactory<ChannelHistoryNodeModel2> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ChannelHistoryNodeModel2 createNodeModel() {
        return new ChannelHistoryNodeModel2();
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
    public NodeView<ChannelHistoryNodeModel2> createNodeView(final int viewIndex,
            final ChannelHistoryNodeModel2 nodeModel) {
        return new ChannelHistoryNodeView2(nodeModel);
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
        return new ChannelHistoryNodeDialog2();
    }

}

