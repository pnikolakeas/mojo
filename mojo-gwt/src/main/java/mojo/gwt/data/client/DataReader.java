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
package mojo.gwt.data.client;

import java.util.List;

import mojo.gwt.data.client.type.ClassType;

/**
 * Converts raw data to list of records.
 */
public abstract class DataReader<T> {

	private ClassType<T> type;

	public DataReader(ClassType<T> type) {
		setType(type);
	}

	public ClassType<T> getType() {
		return type;
	}

	protected void setType(ClassType<T> type) {
		assert type != null;
		this.type = type;
	}

	/**
	 * Implements the data conversion mechanism.
	 */
	public abstract List<T> convert(Object data);
}