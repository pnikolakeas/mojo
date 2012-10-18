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
package mojo.dao.model.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import mojo.dao.model.AbstractEntity;

@Entity
@Table(name = "mojo_user")
public class User extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	public enum Gender {

		MALE, FEMALE
	}

	private String email;
	private String nickname;
	private String password;

	private String fullname;
	private Gender gender;
	private Date birthday;
	private Country country;
	private Language language;
	private String postcode;
	private String timezone;

	private Date signUpTime;
	private Date signInTime;

	private List<OpenID> openIDs;
	private List<UserGroup> groups;

	@Column(nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(nullable = false)
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Column(nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	@Enumerated(EnumType.STRING)
	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	@Temporal(TemporalType.DATE)
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getSignUpTime() {
		return signUpTime;
	}

	public void setSignUpTime(Date signUpTime) {
		this.signUpTime = signUpTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getSignInTime() {
		return signInTime;
	}

	public void setSignInTime(Date signInTime) {
		this.signInTime = signInTime;
	}

	@OneToMany(mappedBy = "user")
	public List<OpenID> getOpenIDs() {
		if (openIDs == null) {
			openIDs = new ArrayList<OpenID>();
		}

		return openIDs;
	}

	public void setOpenIDs(List<OpenID> openIDs) {
		this.openIDs = openIDs;
	}

	/**
	 * Helper method.
	 */
	public OpenID addOpenID(String address) {
		OpenID openID = new OpenID();
		openID.setAddress(address);
		getOpenIDs().add(openID);
		openID.setUser(this);
		return openID;
	}

	@ManyToMany(mappedBy = "users")
	public List<UserGroup> getGroups() {
		if (groups == null) {
			groups = new ArrayList<UserGroup>();
		}

		return groups;
	}

	public void setGroups(List<UserGroup> groups) {
		this.groups = groups;
	}
}
