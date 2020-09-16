package org.tsinghuatj.configurer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.tsinghuatj.framework.web.support.interceptor.CustomAuthenticationProvider;
import org.tsinghuatj.framework.web.support.interceptor.UnauthorizedEntryPoint;
import org.tsinghuatj.framework.web.support.interceptor.JWTAuthenticationFilter;
import org.tsinghuatj.framework.web.support.interceptor.JWTLoginFilter;
import org.tsinghuatj.framework.web.support.interceptor.MyAccessDeniedHandler;
import org.tsinghuatj.sys.service.ILoginService;
import org.tsinghuatj.sys.service.impl.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private @Autowired UserDetailsServiceImpl customUserDetailsService;

	private @Autowired ILoginService loginService;

	//都可以访问
	private static final String[] AUTH_WHITELIST = { "/users/signup", "/index", "/v2/api-docs", "/swagger-resources",
			"/swagger-resources/**", "/configuration/ui", "/configuration/security", "/swagger-ui.html", "/webjars/**",
			"/favicon.ico", "/file/*/*" };

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll().anyRequest().authenticated() // 所有请求需要身份认证
				.and().exceptionHandling().authenticationEntryPoint(new UnauthorizedEntryPoint()).accessDeniedHandler(new MyAccessDeniedHandler())
				.and().addFilter(new JWTLoginFilter(authenticationManager())).addFilter(new JWTAuthenticationFilter(authenticationManager())).logout() // 默认注销行为为logout，可以通过下面的方式来修改
				.logoutUrl("/logout").logoutSuccessUrl("/login")// 设置注销成功后跳转页面，默认是跳转到登录页面;
				// .logoutSuccessHandler(customLogoutSuccessHandler)
				.permitAll();
		http.headers().cacheControl().and().frameOptions().disable();
	}
	
	
	/*
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/**").permitAll().and().csrf().disable();
		http.headers().cacheControl().and().frameOptions().disable();
	}*/

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// 使用自定义身份验证组件
		auth.authenticationProvider(new CustomAuthenticationProvider(customUserDetailsService, loginService));		
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/public/**", "/v2/api-docs*", "/webjars/**", "/swagger-resources/**",
				"/swagger-ui.html*", "/favicon.ico");
	}

}
