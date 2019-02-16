package cn.aegisa.spring.boot.mybatis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Using IntelliJ IDEA.
 *
 * @author XIANYINGDA at 9/10/2018 2:26 PM
 */
@ConfigurationProperties(prefix = "spring.mybatis")
@Data
public class MybatisProperties {
    private String driverClassName;
    private Integer initialSize;
    private Integer minIdle;
    private Integer maxActive;
    private Long maxWait;
    private Long timeBetweenEvictionRunsMillis;
    private Long minEvictableIdleTimeMillis;
    private String validationQuery;
    private Boolean testWhileIdle;
    private Boolean testOnBorrow;
    private Boolean testOnReturn;
    private String masterUrl;
    private String masterUsername;
    private String masterPassword;
    private String slaveUrl;
    private String slaveUsername;
    private String slavePassword;

    private String mapperBasePackage;
    private String mapperConfig;
}
