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
package mojo.gwt.ui.client.activity;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.place.shared.Place;

public abstract class BaseActivity<T extends Place> extends AbstractActivity {

	private ClientFactory clientFactory;
	private T place;

	protected BaseActivity(ClientFactory clientFactory) {
		this(clientFactory, null);
	}

	protected BaseActivity(ClientFactory clientFactory, T place) {
		setClientFactory(clientFactory);
		setPlace(place);
	}

	public ClientFactory getClientFactory() {
		return clientFactory;
	}

	protected void setClientFactory(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	public T getPlace() {
		return place;
	}

	protected void setPlace(T place) {
		this.place = place;
	}

	public void goTo(Place place) {
		clientFactory.getPlaceController().goTo(place);
	}
}
