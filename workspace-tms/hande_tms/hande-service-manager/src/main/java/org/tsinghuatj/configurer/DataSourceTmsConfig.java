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
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;


@Configuration
@MapperScan(basePackages = {"org.tsinghuatj.tool.repository","org.tsinghuatj.base.repository","org.tsinghuatj.sys.repository","org.tsinghuatj.measure.repository","org.tsinghuatj.fixture.repository","org.tsinghuatj.mould.repository"}, sqlSessionTemplateRef  = "tmsSqlSessionTemplate")
public class DataSourceTmsConfig {

	@Value("${spring.datasource.tms.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.tms.url}")
    private String url;

    @Value("${spring.datasource.tms.username}")
    private String username;

    @Value("${spring.datasource.tms.password}")
    private String password;
	
	
    @Bean(name = "tmsDataSource")
    public DataSource dataSource() {
    	DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(this.driverClassName);
        dataSource.setUrl(this.url);
        dataSource.setUsername(this.username);
        dataSource.setPassword(this.password);
        return dataSource;
    }

    @Bean(name = "tmsSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("tmsDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*/*Mapper.xml"));
        return bean.getObject();
    }

    @Bean(name = "tmsTransactionManager")
    public DataSourceTransactionManager transactionManager(@Qualifier("tmsDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "tmsSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("tmsSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
