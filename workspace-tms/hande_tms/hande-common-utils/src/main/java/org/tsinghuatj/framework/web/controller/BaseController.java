package org.tsinghuatj.framework.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.CustomUser;
import org.tsinghuatj.framework.support.BusinessException;
import org.tsinghuatj.framework.support.TokenException;
import org.tsinghuatj.framework.web.support.binder.DoubleEditor;

import com.alibaba.fastjson.support.spring.FastJsonJsonView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseController {
	protected @Autowired MessageSource messageSource;
	protected @Value("${url.resources.cdn:}") String resources_cdn;
	protected @Value("${url.resources.attachment:}") String resources_attachment;

	@ModelAttribute
	public void _init(ModelAndView mav) throws BusinessException {
		mav.addObject("dnss", Arrays.asList(resources_cdn, resources_attachment));
		mav.addObject("cdn", resources_cdn);
		mav.addObject("attachment", resources_attachment);
	}

	/**
	 * 获取登录用户登录信息-id，如果用户没有登录，则返回0
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public Long getAuthentication() throws BusinessException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			CustomUser user = (CustomUser) authentication.getPrincipal();
			return user.getId();
		}
		return 1L;
	}

	/**
	 * 获取登录用户信息-id,realName,roleId
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public CustomUser getCompleteAuthentication() throws BusinessException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			return (CustomUser) authentication.getPrincipal();
		}
		return null;
	}

	/**
	 * 判断用户角色
	 * 
	 * @return
	 * @throws BusinessException
	 */
	/*public boolean validateUserRole(CustomUser user, Integer requireRoleId) throws BusinessException {
		Validate.isTrue(user.getId() > 0);
		if (!user.getRoleId().equals(requireRoleId)) {
			throw new BusinessException("role.do.not.match.error");
		}
		return true;
	}
*/
	/**
	 * 时间类型绑定
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// CustomDateEditor dateEditor = new CustomDateEditor(fmt, true);
		// binder.registerCustomEditor(Date.class, dateEditor);
		binder.registerCustomEditor(Float.class, new DoubleEditor());
		binder.registerCustomEditor(Double.class, new DoubleEditor());
	}

	/**
	 * 异常页面控制
	 *
	 * @param throwable
	 * @param request
	 * @return
	 */
	@ExceptionHandler(Throwable.class)
	@ResponseBody
	@ResponseStatus(code = HttpStatus.OK)
	public ModelAndView runtimeExceptionHandler(Throwable throwable, WebRequest request) {
		FastJsonJsonView jsonView = new FastJsonJsonView();
		if (throwable instanceof BusinessException) {
			jsonView.addStaticAttribute("code", ((BusinessException) throwable).getCode());
			jsonView.addStaticAttribute("info", getMessage(((BusinessException) throwable).getInfo(), ((BusinessException) throwable).getParam()));
		} else if (throwable instanceof TokenException) {
			jsonView.addStaticAttribute("code", ((BusinessException) throwable).getCode());
			jsonView.addStaticAttribute("info", getMessage(((BusinessException) throwable).getInfo(), ((BusinessException) throwable).getParam()));
		} else if (throwable instanceof BindException) {
			jsonView.addStaticAttribute("code", 501);
			StringBuffer errorMessage = new StringBuffer();
			List<FieldError> fieldErrors = ((BindException) throwable).getFieldErrors();
			for (FieldError fieldError : fieldErrors) {
				errorMessage.append(fieldError.getField());
				errorMessage.append("-");
				errorMessage.append(fieldError.getDefaultMessage());
				errorMessage.append(" ");
			}
			jsonView.addStaticAttribute("info", errorMessage);
		} else if (throwable instanceof IllegalArgumentException) {
			jsonView.addStaticAttribute("code", 501);
			jsonView.addStaticAttribute("info", throwable.getMessage());
		} else {
			jsonView.addStaticAttribute("code", 501);
			jsonView.addStaticAttribute("info", getMessage("action.info.error"));
		}
		if (log.isDebugEnabled()) {
			log.debug("服务器异常:"+throwable.getMessage());
		}
		return new ModelAndView(jsonView);
	}

	/**
	 * 获取国际化资源
	 * 
	 * @param code
	 * @return
	 */
	public String getMessage(String code) {
		return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
	}

	/**
	 * 获取国际化资源
	 *
	 * @param key
	 *            配置文件中key
	 * @param param
	 *            占位符
	 * @return String
	 */
	public String getMessage(String key, Object[] param) {
		return messageSource.getMessage(key, param, LocaleContextHolder.getLocale());
	}

	public <T> AjaxReturn pageReturn(Collection<T> data) {
		Map<String, Collection<T>> map = new HashMap<String, Collection<T>>();
		map.put("rows", data);
		return new AjaxReturn(200, "", map);
	}

	protected ResponseEntity<byte[]> getResponseEntity(byte[] data, String fileName) throws UnsupportedEncodingException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", new String(fileName.getBytes("UTF-8"), "ISO8859-1")); // 解决文件名中文乱码问题
		return new ResponseEntity<>(data, headers, HttpStatus.OK);
	}

}
