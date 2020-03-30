package com.sjwebb.knime.slack.nodes.users.get;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * <code>NodeFactory</code> for the "GetUsers" Node.
 * Get the users from a Sack workspace
 *
 * @author Samuel Webb
 */
public class GetUsersNodeFactory 
        extends NodeFactory<GetUsersNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public GetUsersNodeModel createNodeModel() {
        return new GetUsersNodeModel();
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
    public NodeView<GetUsersNodeModel> createNodeView(final int viewIndex,
            final GetUsersNodeModel nodeModel) {
        return new GetUsersNodeView(nodeModel);
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
        return new GetUsersNodeDialog();
    }

}

