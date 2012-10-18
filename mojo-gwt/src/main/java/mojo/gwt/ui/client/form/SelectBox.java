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
package mojo.gwt.ui.client.form;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ListBox;

import mojo.gwt.data.client.DataStore;
import mojo.gwt.data.client.event.LoadEvent;
import mojo.gwt.data.client.event.LoadHandler;
import mojo.gwt.data.client.type.ClassType;
import mojo.gwt.data.client.type.ClassTypeRegistry;

/**
 * ListBox that supports lazy loading.
 */
public class SelectBox extends ListBox {

	private DataStore store;
	private String keyField;
	private String textField;

	public SelectBox() {
		addStyleName("SelectBox");
		addClickHandler(new OnClick());
	}

	/**
	 * The data record source.
	 */
	public DataStore getStore() {
		return store;
	}

	public void setStore(DataStore store) {
		store.addLoadHandler(new OnLoad());
		this.store = store;
	}

	/**
	 * The field that will be submitted.
	 */
	public String getKeyField() {
		return keyField;
	}

	public void setKeyField(String keyField) {
		this.keyField = keyField;
	}

	/**
	 * The field that will be presented.
	 */
	public String getTextField() {
		return textField;
	}

	public void setTextField(String textField) {
		this.textField = textField;
	}

	/**
	 * Sets the currently selected entry by matching the submit value.
	 * 
	 * If no match is found the argument will be appended and become the
	 * selected one.
	 */
	public void setSelectedValue(String value) {
		for (int x = 0; x < getItemCount(); x++) {
			if (getValue(x).equals(value)) {
				setSelectedIndex(x);
				return;
			}
		}

		if (!isStoreLoaded()) {
			addItem(value, value);
			setSelectedIndex(getItemCount() - 1);
		}
	}

	protected boolean isStoreLoaded() {
		return getStore() != null && getStore().isLoaded();
	}

	/**
	 * Initiates the data loading process.
	 */
	protected class OnClick implements ClickHandler {

		@Override
		public void onClick(ClickEvent event) {
			if (!isStoreLoaded()) {
				addItem("Loading...");
				getStore().loadData();
			}
		}
	}

	/**
	 * Finalizes the data loading process.<br />
	 * Populates the list of items with the loaded list of records.
	 */
	protected class OnLoad implements LoadHandler {

		@Override
		public void onLoad(LoadEvent event) {
			// keep current selection
			int idx = getSelectedIndex();
			String val = idx >= 0 ? getValue(idx) : null;

			clear();
			addItem("");

			DataStore store = (DataStore) event.getSource();
			List<?> records = store.getData();

			for (Object record : records) {
				String value = extract(record, keyField);
				String item = extract(record, textField);
				addItem(item, value);
			}

			if (val != null) {
				// restore selection
				setSelectedValue(val);
			}
		}

		/**
		 * Extracts field value from data record.<br />
		 * Ensures that the result will be a not-null string.
		 */
		protected String extract(Object record, String field) {
			ClassType classType = ClassTypeRegistry.get(record.getClass());
			Object value = classType.getProperty(record, field);
			return value != null ? value.toString() : "";
		}
	}
}
