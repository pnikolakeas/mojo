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
package mojo.dao.core;

public class DataException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance without detail message.
	 */
	public DataException() {
	}

	/**
	 * Constructs an instance with a specified detail message.
	 */
	public DataException(String message) {
		super(message);
	}

	/**
	 * Constructs an instance with a specified cause.
	 */
	public DataException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs an instance with a specified detail message and cause.
	 */
	public DataException(String message, Throwable cause) {
		super(message, cause);
	}
}
