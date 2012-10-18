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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.shared.HandlerRegistration;

import mojo.gwt.data.client.event.LoadEvent;
import mojo.gwt.data.client.event.LoadHandler;
import mojo.gwt.data.client.util.Observable;

public class DataStore<T> extends Observable {

	private List<T> data;
	private DataReader<T> reader;

	public boolean isLoaded() {
		return data != null;
	}

	public List<T> getData() {
		if (data == null) {
			data = new ArrayList<T>();
		}

		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
		fireEvent(new LoadEvent());
	}

	public DataReader<T> getReader() {
		return reader;
	}

	public void setReader(DataReader<T> reader) {
		this.reader = reader;
	}

	public HandlerRegistration addLoadHandler(LoadHandler handler) {
		return addHandler(LoadEvent.TYPE, handler);
	}

	/**
	 * Fires the data conversion process. Requires a valid reader.
	 * 
	 * @param data the raw data that will be fed to the reader
	 */
	public void readData(Object data) {
		assert getReader() != null;
		setData(getReader().convert(data));
	}

	/**
	 * Fires the data loading process.
	 */
	public void loadData() {
	}
}
