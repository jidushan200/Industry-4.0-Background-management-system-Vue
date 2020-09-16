package org.tsinghuatj.support.interceptor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.tsinghuatj.framework.domain.CustomUser;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.utils.StringUtils;
import org.tsinghuatj.support.annotation.OperateLog;
import org.tsinghuatj.sys.domain.OperationLog;
import org.tsinghuatj.sys.repository.OperationLogMapper;

public class OperateLogHandler extends HandlerInterceptorAdapter {

	private @Resource OperationLogMapper operationLogMapper;

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
		if (handler instanceof HandlerMethod) {
			// 获取方法注解，根据注解的类型
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			OperateLog operateLog = handlerMethod.getMethodAnnotation(OperateLog.class);
			if (operateLog != null) {
				String info = operateLog.info();
				String[] params = operateLog.params();
				List<String> valueList = new ArrayList<String>();
				for (String param : params) {
					valueList.add(request.getParameter(param));
				}
				Integer size = valueList.size();
				if (size == 1) {
					info = String.format(info, valueList.get(0));
				} else if (size == 2) {
					info = String.format(info, valueList.get(0), valueList.get(1));
				} else if (size == 3) {
					info = String.format(info, valueList.get(0), valueList.get(1), valueList.get(2));
				}
				CustomUser user = getAuthentication();
				Long userId = user.getId();
				String ip = "";
				try {
					ip = InetAddress.getLocalHost().getHostAddress();
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				}
				Date date = DateTime.now().toDate();
				OperationLog log = OperationLog.builder().pkId(StringUtils.uuid()).createTime(date)//
						.createUser(userId)//
						.updateTime(date)//
						.updateUser(userId)//
						.delMark(0)//
						.operateId(userId)//
						.operateName(user.getRealname()).operateInfo(info)//
						.ip(ip).build();
				operationLogMapper.insert(log);
			}
		}
	}

	/**
	 * 获取登录用户信息，如果用户没有登录，则返回0
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public CustomUser getAuthentication() throws BusinessException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			return (CustomUser) authentication.getPrincipal();
		}
		return null;
	}
}
