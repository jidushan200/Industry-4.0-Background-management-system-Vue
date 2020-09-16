package org.tsinghuatj.sys.service.impl;

import static java.util.Collections.emptyList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.tsinghuatj.framework.domain.CustomUser;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.StringUtils;
import org.tsinghuatj.sys.domain.UserAccount;
import org.tsinghuatj.sys.service.ILoginService;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

	private @Autowired ILoginService loginService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		boolean accountNonExpired = true;
		boolean accountNonLocked = true;
		boolean credentialsNonExpired = true;

		UserAccount userAccount = null;
		if (StringUtils.isNotBlank(username)) {
			try {
				userAccount = loginService.getUserInfoByName(username);
			} catch (BusinessException e) {				
				e.printStackTrace();
			}
		}
		if (null == userAccount) {
			throw new UsernameNotFoundException("user does not exist");
		}
		
		return new CustomUser(userAccount.getPkId(), username, userAccount.getLoginPwd(), userAccount.getRealName(),userAccount.getRoleId(),userAccount.getDepartmentId(),
				true, accountNonExpired, accountNonLocked, credentialsNonExpired, emptyList());

	}

}
