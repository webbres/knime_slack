package com.sjwebb.knime.slack.nodes.conversation.names;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "ConversationChannelNames" Node.
 * Use the conversations API to get the channel names
 *
 * @author Samuel Webb
 */
public class ConversationChannelNamesNodeFactory 
        extends NodeFactory<ConversationChannelNamesNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public ConversationChannelNamesNodeModel createNodeModel() {
        return new ConversationChannelNamesNodeModel();
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
    public NodeView<ConversationChannelNamesNodeModel> createNodeView(final int viewIndex,
            final ConversationChannelNamesNodeModel nodeModel) {
        return new ConversationChannelNamesNodeView(nodeModel);
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
        return new ConversationChannelNamesNodeDialog();
    }

}

