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
package mojo.dao.model.node;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import mojo.dao.model.AbstractRule;
import mojo.dao.model.user.UserGroup;

@Entity
@Table(name = "mojo_node_rule")
public class NodeRule extends AbstractRule {

	private static final long serialVersionUID = 1L;

	private int pos;
	private Node node;
	private UserGroup group;

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	@JoinColumn(nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	@JoinColumn(nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	public UserGroup getGroup() {
		return group;
	}

	public void setGroup(UserGroup group) {
		this.group = group;
	}
}
