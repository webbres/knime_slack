package com.sjwebb.knime.slack.nodes.channels.get;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "GetChannels" Node.
 * Get the channels from the connected Slack workspace. * n * nThe oath token(s) must be specified in the KNIME preferences. 
 *
 * @author Samuel Webb
 */
public class GetChannelsNodeFactory 
        extends NodeFactory<GetChannelsNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public GetChannelsNodeModel createNodeModel() {
        return new GetChannelsNodeModel();
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
    public NodeView<GetChannelsNodeModel> createNodeView(final int viewIndex,
            final GetChannelsNodeModel nodeModel) {
        return new GetChannelsNodeView(nodeModel);
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
        return new GetChannelsNodeDialog();
    }

}

