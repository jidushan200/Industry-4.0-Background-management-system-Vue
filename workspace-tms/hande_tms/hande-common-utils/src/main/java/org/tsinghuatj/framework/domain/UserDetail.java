package org.tsinghuatj.framework.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetail implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	private boolean enabled;
	private Collection<? extends GrantedAuthority> authorities;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return enabled;
	}

	public UserDetail(String username, String password, boolean enabled) {
		super();
		
		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}

	public UserDetail(String username, String password, boolean enabled, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, Collection<? extends GrantedAuthority> authorities) {
		super();	
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.accountNonExpired = accountNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.credentialsNonExpired = credentialsNonExpired;
		this.authorities = authorities;
	}

	@Override
	public String toString() {
		return this.username;
	}

	@Override
	public int hashCode() {
		return this.username.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		//return this.toString().equals(obj.toString());
		return obj instanceof User ? this.username.equals(((User)obj).getUsername()) : false;
	}

}
