package org.tsinghuatj.framework.web.support.interceptor;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.tsinghuatj.framework.domain.AjaxReturn;

import com.fasterxml.jackson.databind.ObjectMapper;
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
			throws IOException {
		String error = e.getMessage();
		try (Writer writer = response.getWriter();) {
			response.setContentType("application/json; charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			objectMapper.writeValue(writer, new AjaxReturn(401, error, null));
		}
	}

}
