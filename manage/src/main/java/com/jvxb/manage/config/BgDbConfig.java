package com.jvxb.manage.config;

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
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 配置mybatis数据源
 *
 * @author jvxb
 * @since 2020.03
 */
@Configuration
@MapperScan(basePackages = {"com.jvxb.manage.livable.mapper*"}, sqlSessionTemplateRef = "bgSqlSessionTemplate")
public class BgDbConfig {

    //	确定此数据源为master
    @Primary
    @Bean(name = "bgDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.bg")
    public DataSource getDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "bgSqlSessionFactory")
    public SqlSessionFactory getSqlSessionFactory(@Qualifier("bgDatasource") DataSource dataSource,
                                                  @Autowired Environment env) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = MyBatisPlusGlobalConfig.createSqlSessionFactory(env);
        sqlSessionFactory.setDataSource(dataSource);
		sqlSessionFactory.setTypeHandlersPackage("com.jvxb.livable.util");
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("com/jvxb/manage/livable/mapper/xml/*Mapper.xml"));
        return sqlSessionFactory.getObject();
    }

    // 配置事务管理器
    @Bean(name = "bgTransactionManager")
    //	确定此数据源为master
    @Primary
    public DataSourceTransactionManager getTransactionManager(@Qualifier("bgDatasource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    //	确定此数据源为master
    @Primary
    @Bean(name = "bgSqlSessionTemplate")
    public SqlSessionTemplate getSqlSessionTemplate(
            @Qualifier("bgSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
