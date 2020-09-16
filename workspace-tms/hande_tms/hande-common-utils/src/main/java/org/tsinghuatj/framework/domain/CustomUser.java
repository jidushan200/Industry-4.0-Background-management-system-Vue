package org.tsinghuatj.framework.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUser implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String username;
	private String realname;
	private Long departmentId;
	private Long roleId;
	private String password;
	private boolean accountNonExpired;
	private boolean accountNonLocked;
	private boolean credentialsNonExpired;
	private boolean enabled;
	private Collection<? extends GrantedAuthority> authorities;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
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

	public CustomUser(Long id, String username, String password, boolean enabled) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}

	public CustomUser(Long id, String realname, Long roleId, Long departmentId) {
		super();
		this.id = id;
		this.realname = realname;
		this.roleId = roleId;
		this.departmentId = departmentId;
	}

	public CustomUser(Long id, String username, String password, String realname, Long roleId, Long departmentId, boolean enabled, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, Collection<? extends GrantedAuthority> authorities) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.realname = realname;
		this.enabled = enabled;
		this.departmentId = departmentId;
		this.roleId = roleId;
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
		return username.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// return this.toString().equals(obj.toString());
		return obj instanceof CustomUser ? this.username.equals(((CustomUser) obj).username) : false;
	}

}
