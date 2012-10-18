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
package mojo.web.core;

import org.springframework.beans.factory.FactoryBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mojo.dao.AuditContext;
import mojo.dao.model.user.User;

public class WebContextFactoryBean implements FactoryBean<AuditContext> {

	private static final Logger logger = LoggerFactory.getLogger(WebContextFactoryBean.class);

	@Override
	public AuditContext getObject() throws Exception {
		logger.debug("Providing lazy context object");
		return new LazyContext();
	}

	@Override
	public Class<AuditContext> getObjectType() {
		return AuditContext.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public static class LazyContext implements AuditContext {

		@Override
		public User getUser() {
			return WebContextHolder.getCurrentContext().getUser();
		}

		@Override
		public String getRemoteUser() {
			return WebContextHolder.getCurrentContext().getRemoteUser();
		}

		@Override
		public String getRemoteHost() {
			return WebContextHolder.getCurrentContext().getRemoteHost();
		}

		@Override
		public boolean isUserInRole(String role) {
			return WebContextHolder.getCurrentContext().isUserInRole(role);
		}
	}
}
