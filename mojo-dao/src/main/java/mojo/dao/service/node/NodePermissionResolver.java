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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import mojo.dao.AuditContext;
import mojo.dao.core.DataPage;
import mojo.dao.core.DataService;
import mojo.dao.core.spec.ByKey;
import mojo.dao.core.spec.Select;
import mojo.dao.model.EntityUtils;
import mojo.dao.model.Permissions;
import mojo.dao.model.node.Node;
import mojo.dao.model.node.NodeRule;
import mojo.dao.model.user.User;
import mojo.dao.model.user.UserGroup;
import mojo.dao.service.PermissionRsolver;

@Component
public class NodePermissionResolver extends PermissionRsolver {

	@Autowired
	@Qualifier("nodeRuleService")
	private DataService<NodeRule> nodeRuleService;

	@Autowired
	@Qualifier("auditContext")
	private AuditContext auditContext;

	@Override
	protected int resolveRulesAccess(Permissions entity) {
		if (entity instanceof Node) {
			Node node = (Node) entity;
			User user = auditContext.getUser();

			if (user != null) {
				// fetch node rules for this node
				ByKey filter = new ByKey().property("node").key(node);
				Select<NodeRule> select = new Select<NodeRule>(filter);
				DataPage<NodeRule> dataPage = nodeRuleService.select(select);
				List<NodeRule> nodeRules = dataPage.getData();

				// get user groups for current user
				List<UserGroup> userGroups = user.getGroups();

				for (NodeRule nodeRule : nodeRules) {
					UserGroup nodeGroup = nodeRule.getGroup();

					for (UserGroup userGroup : userGroups) {
						if (EntityUtils.equals(nodeGroup, userGroup)) {
							return nodeRule.getPermissions();
						}
					}
				}
			}
		}

		return Permissions.NONE;
	}
}
