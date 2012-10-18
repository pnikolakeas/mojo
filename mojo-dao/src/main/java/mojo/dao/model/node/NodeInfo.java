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

import java.io.Serializable;
import java.util.Locale;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "mojo_node_info")
public class NodeInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Id id;
	private String title;
	private String description;

	/**
	 * @hibernate.composite-id
	 */
	public Id getId() {
		return id;
	}

	protected void setId(Id id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Embeddable
	public static class Id implements Serializable {

		private static final long serialVersionUID = 1L;

		private Integer nodeId;
		private Locale locale;

		/**
		 * @hibernate.key-property
		 */
		public Integer getNodeId() {
			return nodeId;
		}

		public void setNodeId(Integer nodeId) {
			this.nodeId = nodeId;
		}

		/**
		 * @hibernate.key-property
		 */
		public Locale getLocale() {
			return locale;
		}

		public void setLocale(Locale locale) {
			this.locale = locale;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof Id)) {
				return false;
			}

			StringBuilder str = new StringBuilder();
			str.append(nodeId);
			str.append(locale);

			Id other = (Id) obj;

			StringBuilder otherStr = new StringBuilder();
			otherStr.append(other.nodeId);
			otherStr.append(other.locale);

			return str.toString().equals(otherStr.toString());
		}

		@Override
		public int hashCode() {
			StringBuilder str = new StringBuilder();
			str.append(nodeId);
			str.append(locale);

			return str.toString().hashCode();
		}
	}
}
