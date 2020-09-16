package org.tsinghuatj.configurer;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.tsinghuatj.framework.web.support.interceptor.SecureHandlerInterceptorAdapter;
import org.tsinghuatj.support.interceptor.OperateLogHandler;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;

@Configuration
public class WebMvcConfigurer extends WebMvcConfigurationSupport {
	private @Autowired Environment environment;

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.clear();
		// 转换为utf-8编码，防止乱码
		StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
		stringHttpMessageConverter.setWriteAcceptCharset(false);
		stringHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON_UTF8, MediaType.APPLICATION_JSON));
		converters.add(stringHttpMessageConverter);

		// 允许字节下载
		ByteArrayHttpMessageConverter byteArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();
		converters.add(byteArrayHttpMessageConverter);

		// 使用fastjson输出json
		FastJsonHttpMessageConverter4 fastJsonHttpMessageConverter4 = new FastJsonHttpMessageConverter4();
		//fastJsonHttpMessageConverter4.getFastJsonConfig().setSerializerFeatures(SerializerFeature.BrowserCompatible);
		
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.BrowserCompatible,SerializerFeature.PrettyFormat,SerializerFeature.DisableCircularReferenceDetect);
        
        fastJsonHttpMessageConverter4.setFastJsonConfig(fastJsonConfig);
		converters.add(fastJsonHttpMessageConverter4);

		super.configureMessageConverters(converters);
	}

	@Bean
	public ResourceUrlEncodingFilter resourceUrlEncodingFilter() {
		ResourceEncodingFilter resourceEncodingFilter = new ResourceEncodingFilter();
		resourceEncodingFilter.setUrlCdn(environment.getProperty("url.resources.cdn", ""));
		return resourceEncodingFilter;
	}

	@Bean
	public LocaleResolver localeResolver() {
		CookieLocaleResolver slr = new CookieLocaleResolver();
		// 设置默认区域,
		slr.setDefaultLocale(Locale.CHINA);
		slr.setCookieMaxAge(3600);// 设置cookie有效期.
		return slr;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		// 设置请求地址的参数,默认为：locale
		lci.setParamName(LocaleChangeInterceptor.DEFAULT_PARAM_NAME);
		return lci;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
		registry.addInterceptor(secureHandlerInterceptorAdapter());
		registry.addInterceptor(operateLogHandler());
		super.addInterceptors(registry);
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new DateConverter());
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/").addResourceLocations("classpath:/resources/").addResourceLocations("classpath:/static/");
		super.addResourceHandlers(registry);

	}

	/**
	 * 国际化
	 * 
	 * @return
	 */
	@Bean
	public SecureHandlerInterceptorAdapter secureHandlerInterceptorAdapter() {
		SecureHandlerInterceptorAdapter interceptor = new SecureHandlerInterceptorAdapter();
		return interceptor;
	}

	/**
	 * 日志
	 * 
	 * @return
	 */
	@Bean
	public OperateLogHandler operateLogHandler() {
		OperateLogHandler interceptor = new OperateLogHandler();
		return interceptor;
	}

	@Bean
	public CorsFilter corsFilter() {
		final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		final CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.addAllowedOrigin("*");
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedMethod("*");
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}

	// The request was rejected because the URL was not normalized.
	@Bean
	public HttpFirewall httpFirewall() {
		return new DefaultHttpFirewall();
	}

	/**
	 * 添加XSS过滤器
	 * 
	 * @return
	 */
	// @Bean
	// public XssParameterFilter xssParameterFilter() {
	// return new XssParameterFilter();
	// }
}