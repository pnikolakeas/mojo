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
package mojo.dao.test.service;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import mojo.dao.core.Repository;
import mojo.dao.core.spec.Delete;
import mojo.dao.core.spec.Insert;
import mojo.dao.model.Permissions;
import mojo.dao.model.node.Node;
import mojo.dao.service.node.NodeService;
import mojo.dao.test.BaseTest;

public class NodeTest extends BaseTest {

	// used to persist the root node
	private Repository<Node> nodeRepository;

	private NodeService nodeService;
	private Node rootNode;

	protected NodeTest(String name) {
		super(name);

		nodeRepository = getBean("nodeRepository");
		nodeService = getBean("nodeService");
	}

	@Override
	protected void setUp() {
		super.setUp();

		rootNode = new Node();
		rootNode.setCode("root");
		rootNode.setPermissions(Permissions.ALL);

		TransactionTemplate template = createTransactionTemplate();
		template.execute(new TransactionCallback<Object>() {

			public Object doInTransaction(TransactionStatus status) {
				nodeRepository.insert(new Insert<Node>(rootNode));
				return null;
			}
		});
	}

	@Override
	protected void tearDown() {
		TransactionTemplate template = createTransactionTemplate();
		template.execute(new TransactionCallback<Object>() {

			public Object doInTransaction(TransactionStatus status) {
				nodeRepository.delete(new Delete<Node>(rootNode.getId()));
				return null;
			}
		});

		super.tearDown();
	}

	public void testRootCRUD() {
		Exception exception = null;
		Node rootNode = null;

		try {
			log("Creating rootNode");
			rootNode = new Node();
			rootNode.setCode("alt-root-node");
			rootNode.setPermissions(Permissions.ALL);
			nodeService.insert(rootNode);
		}
		catch (Exception e) {
			exception = e;
		}
		finally {
			assertNull("root creation should be allowed", exception);
		}

		try {
			log("Modifying rootNode #" + rootNode.getId());
			rootNode.setCode("modified-root-node");
			rootNode = nodeService.update(rootNode);
		}
		catch (Exception e) {
			exception = e;
		}
		finally {
			assertNull("root modification should be allowed", exception);
		}

		try {
			log("Deleting rootNode #" + rootNode.getId());
			nodeService.delete(rootNode.getId());
		}
		catch (Exception e) {
			exception = e;
		}
		finally {
			assertNull("root deletion should be allowed", exception);
		}
	}

	public void testNodeCRUD() {
		log("Creating node");
		Node node = rootNode.createChildNode("child-node");
		node.setPermissions(Permissions.ALL);
		node = nodeService.insert(node);
		assertValidNode(node);

		log("Retrieving node #" + node.getId());
		Node loadedNode = nodeService.findById(node.getId());
		assertEqualNodes(node, loadedNode);

		log("Modifying node #" + node.getId());
		node.setCode("modified-child-node");
		Node updatedNode = nodeService.update(node);
		assertEqualNodes(node, updatedNode);

		log("Deleting node #" + node.getId());
		nodeService.delete(node.getId());
		node = nodeService.findById(node.getId());
		assertNull("not null node after delete", node);
	}

	public void testNodeRules() {
		log("Creating folder with all rules");
		Node folder = rootNode.createChildNode("folder");
		folder.setPermissions(Permissions.ALL);
		folder = nodeService.insert(folder);

		log("Retrieving folder #" + folder.getId());
		Node loadedFolder = nodeService.findById(folder.getId());
		assertEqualNodes(folder, loadedFolder);

		log("Creating file with no rules");
		Node file = folder.createChildNode("file");
		file = nodeService.insert(file);

		Exception exception = null;

		try {
			log("Attempting to modify file #" + file.getId());
			nodeService.update(file);
		}
		catch (Exception e) {
			exception = e;
		}
		finally {
			assertNotNull("file modification shouldn't be allowed", exception);
			exception = null;
		}

		try {
			log("Attempting to modify folder #" + folder.getId());
			nodeService.update(folder);
		}
		catch (Exception e) {
			exception = e;
		}
		finally {
			assertNull("folder modification should be allowed", exception);
			exception = null;
		}

		try {
			log("Attempting to delete file #" + file.getId());
			nodeService.delete(file.getId());
		}
		catch (Exception e) {
			exception = e;
		}
		finally {
			assertNotNull("file deletion shouldn't be allowed", exception);
			exception = null;
		}

		try {
			log("Attempting to delete folder #" + folder.getId());
			nodeService.delete(folder.getId());
		}
		catch (Exception e) {
			exception = e;
		}
		finally {
			assertNull("folder deletion should be allowed", exception);
			exception = null;
		}
	}

	/**
	 * Experimental code ...
	 */
	public void testIntrospection() {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(Node.class);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

			log("Bean: " + Node.class);

			for (PropertyDescriptor descriptor : propertyDescriptors) {
				String read = descriptor.getReadMethod() != null ? "yes" : "no";
				String write = descriptor.getWriteMethod() != null ? "yes" : "no";
				log("Property: " + descriptor.getName() + ", read: " + read + ", write: " + write);
			}
		}
		catch (IntrospectionException ex) {
			log(ex.getMessage());
		}
	}

	protected static void assertValidNode(Node node) {
		assertNotNull("null node", node);
	}

	protected static void assertEqualNodes(Node exp, Node act) {
		if (exp == null) {
			assertNull("not null node", act);
		}
		else {
			assertNotNull("null node", act);
			assertEquals("incorrect node.id", exp.getId(), act.getId());
			assertEquals("incorrect node.code", exp.getCode(), act.getCode());
			assertEquals("incorrect node.permissions", exp.getPermissions(), act.getPermissions());
			assertEqualEntities(exp.getParentNode(), act.getParentNode());
		}
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new NodeTest("testRootCRUD"));
		suite.addTest(new NodeTest("testNodeCRUD"));
		suite.addTest(new NodeTest("testNodeRules"));
		// suite.addTest(new NodeTest("testIntrospection"));
		return suite;
	}
}
