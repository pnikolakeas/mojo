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

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import mojo.gwt.http.client.WebRequestBuilder;
import mojo.gwt.http.client.WebRequestCallback;
import mojo.gwt.ui.client.WebUtils;
import mojo.gwt.ui.client.activity.BaseActivity;
import mojo.gwt.ui.client.activity.ClientFactory;
import mojo.gwt.ui.client.content.ContentActivity.ContentPlace;

public class ContentActivity extends BaseActivity<ContentPlace> {

	public static final String CONTENT_BEGIN = "<!-- content-begin -->";
	public static final String CONTENT_END = "<!-- content-end -->";

	public ContentActivity(ClientFactory clientFactory, ContentPlace place) {
		super(clientFactory, place);
	}

	@Override
	public void start(final AcceptsOneWidget container, EventBus eventBus) {
		if (getPlace().uri == null) {
			getPlace().uri = Window.Location.getHref();

			String elementId = getClientFactory().getMainComponentId();
			Element element = Document.get().getElementById(elementId);

			if (element != null) {
				ContentPanel view = ContentPanel.wrap(element);
				view.setActivity(ContentActivity.this);
				container.setWidget(view);
			}

			return;
		}

		WebUtils.showLoadingMask();

		WebRequestBuilder builder = new WebRequestBuilder(RequestBuilder.GET, getPlace().uri);
		builder.setCallback(new WebRequestCallback() {

			@Override
			public void onResponseReceived(Request request, Response response) {
				if (response.getStatusCode() == Response.SC_OK) {
					String text = response.getText();

					int bgn = text.indexOf(CONTENT_BEGIN);
					int end = text.indexOf(CONTENT_END);

					if (bgn != -1 && end != -1) {
						end += CONTENT_END.length();
						String html = text.substring(bgn, end);

						ContentPanel view = new ContentPanel(html);
						view.setActivity(ContentActivity.this);
						container.setWidget(view);
					}
				}

				WebUtils.hideLoadingMask();
			}
		});

		builder.send();
	}

	/**
	 * Carries activity parameters.
	 */
	public static class ContentPlace extends Place {

		public String uri;

		public ContentPlace() {
			this(null);
		}

		public ContentPlace(String uri) {
			this.uri = uri;
		}
	}

	/**
	 * Converts place to / from uri compatible form.
	 */
	public static class ContentTokenizer implements PlaceTokenizer<ContentPlace> {

		/**
		 * Called *before* the activity is started; e.g. when navigating through
		 * the browser history buttons.
		 * 
		 * PlaceHistoryHandler.handleHistoryToken(String) -> <br>
		 * PlaceHistoryMapper.getPlace(String) -> <br>
		 * PlaceTokenizer.getPlace(String)
		 */
		@Override
		public ContentPlace getPlace(String token) {
			return new ContentPlace(token);
		}

		/**
		 * Called *after* the activity is started (but not the first time).
		 * 
		 * PlaceHistoryHandler.tokenForPlace(Place) -> <br>
		 * PlaceHistoryMapper.getToken(Place) -> <br>
		 * PlaceTokenizer.getToken(Place)
		 */
		@Override
		public String getToken(ContentPlace place) {
			if (place.uri != null) {
				return place.uri;
			}

			return "";
		}
	}
}
