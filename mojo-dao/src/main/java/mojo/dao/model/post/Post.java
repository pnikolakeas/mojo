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
package mojo.dao.model.post;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import mojo.dao.model.AuditableEntity;
import mojo.dao.model.Hierarchical;

@Entity
@Table(name = "mojo_post")
public class Post extends AuditableEntity implements Hierarchical {

	private static final long serialVersionUID = 1L;

	private String title;
	private String content;

	private Post parentNode;
	private List<Post> childNodes;
	private boolean terminal;
	private boolean hidden;

	public Post() {
		terminal = true;
	}

	@Column(nullable = false)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Lob
	@Column(length = Short.MAX_VALUE)
	// http://opensource.atlassian.com/projects/hibernate/browse/HHH-2614
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Helper method.
	 */
	@Transient
	public boolean isRoot() {
		return getParentNode() == null;
	}

	/**
	 * The hierarchical parent.
	 */
	@Override
	@ManyToOne(fetch = FetchType.LAZY)
	public Post getParentNode() {
		return parentNode;
	}

	public void setParentNode(Post parentNode) {
		this.parentNode = parentNode;
	}

	/**
	 * Helper method.
	 */
	public boolean hasChildNodes() {
		return childNodes != null && !childNodes.isEmpty();
	}

	@OrderBy("id")
	@OneToMany(mappedBy = "parentNode",
	cascade = { CascadeType.REMOVE })
	public List<Post> getChildNodes() {
		if (childNodes == null) {
			childNodes = new ArrayList<Post>();
		}

		return childNodes;
	}

	public void setChildNodes(List<Post> childNodes) {
		this.childNodes = childNodes;
	}

	/**
	 * Helper method.
	 */
	public Post createChildNode(String title) {
		Post node = new Post();
		getChildNodes().add(node);
		node.setParentNode(this);
		node.setTitle(title);
		return node;
	}

	public boolean isTerminal() {
		return terminal;
	}

	public void setTerminal(boolean terminal) {
		this.terminal = terminal;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
}
