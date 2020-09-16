package org.tsinghuatj.framework.web.support.interceptor;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.web.support.interceptor.ConstantKey;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class LoginSuccessHandler implements AuthenticationSuccessHandler, InitializingBean {
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void afterPropertiesSet() throws Exception {

	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException, ServletException {
		String token = null;
		try {
			Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
			// 定义存放角色集合的对象
			List<String> roleList = new ArrayList<String>();
			for (GrantedAuthority grantedAuthority : authorities) {
				roleList.add(grantedAuthority.getAuthority());
			}
			// 设置过期时间
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.MONTH, 1);
			Date time = calendar.getTime();
			token = Jwts.builder().setSubject(auth.getName() + "-" + roleList).setExpiration(time) // 设置过期时间30分钟
					.signWith(SignatureAlgorithm.HS512, ConstantKey.SIGNING_KEY) // 采用什么算法是可以自己选择的，不一定非要采用HS512
					.compact();
			// 登录成功后，返回token到header里面
			response.addHeader("Authorization", "Bearer " + token);
			try (Writer writer = response.getWriter();) {
				response.setContentType("application/json; charset=utf-8");
				response.setStatus(HttpServletResponse.SC_OK);
				objectMapper.writeValue(writer, new AjaxReturn(200, null, "Bearer " + token));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
