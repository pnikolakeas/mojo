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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import mojo.dao.model.user.Country;
import mojo.dao.model.user.Language;
import mojo.dao.model.user.OpenID;
import mojo.dao.model.user.User;
import mojo.dao.model.user.UserGroup;

@Repository
public class LoginRepository {

	@PersistenceContext
	private EntityManager entityManager;

	public void createUser(User user) {
		entityManager.persist(user);
	}

	public void createUserGroup(UserGroup userGroup) {
		entityManager.persist(userGroup);
	}

	public void createOpenID(OpenID openID) {
		entityManager.persist(openID);
	}

	@SuppressWarnings("unchecked")
	public User findUserByNicknameOrEmail(String username) {
		StringBuilder sb = new StringBuilder();
		sb.append("select u ");
		sb.append("from User u ");
		sb.append("left join fetch u.country c ");
		sb.append("left join fetch u.language l ");
		sb.append("where u.nickname = :username or u.email = :username");

		Query query = entityManager.createQuery(sb.toString());
		query.setParameter("username", username);

		List<User> users = query.getResultList();

		if (users.size() > 0) {
			return users.get(0);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public User findUserByOpenID(String identifier) {
		StringBuilder sb = new StringBuilder();
		sb.append("select u ");
		sb.append("from OpenID o ");
		sb.append("join o.user u ");
		sb.append("left join fetch u.country c ");
		sb.append("left join fetch u.language l ");
		sb.append("where o.address = :identifier");

		Query query = entityManager.createQuery(sb.toString());
		query.setParameter("identifier", identifier);

		List<User> users = query.getResultList();

		if (users.size() > 0) {
			return users.get(0);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public Country findCountryByCode(String code) {
		StringBuilder sb = new StringBuilder();
		sb.append("select c ");
		sb.append("from Country c ");
		sb.append("where c.code = :code");

		Query query = entityManager.createQuery(sb.toString());
		query.setParameter("code", code);

		List<Country> countries = query.getResultList();

		if (countries.size() > 0) {
			return countries.get(0);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public Language findLanguageByCode(String code) {
		StringBuilder sb = new StringBuilder();
		sb.append("select l ");
		sb.append("from Language l ");
		sb.append("where l.code = :code");

		Query query = entityManager.createQuery(sb.toString());
		query.setParameter("code", code);

		List<Language> languages = query.getResultList();

		if (languages.size() > 0) {
			return languages.get(0);
		}

		return null;
	}
}
