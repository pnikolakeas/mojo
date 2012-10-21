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
package mojo.web.login;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import mojo.dao.model.user.User;

@Controller
@RequestMapping("/login/basic")
public class BasicLoginController extends AbstractLoginController {

	private static final String USERNAME_PARAM = "username";

	/**
	 * Handles login submission.
	 */
	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	public ModelAndView doSignIn(HttpServletRequest request) {
		String username = request.getParameter(USERNAME_PARAM);
		User user = getLoginService().findUserByNicknameOrEmail(username);
		ModelAndView mav = new ModelAndView("/login/basic/signin");

		if (user != null) {
			// user found; perform sign-in
			signIn(request.getSession(), user);
		}
		else {
			throw new RuntimeException();
		}

		return mav;
	}

	/**
	 * Handles registration submission.
	 */
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ModelAndView doSignUp(HttpServletRequest request) {
		User user = extractUser(request);
		getLoginService().createUser(user);
		signIn(request.getSession(), user);
		return null;
	}
}
