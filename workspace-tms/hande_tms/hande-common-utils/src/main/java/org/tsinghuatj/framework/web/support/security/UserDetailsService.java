package org.tsinghuatj.framework.web.support.security;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
	private final Map<String, Date> map = Collections.synchronizedMap(new HashMap<String, Date>());

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		synchronized (map) {
			if (map.get(username) != null && map.get(username).after(new Date())) {
				return null;
			}
			map.put(username, DateTime.now().plusSeconds(3).toDate());

			try {
			    
			} catch (Exception e) {
				throw new RuntimeException(e);

			}
		}
		return null;
	}
}
