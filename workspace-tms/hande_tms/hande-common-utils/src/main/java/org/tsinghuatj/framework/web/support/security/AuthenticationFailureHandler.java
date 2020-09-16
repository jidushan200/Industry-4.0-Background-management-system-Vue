package org.tsinghuatj.framework.web.support.security;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.tsinghuatj.framework.domain.AjaxReturn;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AuthenticationFailureHandler implements org.springframework.security.web.authentication.AuthenticationFailureHandler {
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
		try (Writer writer = response.getWriter();) {
			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			response.setCharacterEncoding("UTF-8"); // 设置编码格式
			String error = "";
			if (null != exception) {
				error = exception.getClass().getSimpleName();
			}
			objectMapper.writeValue(writer, new AjaxReturn(501, "Login Failure", error));
		}

	}
}
