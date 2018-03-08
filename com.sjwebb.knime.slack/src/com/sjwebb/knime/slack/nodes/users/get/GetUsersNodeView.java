package com.sjwebb.knime.slack.nodes.users.get;

import org.knime.core.node.NodeView;

/**
 * <code>NodeView</code> for the "GetUsers" Node.
 * Get the users from a Sack workspace
 *
 * @author Samuel Webb
 */
public class GetUsersNodeView extends NodeView<GetUsersNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link GetUsersNodeModel})
     */
    protected GetUsersNodeView(final GetUsersNodeModel nodeModel) {
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

