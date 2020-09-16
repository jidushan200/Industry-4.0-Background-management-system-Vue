package org.tsinghuatj.framework.web.support.interceptor;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.tsinghuatj.framework.domain.CustomUser;
import org.tsinghuatj.framework.support.TokenException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * 自定义JWT认证过滤器 该类继承自BasicAuthenticationFilter，在doFilterInternal方法中，
 * 从http头的Authorization 项读取token数据，然后用Jwts包提供的方法校验token的合法性。
 * 如果校验通过，就认为这是一个取得授权的合法请求
 */
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

	private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String header = request.getHeader("Authorization");
		if (header == null || !header.startsWith("Bearer ")) {
			header = request.getParameter("token");
		}
		if (header == null || !header.startsWith("Bearer ")) {
			chain.doFilter(request, response);
			return;
		}
		UsernamePasswordAuthenticationToken authentication = getAuthentication(header);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(String header) {
		//long start = System.currentTimeMillis();
		String userStr = null;
		try {
			userStr = Jwts.parser().setSigningKey(ConstantKey.SIGNING_KEY).parseClaimsJws(header.replace("Bearer ", "")).getBody().getSubject();
			//long end = System.currentTimeMillis();
			//logger.info("执行时间: {}", (end - start) + " 毫秒");
			if (userStr != null) {
				String userId = userStr.split("-")[0];
				String roleId = userStr.split("-")[1];
				String departmentId = userStr.split("-")[2];
				String realName = userStr.split("-")[3];
				String[] split = userStr.split("-")[4].split(",");
				ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
				for (int i = 0; i < split.length; i++) {
					authorities.add(new SimpleGrantedAuthority(split[i]));
				}
				CustomUser user = new CustomUser(Long.parseLong(userId),realName, Long.parseLong(roleId), Long.parseLong(departmentId));
				return new UsernamePasswordAuthenticationToken(user, null, authorities);
			}

		} catch (ExpiredJwtException e) {
			logger.error("Token已过期: {} " + e);
			throw new TokenException("expired.jwt.exception");
		} catch (UnsupportedJwtException e) {
			logger.error("Token格式错误: {} " + e);
			throw new TokenException("Token格式错误");
		} catch (MalformedJwtException e) {
			logger.error("Token没有被正确构造: {} " + e);
			throw new TokenException("Token没有被正确构造");
		} catch (SignatureException e) {
			logger.error("签名失败: {} " + e);
			throw new TokenException("签名失败");
		} catch (IllegalArgumentException e) {
			logger.error("非法参数异常: {} " + e);
			throw new TokenException("非法参数异常");
		}
		return null;
	}

}
