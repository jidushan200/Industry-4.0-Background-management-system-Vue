package org.tsinghuatj.framework.web.support.security;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.tsinghuatj.framework.domain.AjaxReturn;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LoginSuccessHandler implements AuthenticationSuccessHandler, InitializingBean {
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		try (Writer writer = response.getWriter();) {
			response.setContentType("application/json; charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			objectMapper.writeValue(writer, new AjaxReturn(200, null, null));
		}
	}

}
