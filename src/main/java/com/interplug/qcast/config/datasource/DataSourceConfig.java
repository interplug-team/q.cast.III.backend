package com.interplug.qcast.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

/** data source config */
@Configuration
@MapperScan(basePackages = {"com.interplug.qcast"})
public class DataSourceConfig {

  protected static final String MASTER = "masterDataSource";
  protected static final String READ = "readDataSource";

  /** masterDataSource config */
  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.master")
  public DataSource masterDataSource() {
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }

  /** readDataSource config */
  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.read")
  public DataSource readDataSource() {
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }

  /** routingDataSource config */
  @Bean
  public DataSource routingDataSource(
      @Qualifier(MASTER) DataSource master, @Qualifier(READ) DataSource slave) {
    Map<Object, Object> dataSources = new HashMap<>();
    dataSources.put(MASTER, master);
    dataSources.put(READ, slave);

    RoutingDataSource routingDataSource = new RoutingDataSource();
    routingDataSource.setTargetDataSources(dataSources);
    routingDataSource.setDefaultTargetDataSource(master);

    return routingDataSource;
  }

  /** lazyDataSource config */
  @Bean
  @DependsOn({"routingDataSource"})
  public LazyConnectionDataSourceProxy lazyDataSource(
      @Qualifier("routingDataSource") DataSource dataSource) {
    return new LazyConnectionDataSourceProxy(dataSource);
  }

  /** transactionManager config */
  @Bean
  public PlatformTransactionManager transactionManager(
      @Qualifier("lazyDataSource") DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }

  /** sqlSessionFactory config */
  @Bean
  @SuppressWarnings("PMD.SignatureDeclareThrowsException")
  public SqlSessionFactory sqlSessionFactory(LazyConnectionDataSourceProxy dataSource)
      throws Exception {
    final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    sessionFactory.setDataSource(dataSource);
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    sessionFactory.setMapperLocations(resolver.getResources("mappers/**/*Mapper.xml"));
    sessionFactory.setConfigLocation(resolver.getResource("config/mybatis-config.xml"));

    return sessionFactory.getObject();
  }

  /** sqlSessionTemplate config */
  @Bean
  public SqlSessionTemplate sqlSessionTemplate(
      @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
    return new SqlSessionTemplate(sqlSessionFactory);
  }
}
