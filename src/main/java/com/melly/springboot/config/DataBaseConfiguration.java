package com.melly.springboot.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@MapperScan(value = {"com.melly.springboot.mapper"}, sqlSessionTemplateRef = "sqlSessionTemplate")
@EnableTransactionManagement
public class DataBaseConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DataBaseConfiguration.class);

    private static final int maxLifetime = 1800;

    @Value("${spring.datasource.druid.db-type}")
    private Class<? extends DataSource> dataSourceType;

    @Resource(name = "writeDataSource")
    private DataSource dataSource;

    @Bean(name = "writeDataSource", destroyMethod = "close", initMethod = "init")
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource writeDataSource(Environment env) {
        log.info("-------------------- writeDataSource init ---------------------");
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        ds.setUniqueResourceName("writeDataSource");
        ds.setXaProperties(build(env, "spring.datasource.druid.", ds));
        return ds;
    }

    private Properties build(Environment env, String prefix, AtomikosDataSourceBean ds) {
        int maxActive = env.getProperty(prefix + "maxActive", Integer.class);
        int initialSize = env.getProperty(prefix + "initialSize", Integer.class);
        int minIdle = env.getProperty(prefix + "minIdle", Integer.class);
        int maxWait = env.getProperty(prefix + "maxWait", Integer.class);
        ds.setMaxPoolSize(maxActive);
        ds.setMinPoolSize(initialSize);
        ds.setBorrowConnectionTimeout(maxWait / 1000);
        ds.setMaxLifetime(maxLifetime);
        Properties prop = new Properties();
        prop.put("removeAbandoned", true);
        prop.put("removeAbandonedTimeout", maxLifetime);
        prop.put("url", env.getProperty(prefix + "url"));
        prop.put("username", env.getProperty(prefix + "username"));
        prop.put("password", env.getProperty(prefix + "password"));
        prop.put("driverClassName", env.getProperty(prefix + "driver-class-name", ""));
        prop.put("initialSize", initialSize);
        prop.put("maxActive", maxActive);
        prop.put("numTestsPerEvictionRun", maxActive);
        prop.put("minIdle", minIdle);
        prop.put("maxWait", maxWait);
        prop.put("validationQuery", env.getProperty(prefix + "validationQuery"));
        prop.put("testOnBorrow", env.getProperty(prefix + "testOnBorrow", Boolean.class));
        prop.put("testOnReturn", env.getProperty(prefix + "testOnReturn", Boolean.class));
        prop.put("testWhileIdle", env.getProperty(prefix + "testWhileIdle", Boolean.class));
        prop.put("filters", env.getProperty(prefix + "filters"));
//        prop.put("connectionProperties", env.getProperty(prefix + "connectionProperties"));
        return prop;
    }

    @Bean(name = "sqlSessionFactoryOne")
    public SqlSessionFactory sqlSessionFactoryOne(@Qualifier("writeDataSource") DataSource dataSource)
            throws Exception {
        return createSqlSessionFactory(dataSource);
    }

    @Bean(name = "sqlSessionTemplate")
    public CustomSqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactoryOne") SqlSessionFactory factoryOne) {
        Map<Object, SqlSessionFactory> sqlSessionFactoryMap = new HashMap<>();
        sqlSessionFactoryMap.put(DataSourceEnums.WRITE.getType(), factoryOne);
        CustomSqlSessionTemplate customSqlSessionTemplate = new CustomSqlSessionTemplate(factoryOne);
        customSqlSessionTemplate.setTargetSqlSessionFactory(sqlSessionFactoryMap);
        return customSqlSessionTemplate;
    }

    private SqlSessionFactory createSqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:persistence/**/*.xml"));
        sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        sqlSessionFactoryBean.getObject().getConfiguration().setCacheEnabled(false);
        sqlSessionFactoryBean.getObject().getConfiguration().setLazyLoadingEnabled(false);
        return sqlSessionFactoryBean.getObject();
    }

}
