package org.tsinghuatj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	/*@Order(Ordered.LOWEST_PRECEDENCE)
	@Configuration
	@EnableWebSecurity
	protected static class WebSecurityConfig extends WebSecurityConfigurerAdapter {
		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().antMatchers("/", "/**").permitAll().and().csrf().disable();
			http.headers().frameOptions().disable();
		}
	}*/
	
	
}
