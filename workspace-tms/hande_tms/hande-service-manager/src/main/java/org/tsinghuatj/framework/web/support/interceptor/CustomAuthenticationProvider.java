package org.tsinghuatj.framework.web.support.interceptor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.tsinghuatj.framework.domain.CustomUser;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.StringUtils;
import org.tsinghuatj.sys.domain.OperationLog;
import org.tsinghuatj.sys.service.ILoginService;

public class CustomAuthenticationProvider implements AuthenticationProvider {

	private UserDetailsService userDetailsService;
	private ILoginService loginService;

	public CustomAuthenticationProvider(UserDetailsService userDetailsService, ILoginService loginService) {
		this.userDetailsService = userDetailsService;
		this.loginService = loginService;

	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// 获取认证的用户名 & 密码
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		// 认证逻辑
		CustomUser userDetails = (CustomUser) userDetailsService.loadUserByUsername(name);

		PasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
		if (null == userDetails || !bcryptPasswordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Bad Credentials");
		}

		Set<GrantedAuthority> authorities = new LinkedHashSet<GrantedAuthority>();
		List<String> auths;
		try {
			auths = loginService.listAutoByUser(userDetails.getId());
			if (CollectionUtils.isNotEmpty(auths)) {
				for (String auth : auths) {
					authorities.add(new SimpleGrantedAuthority(auth));
				}
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		// 生成令牌 这里令牌里面存入了:name,password,authorities, 当然你也可以放其他内容
		Date date = new Date();
		Long userId = userDetails.getId();
		String ip = "";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e1) {			
			e1.printStackTrace();
		}
		OperationLog log = OperationLog.builder().pkId(StringUtils.uuid()).createTime(date)//
				.createUser(userId)//
				.updateTime(new Date())//
				.updateUser(userId)//
				.delMark(0)//
				.operateId(userId)//
				.operateName(userDetails.getRealname()).operateInfo("用户登录")//
				.ip(ip)//
				.build();

		try {
			loginService.saveLoginlog(log);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, password, authorities);
		return auth;

	}

	/**
	 * 是否可以提供输入类型的认证服务
	 * 
	 * @param authentication
	 * @return
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
