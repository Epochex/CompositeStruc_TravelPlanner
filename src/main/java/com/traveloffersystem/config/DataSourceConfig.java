//package com.traveloffersystem.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import com.zaxxer.hikari.HikariDataSource;
//
//import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
//import org.springframework.core.io.ClassPathResource;
//
//import javax.sql.DataSource;
//
///**
// * Spring 3 兼容的 DataSource 配置
// */
//@Configuration
//public class DataSourceConfig {
//
//    @Bean
//    public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
//        PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
//        configurer.setLocation(new ClassPathResource("jdbc.properties")); // 配置文件名称
//        return configurer;
//    }
//
//    @Bean
//    public DataSource dataSource() {
//        HikariDataSource ds = new HikariDataSource();
//        ds.setDriverClassName("${spring.datasource.driver-class-name}");
//        ds.setJdbcUrl("${spring.datasource.url}");
//        ds.setUsername("${spring.datasource.username}");
//        ds.setPassword("${spring.datasource.password}");
//        ds.setMaximumPoolSize(Integer.parseInt("${spring.datasource.hikari.maximum-pool-size:5}"));
//        return ds;
//    }
//}
