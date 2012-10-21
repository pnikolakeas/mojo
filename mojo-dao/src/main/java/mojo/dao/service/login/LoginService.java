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
package mojo.dao.service.login;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mojo.dao.model.user.Country;
import mojo.dao.model.user.Language;
import mojo.dao.model.user.OpenID;
import mojo.dao.model.user.User;
import mojo.dao.model.user.UserGroup;

@Service
public class LoginService {

	@Autowired
	private LoginRepository loginRepository;

	public void createUser(User user) {
		user.setSignUpTime(new Date());
		loginRepository.createUser(user);

		for (UserGroup group : user.getGroups()) {
			loginRepository.createUserGroup(group);
		}
	}

	public void createUserWithOpenID(User user, String address) {
		createUser(user);

		OpenID openID = user.addOpenID(address);
		loginRepository.createOpenID(openID);
	}

	public User findUserByNicknameOrEmail(String username) {
		return loginRepository.findUserByNicknameOrEmail(username);
	}

	public User findUserByOpenID(String identifier) {
		return loginRepository.findUserByOpenID(identifier);
	}

	public Country findCountryByCode(String code) {
		return loginRepository.findCountryByCode(code);
	}

	public Language findLanguageByCode(String code) {
		return loginRepository.findLanguageByCode(code);
	}
}
