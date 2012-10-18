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
package mojo.dao.model;

public class EntityUtils {

	/**
	 * Implements equivalence check on non-null object references.
	 */
	public static boolean equals(AbstractEntity e1, AbstractEntity e2) {
		if (e1 == null || e2 == null) {
			return false;
		}

		if (e1 == e2) {
			return true;
		}

		Class<? extends AbstractEntity> c1 = e1.getClass();
		Class<? extends AbstractEntity> c2 = e2.getClass();

		if (c1 != c2) {
			return false;
		}

		Integer id1 = e1.getId();
		Integer id2 = e2.getId();

		return id1 != null && id2 != null && id1.intValue() == id2.intValue();
	}
}
