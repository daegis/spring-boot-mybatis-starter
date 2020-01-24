package cn.aegisa.spring.boot.mybatis;

import cn.aegisa.spring.boot.mybatis.component.service.impl.CommonServiceImpl;
import cn.aegisa.spring.boot.mybatis.component.spi.impl.CommonDaoImpl;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Using IntelliJ IDEA.
 *
 * @author XIANYINGDA at 9/10/2018 2:33 PM
 */
@SuppressWarnings("Duplicates")
@Configuration
@EnableConfigurationProperties(MybatisProperties.class)
@ConditionalOnProperty(prefix = "spring.mybatis", name = "master-url")
@Slf4j
public class MybatisAutoConfiguration {

    private final String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
    private final String VALIDATION_QUERY = "SELECT now()";
    private final String MAPPER_LOCATION = "mybatis/mapper/*.xml";
    private final String MAPPER_CONFIG = "mybatis/sql-map-config.xml";

    @Autowired
    private MybatisProperties properties;

    @Bean
    public Map<String, Object> mybatisMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("message", "ok");
        map.put("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        map.put("params", properties);
        return map;
    }

    private HikariDataSource parentDatasource(MybatisProperties properties) {
        final HikariDataSource hikari = new HikariDataSource();
        hikari.setDriverClassName(properties.getDriverClassName() == null ? DRIVER_CLASS_NAME : properties.getDriverClassName());
        hikari.setReadOnly(false);
        hikari.setConnectionTestQuery(VALIDATION_QUERY);
        return hikari;
    }

    private DataSource getDataSource(String dbUrl_slave, String username_slave, String password_slave, MybatisProperties properties) {
        final HikariDataSource dataSource = parentDatasource(properties);
        dataSource.setJdbcUrl(dbUrl_slave);
        dataSource.setUsername(username_slave);
        dataSource.setPassword(password_slave);
        return dataSource;
    }

    @Bean
    public DataSource masterDbDatasource() {
        final String masterUrl = properties.getMasterUrl();
        final String masterUsername = properties.getMasterUsername();
        final String masterPassword = properties.getMasterPassword();
        if (masterUrl == null || masterUrl.trim().equals("")) {
            throw new RuntimeException("Master database url can not be null");
        }
        if (masterUsername == null || masterUsername.trim().equals("")) {
            throw new RuntimeException("Master database username can not be null");
        }
        if (masterPassword == null || masterPassword.trim().equals("")) {
            throw new RuntimeException("Master database password can not be null");
        }
        return getDataSource(masterUrl, masterUsername, masterPassword, properties);
    }

    @Bean
    public DataSource slaveDbDatasource() {
        final String slaveUrl = properties.getSlaveUrl();
        final String slaveUsername = properties.getSlaveUsername();
        final String slavePassword = properties.getSlavePassword();
        if (slaveUrl == null || slaveUrl.trim().equals("")) {
            throw new RuntimeException("Slave database url can not be null");
        }
        if (slaveUsername == null || slaveUsername.trim().equals("")) {
            throw new RuntimeException("Slave database username can not be null");
        }
        if (slavePassword == null || slavePassword.trim().equals("")) {
            throw new RuntimeException("Slave database password can not be null");
        }
        return getDataSource(slaveUrl, slaveUsername, slavePassword, properties);
    }

    /**
     * 配置从库
     *
     * @return 从库SqlSession
     */
    @Bean
    public SqlSessionFactoryBean slaveSqlSessionFactoryBean() throws IOException {
        final SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(slaveDbDatasource());
        final ClassPathResource configLocationResource = new ClassPathResource(properties.getMapperConfig() == null ? MAPPER_CONFIG : properties.getMapperConfig());
        factoryBean.setConfigLocation(configLocationResource);
        PathMatchingResourcePatternResolver multiResolver_self = new PathMatchingResourcePatternResolver();
        Resource[] mapperLocationResource = multiResolver_self.getResources(properties.getMapperBasePackage() == null ? MAPPER_LOCATION : properties.getMapperBasePackage());
        factoryBean.setMapperLocations(mapperLocationResource);
        return factoryBean;
    }

    /**
     * 主库模板
     *
     * @return
     */
    @Bean
    public JdbcTemplate contentJdbcTemplate() {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(masterDbDatasource());
        return jdbcTemplate;
    }

    /**
     * 从库模板
     *
     * @return
     */
    @Bean
    public JdbcTemplate contentJdbcQueryTemplate() {
        final JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(slaveDbDatasource());
        return jdbcTemplate;
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        final DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(masterDbDatasource());
        return transactionManager;
    }

    /**
     * 配置主库
     *
     * @return 主库qlSession
     */
    @Bean
    public SqlSessionFactoryBean masterSqlSessionFactoryBean() throws IOException {
        final SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(masterDbDatasource());
        final ClassPathResource configLocationResource = new ClassPathResource(properties.getMapperConfig() == null ? MAPPER_CONFIG : properties.getMapperConfig());
        factoryBean.setConfigLocation(configLocationResource);
        PathMatchingResourcePatternResolver multiResolver_self = new PathMatchingResourcePatternResolver();
        Resource[] mapperLocationResource = multiResolver_self.getResources(properties.getMapperBasePackage() == null ? MAPPER_LOCATION : properties.getMapperBasePackage());
        factoryBean.setMapperLocations(mapperLocationResource);
        return factoryBean;
    }

    @Bean
    public SqlSessionTemplate masterSqlSession() throws Exception {
        return new SqlSessionTemplate(Objects.requireNonNull(masterSqlSessionFactoryBean().getObject()));
    }

    @Bean
    public SqlSessionTemplate slaveSqlSession() throws Exception {
        return new SqlSessionTemplate(Objects.requireNonNull(slaveSqlSessionFactoryBean().getObject()));
    }

    @Bean
    public CommonDaoImpl modelCommonDao() throws Exception {
        final CommonDaoImpl commonDao = new CommonDaoImpl();
        commonDao.setSqlSession(masterSqlSession());
        commonDao.setSqlSessionQurey(slaveSqlSession());
        return commonDao;
    }

    @Bean
    public CommonServiceImpl modelCommonService() throws Exception {
        final CommonServiceImpl service = new CommonServiceImpl();
        service.setCommonDao(modelCommonDao());
        return service;
    }

}
