package org.tsinghuatj.framework.web.support.security;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.tsinghuatj.framework.domain.AjaxReturn;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AccessDeniedHandler implements org.springframework.security.web.access.AccessDeniedHandler {
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		try (Writer writer = response.getWriter();) {
			response.setContentType("application/json; charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			objectMapper.writeValue(writer, new AjaxReturn(402, null, null));
		}
	}

}