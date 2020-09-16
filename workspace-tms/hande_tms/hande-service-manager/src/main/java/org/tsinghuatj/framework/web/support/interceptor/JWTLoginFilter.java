package org.tsinghuatj.framework.web.support.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.tsinghuatj.framework.domain.AjaxReturn;
import org.tsinghuatj.framework.domain.CustomUser;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * 验证用户名密码正确后，生成一个token，并将token返回给客户端
 * 该类继承自UsernamePasswordAuthenticationFilter，重写了其中的2个方法 attemptAuthentication
 * ：接收并解析用户凭证。 successfulAuthentication ：用户成功登录后，这个方法会被调用，我们在这个方法里生成token。
 * 
 * @author zhaoxinguo on 2017/9/12.
 */
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	private ObjectMapper objectMapper = new ObjectMapper();

	public JWTLoginFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	// 接收并解析用户凭证
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		String usrname = req.getParameter("loginName");
		String password = req.getParameter("password");
		return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usrname, password, new ArrayList<>()));

	}

	// 用户成功登录后，这个方法会被调用，我们在这个方法里生成token
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		// builder the token
		String token = null;

		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		// 定义存放角色集合的对象
		String auths = "";
		for (GrantedAuthority grantedAuthority : authorities) {
			auths += grantedAuthority.getAuthority() + ",";
		}
		// 设置过期时间
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		//calendar.add(Calendar.DAY_OF_MONTH, 30);
		calendar.add(Calendar.MONTH, 1);
		Date time = calendar.getTime();
		CustomUser user = (CustomUser) auth.getPrincipal();
		token = "Bearer " + Jwts.builder().setSubject(user.getId() + "-"+user.getRoleId()+"-"+user.getDepartmentId()+ "-"+user.getRealname() + "-" + auths).setExpiration(time) // 设置过期时间30天
				.signWith(SignatureAlgorithm.HS512, ConstantKey.SIGNING_KEY) // 采用什么算法是可以自己选择的，不一定非要采用HS512
				.compact();
		//登录成功后，返回token到header里面
		response.addHeader("Authorization", token);
		response.setContentType("application/json; charset=UTF-8");	//要写在response.getWriter()之前	
		response.setStatus(HttpServletResponse.SC_OK);
		try (Writer writer = response.getWriter();) {			
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("token", token);
			data.put("access", auths.split(","));
			data.put("userId", user.getId());
			data.put("roleId", user.getRoleId());
			data.put("departmentId", user.getDepartmentId());
			data.put("realName", user.getRealname());
			objectMapper.writeValue(writer, new AjaxReturn(200, "Login Success", data));
		}

	}

}
