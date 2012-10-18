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
package mojo.dao.test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mojo.dao.AuditContext;
import mojo.dao.core.Repository;
import mojo.dao.model.AbstractEntity;
import mojo.dao.model.user.User;
import mojo.dao.service.login.LoginService;
import mojo.dao.test.util.SpringUtils;

public abstract class BaseTest extends TestCase {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected PlatformTransactionManager transactionManager;
	protected ApplicationContext appContext;

	protected Repository<Object> repository;

	protected AuditContext auditContext;
	protected LoginService loginService;

	protected BaseTest(String name) {
		super(name);

		appContext = SpringUtils.getApplicationContext();
		transactionManager = getBean("transactionManager");

		repository = getBean("repository");

		auditContext = getBean("auditContext");
		loginService = getBean("loginService");

		initDefaultUser();
	}

	protected void initDefaultUser() {
		User user = auditContext.getUser();

		// ApplicationContext is static and shared among all Test instances.
		// MockContext is singleton and as such the contained user property
		// will also be shared. The first time we make it persistent.

		if (user.getId() == null) {
			loginService.createUser(user);
		}
	}

	@Override
	protected void setUp() {
		logger.debug("--> " + getName());
	}

	@Override
	protected void tearDown() {
		logger.debug("<-- " + getName());
	}

	protected void log(String msg) {
		logger.debug("### " + msg);
	}

	@SuppressWarnings("unchecked")
	protected <T> T getBean(String name) {
		return (T) appContext.getBean(name);
	}

	protected TransactionTemplate createTransactionTemplate() {
		return new TransactionTemplate(transactionManager);
	}

	protected static Date createDate(int year, int month, int day) {
		return createDateTime(year, month, day, 0, 0, 0);
	}

	protected static Date createDateTime(int year, int month, int day, int hour, int minute, int second) {
		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(year, month, day, hour, minute, second);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	protected static void assertEqualEntities(AbstractEntity exp, AbstractEntity act) {
		if (exp == null) {
			assertNull("not null entity", act);
		}
		else {
			assertNotNull("null entity", act);
			assertEquals("incorrect entity.id", exp.getId(), act.getId());
		}
	}
}
