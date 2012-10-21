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
package mojo.dao.service;

import mojo.dao.model.Permissions;

public class PermissionRsolver {

	public boolean hasReadAccess(Permissions entity) {
		return hasAccess(entity, Permissions.READ);
	}

	public boolean hasWriteAccess(Permissions entity) {
		return hasAccess(entity, Permissions.WRITE);
	}

	public boolean hasExecuteAccess(Permissions entity) {
		return hasAccess(entity, Permissions.EXECUTE);
	}

	protected boolean hasAccess(Permissions entity, int access) {
		int defaultAccess = resolveDefaultAccess(entity);

		if (access == (defaultAccess & access)) {
			return true;
		}

		int rulesAccess = resolveRulesAccess(entity);

		if (access == (rulesAccess & access)) {
			return true;
		}

		return false;
	}

	/**
	 * Resolve default access for entity.
	 */
	protected int resolveDefaultAccess(Permissions entity) {
		return entity.getPermissions();
	}

	/**
	 * Resolve rule based access for entity.
	 */
	protected int resolveRulesAccess(Permissions entity) {
		return Permissions.NONE;
	}
}
