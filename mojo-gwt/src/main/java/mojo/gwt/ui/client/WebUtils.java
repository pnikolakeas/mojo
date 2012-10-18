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
package mojo.gwt.ui.client;

import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class WebUtils {

	// http://www.regular-expressions.info/email.html
	public static final String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$";

	public static native void onAttachWidget(Widget widget)
	/*-{
		widget.@com.google.gwt.user.client.ui.Widget::onAttach()();
	}-*/;

	public static native void onDetachWidget(Widget widget)
	/*-{
		widget.@com.google.gwt.user.client.ui.Widget::onDetach()();
	}-*/;

	/**
	 * Show loading mask.
	 */
	public static native void showLoadingMask()
	/*-{
		if ($wnd.showLoadingMask) {
			$wnd.showLoadingMask();
		}
	}-*/;

	/**
	 * Hide loading mask.
	 */
	public static native void hideLoadingMask()
	/*-{
		if ($wnd.hideLoadingMask) {
			$wnd.hideLoadingMask();
		}
	}-*/;

	public static void alert(String title, String message) {
		final WebDialog dialog = new WebDialog();
		dialog.addStyleName("alert-box");
		dialog.setAnimationEnabled(true);
		dialog.setClosable(true);
		dialog.setModal(true);
		dialog.setText(title);

		HTML messageLabel = new HTML(message);
		messageLabel.addStyleName("content");

		dialog.setWidget(messageLabel);
		dialog.center();
		dialog.show();
	}

	public static void tip(String message, UIObject target) {
		HTML messageLabel = new HTML(message);
		messageLabel.addStyleName("content");

		PopupPanel tip = new DecoratedPopupPanel(true);
		tip.addStyleName("tip-box");
		tip.setWidget(messageLabel);
		tip.showRelativeTo(target);
	}
}
