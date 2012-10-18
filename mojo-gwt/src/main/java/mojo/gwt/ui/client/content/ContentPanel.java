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
package mojo.gwt.ui.client.content;

import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import mojo.gwt.ui.client.content.ContentActivity.ContentPlace;

public class ContentPanel extends Widget {

	private static final RegExp uriRegExp = RegExp.compile("^https?://[^/]+/");

	private ContentActivity activity;

	protected static ContentPanel wrap(Element element) {
		// assert that the element is attached
		assert Document.get().getBody().isOrHasChild(element);

		ContentPanel panel = new ContentPanel(element);

		// initialize life cycle
		panel.onAttach();

		// remember it for cleanup
		RootPanel.detachOnWindowClose(panel);

		return panel;
	}

	private ContentPanel(Element element) {
		setElement(element);
		sinkEvents(Event.ONCLICK);
	}

	protected ContentPanel(String html) {
		this(Document.get().createDivElement());
		getElement().setInnerHTML(html);
	}

	protected ContentActivity getActivity() {
		return activity;
	}

	protected void setActivity(ContentActivity activity) {
		this.activity = activity;
	}

	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);

		switch (DOM.eventGetType(event)) {
		case Event.ONCLICK:
			onClick(event);
			break;
		}
	}

	private void onClick(Event event) {
		Element eventEl = Element.as(event.getEventTarget());
		AnchorElement link = getClosestLink(eventEl);

		// resort to default browser behavior
		if (link == null) {
			return;
		}

		String rel = link.getRel();
		String target = link.getTarget();
		String href = link.getHref();

		boolean hasRel = !rel.isEmpty();
		boolean hasTarget = !target.isEmpty();
		boolean hasFragment = href.indexOf("#") != -1;

		// resort to default browser behavior
		if (hasRel || hasTarget || hasFragment) {
			return;
		}

		//
		// check for external link address
		//

		String currentBaseUri = getCurrentBaseUri();

		// resort to default browser behavior
		if (!href.startsWith(currentBaseUri)) {
			return;
		}

		// shorten link address
		href = href.substring(currentBaseUri.length() - 1);

		event.preventDefault();
		changePage(href);
	}

	private AnchorElement getClosestLink(Element el) {
		while (el != null && !el.equals(getElement())) {
			if (el.getNodeName().toLowerCase().equals("a")) {
				return AnchorElement.as(el);
			}

			el = el.getParentElement();
		}

		return null;
	}

	private String getCurrentBaseUri() {
		String currentUri = Window.Location.getHref();
		MatchResult mr = uriRegExp.exec(currentUri);
		currentUri = mr.getGroup(0);

		if (currentUri == null) {
			StringBuilder sb = new StringBuilder();
			sb.append(Window.Location.getProtocol());
			sb.append("//");
			sb.append(Window.Location.getHost());
			sb.append("/");
			currentUri = sb.toString();
		}

		return currentUri;
	}

	private void changePage(String uri) {
		getActivity().goTo(new ContentPlace(uri));
	}
}
