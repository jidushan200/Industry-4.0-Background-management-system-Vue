package org.tsinghuatj.framework.web.support.security;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.utils.NumberUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthenticationSuccessHandler implements org.springframework.security.web.authentication.AuthenticationSuccessHandler {
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		try (Writer writer = response.getWriter();) {
			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			String sessionId = null;
			if (authentication != null && authentication.isAuthenticated()) {
				NumberUtils.toLong(authentication.getName());				
				WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
				sessionId = details.getSessionId();				
			}
			objectMapper.writeValue(writer, new AjaxReturn(200, "login success", sessionId));
		}
		// response.sendRedirect("//172.16.142.222:38001/index");  
	}
}
