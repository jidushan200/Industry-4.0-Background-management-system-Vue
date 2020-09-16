package org.tsinghuatj.configurer;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.resource.ResourceUrlProvider;
import org.springframework.web.servlet.resource.ResourceUrlProviderExposingInterceptor;
import org.springframework.web.util.UrlPathHelper;

public class ResourceEncodingFilter extends org.springframework.web.servlet.resource.ResourceUrlEncodingFilter {
	private static final Log logger = LogFactory.getLog(ResourceEncodingFilter.class);
	private String urlCdn;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
			throw new ServletException("ResourceUrlEncodingFilter just supports HTTP requests");
		}
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		filterChain.doFilter(httpRequest, new ResourceUrlEncodingResponseWrapper(httpRequest, httpResponse, urlCdn));
	}

	private static class ResourceUrlEncodingResponseWrapper extends HttpServletResponseWrapper {

		private final HttpServletRequest request;

		/*
		 * Cache the index and prefix of the path within the DispatcherServlet
		 * mapping
		 */
		private Integer indexLookupPath;

		private String prefixLookupPath;
		private String urlCdn;

		public ResourceUrlEncodingResponseWrapper(HttpServletRequest request, HttpServletResponse wrapped, String urlCdn) {
			super(wrapped);
			this.request = request;
			this.urlCdn = urlCdn;
		}

		@Override
		public String encodeURL(String url) {
			ResourceUrlProvider resourceUrlProvider = getResourceUrlProvider();
			if (resourceUrlProvider == null) {
				logger.debug("Request attribute exposing ResourceUrlProvider not found");
				return super.encodeURL(url);
			}

			initLookupPath(resourceUrlProvider);
			if (url.startsWith(this.prefixLookupPath)) {
				int suffixIndex = getQueryParamsIndex(url);
				String suffix = url.substring(suffixIndex);
				String lookupPath = url.substring(this.indexLookupPath, suffixIndex);
				lookupPath = resourceUrlProvider.getForLookupPath(lookupPath);
				if (lookupPath != null) {
					return super.encodeURL(this.urlCdn + this.prefixLookupPath + lookupPath + suffix);
				}
			}

			return super.encodeURL(url);
		}

		private ResourceUrlProvider getResourceUrlProvider() {
			return (ResourceUrlProvider) this.request.getAttribute(ResourceUrlProviderExposingInterceptor.RESOURCE_URL_PROVIDER_ATTR);
		}

		private void initLookupPath(ResourceUrlProvider urlProvider) {
			if (this.indexLookupPath == null) {
				UrlPathHelper pathHelper = urlProvider.getUrlPathHelper();
				String requestUri = pathHelper.getRequestUri(this.request);
				String lookupPath = pathHelper.getLookupPathForRequest(this.request);
				this.indexLookupPath = requestUri.lastIndexOf(lookupPath);
				this.prefixLookupPath = requestUri.substring(0, this.indexLookupPath);

				if ("/".equals(lookupPath) && !"/".equals(requestUri)) {
					String contextPath = pathHelper.getContextPath(this.request);
					if (requestUri.equals(contextPath)) {
						this.indexLookupPath = requestUri.length();
						this.prefixLookupPath = requestUri;
					}
				}
			}
		}

		private int getQueryParamsIndex(String url) {
			int index = url.indexOf("?");
			return (index > 0 ? index : url.length());
		}
	}

	public String getUrlCdn() {
		return urlCdn;
	}

	public void setUrlCdn(String urlCdn) {
		this.urlCdn = urlCdn;
	}
}