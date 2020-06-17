package com.jvxb.manage.configuration.config.db;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 配置mybatis数据源
 *
 * @author lcl
 * @since 2019-09-10
 */
@Configuration
@MapperScan(basePackages = {"com.jvxb.manage.remote.mapper*"}, sqlSessionTemplateRef = "beautySqlSessionTemplate")
public class BeautyDbConfig {

    @Bean(name = "beautyDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.beauty")
    public DataSource getDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "beautySqlSessionFactory")
    public SqlSessionFactory getSqlSessionFactory(@Qualifier("beautyDatasource") DataSource dataSource,
                                                  @Autowired Environment env) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = MyBatisPlusGlobalConfig.createSqlSessionFactory(env);
        sqlSessionFactory.setDataSource(dataSource);
		sqlSessionFactory.setTypeHandlersPackage("com.jvxb.livable.utils");
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("com/jvxb/manage/remote/mapper/xml/*Mapper.xml"));
        return sqlSessionFactory.getObject();
    }

    // 配置事务管理器
    @Bean(name = "beautyTransactionManager")
    public DataSourceTransactionManager getTransactionManager(@Qualifier("beautyDatasource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "beautySqlSessionTemplate")
    public SqlSessionTemplate getSqlSessionTemplate(
            @Qualifier("beautySqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
