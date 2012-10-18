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
package mojo.dao.test.util;

import java.util.Date;

import mojo.dao.AuditContext;
import mojo.dao.model.user.User;
import mojo.dao.model.user.UserGroup;

public class MockContext implements AuditContext {

	private User user;

	public MockContext() {
		initUser();
	}

	protected void initUser() {
		user = new User();
		user.setFullname("Jesse Blue");
		user.setNickname("Broken Heart");
		user.setEmail("jblue@cavalrycommand.com");
		user.setPassword("J+A=LFE");
		user.setSignUpTime(new Date());

		initGroup("Star Sheriffs");
		initGroup("Outriders");
	}

	protected void initGroup(String name) {
		UserGroup group = new UserGroup();
		group.setName(name);
		group.getUsers().add(user);
		user.getGroups().add(group);
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public String getRemoteUser() {
		return "foo";
	}

	@Override
	public String getRemoteHost() {
		return "bar";
	}

	@Override
	public boolean isUserInRole(String role) {
		return false;
	}
}
