package org.tsinghuatj.framework.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.User;
import org.tsinghuatj.framework.domain.CustomUser;
import org.tsinghuatj.framework.domain.UserDetail;

public class SecurityUtils {
	/**
	 * 获取登录用户编号
	 * 
	 * @return
	 */
	public static Long getAuthentication() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			CustomUser user = (CustomUser) authentication.getPrincipal();
			return user.getId();
		}
		return 0L;
	}

	/**
	 * 用户是否登录
	 * 
	 * @return
	 */
	public static Boolean isAuthenticated() {		
		return getAuthentication() > 0;
	}

	/**
	 * 是否具备权限
	 * 
	 * @param authority
	 * @return
	 */
	public static Boolean hasAllAuthority(String... authoritys) {
		if (authoritys != null && authoritys.length > 0) {
			Set<String> set = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
			return set.containsAll(Arrays.asList(authoritys));
		}
		return true;
	}

	/**
	 * 具备任一项授权
	 * 
	 * @param authoritys
	 * @return
	 */
	public static Boolean hasAnyAuthority(String... authoritys) {
		if (authoritys != null && authoritys.length > 0) {
			Set<String> set = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
			Boolean flag = false;
			for (String authority : authoritys) {
				if (set.contains(authority)) {
					flag = true;
					break;
				}
			}
			return flag;
		} else {
			return true;
		}
	}

	/**
	 * 不具备授权
	 * 
	 * @param authoritys
	 * @return
	 */
	public static Boolean notAuthority(String... authoritys) {
		if (authoritys != null && authoritys.length > 0) {
			Set<String> set = AuthorityUtils.authorityListToSet(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
			Boolean flag = true;
			for (String authority : authoritys) {
				if (set.contains(authority)) {
					flag = false;
					break;
				}
			}
			return flag;
		}
		return true;
	}

	/**
	 * 辨别用户是否已经登录
	 *
	 * @param request
	 * @param sessionRegistry
	 * @param loginedUser
	 */
	public static void deleteSameUser(HttpServletRequest request, SessionRegistry sessionRegistry, UserDetail loginedUser) {
		// SecurityContext sc = (SecurityContext)
		// request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
		List<SessionInformation> sessionsInfo;
		sessionsInfo = sessionRegistry.getAllSessions(loginedUser, true);
		String currentSessionId;
		if (null != sessionsInfo && sessionsInfo.size() == 0) {
			sessionRegistry.registerNewSession(request.getSession().getId(), loginedUser);
			sessionsInfo = sessionRegistry.getAllSessions(loginedUser, false);
		}
		currentSessionId = sessionsInfo.get(0).getSessionId();
		List<Object> o = sessionRegistry.getAllPrincipals();
		for (Object principal : o) {
			if (principal instanceof UserDetail && (loginedUser.getUsername().equals(((UserDetail) principal).getUsername()))) {
				List<SessionInformation> oldSessionsInfo = sessionRegistry.getAllSessions(principal, false);
				if (null != oldSessionsInfo && oldSessionsInfo.size() > 0 && !oldSessionsInfo.get(0).getSessionId().equals(currentSessionId)) {
					for (SessionInformation sessionInformation : sessionsInfo) {
						// 当前session失效
						sessionInformation.expireNow();
						SecurityContextHolder.getContext().setAuthentication(null);
						sessionRegistry.removeSessionInformation(currentSessionId);
						// throw new
						// GeneralServerExistException(ErrorMessage.ALONG_LOGIN_ERROTR.toString());
					}
				}
			}
		}
	}

	public static void dropPreviousUser(HttpServletRequest request, SessionRegistry sessionRegistry, UserDetail userDetail) {
		//Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		List<SessionInformation> sessionsList ;
		sessionsList = sessionRegistry.getAllSessions(userDetail, true);
		if (null == sessionsList || sessionsList.size()<1) {
			return;
		}
	       		
		String currentSessionId = sessionsList.get(0).getSessionId();
		List<Object> list = sessionRegistry.getAllPrincipals();
		for (Object principal : list) {
			if (principal instanceof User && (userDetail.getUsername().equals(((UserDetail) principal).getUsername()))) {
				List<SessionInformation> oldSessionList = sessionRegistry.getAllSessions(principal, false);
				if (null != oldSessionList && oldSessionList.size() > 0 && !oldSessionList.get(0).getSessionId().equals(currentSessionId)) {
					for (SessionInformation sessionInformation : oldSessionList) {
						sessionRegistry.removeSessionInformation(currentSessionId);
						sessionInformation.expireNow();
						sessionInformation.isExpired();
						SecurityContextHolder.getContext().setAuthentication(null);
					}
				}
			}
		}

	}

}
