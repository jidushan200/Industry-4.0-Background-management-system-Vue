package org.tsinghuatj.configurer;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;


@Configuration
@MapperScan(basePackages = "org.tsinghuatj.erp.repository", sqlSessionTemplateRef  = "erpSqlSessionTemplate")
public class DataSourceErpConfig {

	@Value("${spring.datasource.erp.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.erp.url}")
    private String url;

    @Value("${spring.datasource.erp.username}")
    private String username;

    @Value("${spring.datasource.erp.password}")
    private String password;
	
    @Bean(name = "erpDataSource")
    @Primary
    public DataSource dataSource() {
    	DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(this.driverClassName);
        dataSource.setUrl(this.url);
        dataSource.setUsername(this.username);
        dataSource.setPassword(this.password);
        return dataSource;
    }

    @Bean(name = "erpSqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("erpDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:erpmapper/*Mapper.xml"));
        return bean.getObject();
    }

    @Bean(name = "erpTransactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier("erpDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "erpSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("erpSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
