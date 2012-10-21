/*
 * Copyright (C) 2010 Dimitrios Menounos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package mojo.dao.service.node;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import mojo.dao.core.DataException;
import mojo.dao.core.DataService;
import mojo.dao.model.node.Node;

public class NodeService extends DataService<Node> {

	@Autowired
	@Qualifier("nodePermissionResolver")
	private NodePermissionResolver permissionRsolver;

	@Override
	protected void beforeInsert(Node node) {
		checkWrite(node, null);
	}

	@Override
	protected void beforeUpdate(Node node) {
		checkWrite(node, findById(node.getId()));
	}

	@Override
	protected void beforeDelete(Object id) {
		checkWrite(null, findById(id));
	}

	protected void checkWrite(Node clientNode, Node serverNode) {
		Node serverParent;

		//
		// Update or Delete.
		//

		if (serverNode != null) {
			serverParent = serverNode.getParentNode();

			// check parent node permission
			if (serverParent != null && !permissionRsolver.hasWriteAccess(serverParent)) {
				StringBuilder sb = new StringBuilder("Parent node permission violation;");
				sb.append(" #" + serverParent.getId() + " " + serverParent.getName());
				throw new DataException(sb.toString());
			}

			// check node permission
			if (!permissionRsolver.hasWriteAccess(serverNode)) {
				StringBuilder sb = new StringBuilder("Node permission violation;");
				sb.append(" #" + serverNode.getId() + " " + serverNode.getName());
				throw new DataException(sb.toString());
			}
		}

		//
		// Create or Update.
		//

		if (clientNode != null) {
			boolean isCreate = serverNode == null;
			boolean isUpdate = serverNode != null;

			if (!clientNode.isRoot()) {
				serverParent = super.findById(clientNode.getParentNode().getId());

				if (serverParent == null) {
					throw new DataException("Non existing parent node");
				}

				if (isCreate && clientNode.getId() != null) {
					// either insert with id (from beforeInsert)
					// or update with non-existing id (from beforeUpdate)
					throw new DataException("Illegal create or update; wrong id");
				}

				if (isUpdate && serverNode.isRoot()) {
					throw new DataException("Illegal update; server root / client non-root");
				}

				if (isUpdate && checkHierarchy(serverNode, serverParent)) {
					// serverParent at the same level (or below) of serverNode
					throw new DataException("Illegal update; move node within it's own subtree");
				}

				// check parent node permission
				if (!permissionRsolver.hasWriteAccess(serverParent)) {
					StringBuilder sb = new StringBuilder("Parent node permission violation;");
					sb.append(" #" + serverParent.getId() + " " + serverParent.getName());
					throw new DataException(sb.toString());
				}
			}
		}
	}

	/**
	 * Check whether the "child" belongs to a subtree.<br />
	 * The subtree starts from (and includes) the "parent".
	 */
	protected boolean checkHierarchy(Node parent, Node child) {
		for (Node node = child; node != null; node = node.getParentNode()) {
			if (node.getId().equals(parent.getId())) {
				return true;
			}
		}

		return false;
	}
}
