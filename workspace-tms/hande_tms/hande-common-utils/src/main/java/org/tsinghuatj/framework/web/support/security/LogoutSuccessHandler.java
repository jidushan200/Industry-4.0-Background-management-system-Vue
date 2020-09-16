package org.tsinghuatj.framework.web.support.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.tsinghuatj.framework.domain.AjaxReturn;

import com.fasterxml.jackson.databind.ObjectMapper;

public class LogoutSuccessHandler implements org.springframework.security.web.authentication.logout.LogoutSuccessHandler {
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
		if (StringUtils.equalsIgnoreCase(request.getMethod(), "POST")) {
			try (PrintWriter writer = response.getWriter();) {
				response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
				objectMapper.writeValue(writer, new AjaxReturn(200, null, null));
			}
		} else {
			request.getRequestDispatcher("/").forward(request, response);
		}
	}

}
