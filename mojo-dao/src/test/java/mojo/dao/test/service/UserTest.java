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
package mojo.dao.test.service;

import java.util.Date;

import junit.framework.Test;
import junit.framework.TestSuite;

import mojo.dao.core.DataService;
import mojo.dao.model.user.Country;
import mojo.dao.model.user.Language;
import mojo.dao.model.user.User;
import mojo.dao.model.user.User.Gender;
import mojo.dao.test.BaseTest;

public class UserTest extends BaseTest {

	private DataService<User> userService;
	private DataService<Country> countryService;
	private DataService<Language> languageService;

	public UserTest(String name) {
		super(name);

		userService = getBean("userService");
		countryService = getBean("countryService");
		languageService = getBean("languageService");
	}

	public void testCRUD() {
		log("Creating user");
		User user = userService.insert(createUser());
		assertValidUser(user);

		log("Retrieving user #" + user.getId());
		User loadedUser = userService.findById(user.getId());
		assertEqualUsers(user, loadedUser);

		log("Modifying user #" + user.getId());
		User updatedUser = userService.update(modifyUser(user));
		assertValidUser(updatedUser);

		log("Deleting user #" + user.getId());
		userService.delete(user.getId());
		user = userService.findById(user.getId());
		assertNull("not null user after delete", user);
	}

	private User createUser() {
		User user = new User();
		user.setNickname("Knight Rider");
		user.setFullname("Michael Knight");
		user.setEmail("kitt@flag.gov");
		user.setPassword("KITT");
		user.setGender(Gender.MALE);
		user.setBirthday(createDate(1980, 0, 1));
		user.setSignUpTime(new Date());
		return user;
	}

	private User modifyUser(User user) {
		Country country = new Country();
		country.setCode("SV");
		country.setName("Smurf Village");
		countryService.insert(country);

		Language language = new Language();
		language.setCode("sl");
		language.setName("Smurf Language");
		languageService.insert(language);

		user.setNickname("Rainbow");
		user.setFullname("Papa Smurf");
		user.setCountry(country);
		user.setLanguage(language);
		return user;
	}

	protected static void assertValidUser(User user) {
		assertNotNull("null user", user);
		assertNotNull("null user.id", user.getId());
		assertNotNull("null user.nickname", user.getNickname());
		assertNotNull("null user.signUpTime", user.getSignUpTime());
	}

	protected static void assertEqualUsers(User exp, User act) {
		if (exp == null) {
			assertNull("not null user", act);
		}
		else {
			assertNotNull("null user", act);
			assertEquals("incorrect user.id", exp.getId(), act.getId());
			assertEquals("incorrect user.nickname", exp.getNickname(), act.getNickname());
			assertEquals("incorrect user.fullname", exp.getFullname(), act.getFullname());
			assertEquals("incorrect user.email", exp.getEmail(), act.getEmail());
			assertEquals("incorrect user.gender", exp.getGender(), act.getGender());
			assertEquals("incorrect user.birthday", exp.getBirthday(), act.getBirthday());
			assertEqualCountries(exp.getCountry(), act.getCountry());
			assertEqualLanguages(exp.getLanguage(), act.getLanguage());
			assertEquals("incorrect user.signInTime", exp.getSignInTime(), act.getSignInTime());
			assertEquals("incorrect user.signUpTime", exp.getSignUpTime(), act.getSignUpTime());
		}
	}

	protected static void assertEqualCountries(Country exp, Country act) {
		if (exp == null) {
			assertNull("not null country", act);
		}
		else {
			assertNotNull("null country", act);
			assertEquals("incorrect country.id", exp.getId(), act.getId());
			assertEquals("incorrect country.code", exp.getCode(), act.getCode());
			assertEquals("incorrect country.name", exp.getName(), act.getName());
		}
	}

	protected static void assertEqualLanguages(Language exp, Language act) {
		if (exp == null) {
			assertNull("not null language", act);
		}
		else {
			assertNotNull("null language", act);
			assertEquals("incorrect language.id", exp.getId(), act.getId());
			assertEquals("incorrect language.code", exp.getCode(), act.getCode());
			assertEquals("incorrect language.name", exp.getName(), act.getName());
		}
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new UserTest("testCRUD"));
		return suite;
	}
}
