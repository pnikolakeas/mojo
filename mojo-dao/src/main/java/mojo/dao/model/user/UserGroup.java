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
package mojo.dao.model.user;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import mojo.dao.model.AbstractEntity;

@Entity
@Table(name = "mojo_user_group")
public class UserGroup extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	private String name;
	private List<User> users;

	@Column(nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany
	@JoinTable(name = "mojo_user_group_assoc", 
	joinColumns = @JoinColumn(name = "group_id"), 
	inverseJoinColumns = @JoinColumn(name = "user_id"))
	public List<User> getUsers() {
		if (users == null) {
			users = new ArrayList<User>();
		}

		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}
