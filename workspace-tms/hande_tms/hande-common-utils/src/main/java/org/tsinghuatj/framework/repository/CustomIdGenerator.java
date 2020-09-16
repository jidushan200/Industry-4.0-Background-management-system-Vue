package org.tsinghuatj.framework.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.tsinghuatj.framework.service.IdGenerator;

import com.alibaba.druid.pool.DruidDataSource;

@Service("customIdGenerator")
public class CustomIdGenerator implements IdGenerator, InitializingBean {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	private final Long increaseBound = 1l;
	private Long dbid = 1l;

	@Value("${spring.datasource.tms.driver-class-name}")
	private String driverClassName;

	@Value("${spring.datasource.tms.url}")
	private String url;

	@Value("${spring.datasource.tms.username}")
	private String username;

	@Value("${spring.datasource.tms.password}")
	private String password;

	private @Autowired JdbcTemplate jdbcTemplate;

	public Long getUId() {
		return getUniqueId();
	}

	public String getSuid() {
		return getUId().toString();
	}

	public synchronized Long getUniqueId() {
		if (dbid > 0) {
			return updateMaxId();
		}
		return null;
	}

	public Long updateMaxId() {
		String sql = "select MAX_ from t_db_id where ID_=" + dbid;
		List<Long> ids = jdbcTemplate.queryForList(sql, Long.class);
		Long max = -1l;
		if (ids != null && !ids.isEmpty()) {
			max = ids.get(0);
		}
		String updatesql = "UPDATE t_db_id SET MAX_=? WHERE ID_=?";
		max += increaseBound;
		jdbcTemplate.update(updatesql, max, dbid);
		return max;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		String sql = "select ID_ from t_db_id";
		List<Long> ids = jdbcTemplate.queryForList(sql, Long.class);
		if (ids != null && !ids.isEmpty()) {
			dbid = ids.get(0);
		}
	}

	@Bean
	public JdbcTemplate jdbcTemplate() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return new JdbcTemplate(dataSource);
	}

}
