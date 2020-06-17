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
import org.springframework.context.annotation.Primary;
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
@MapperScan(basePackages = {"com.jvxb.manage.livable.mapper*"}, sqlSessionTemplateRef = "manageSqlSessionTemplate")
public class ManageDbConfig {

    //	确定此数据源为master
    @Primary
    @Bean(name = "manageDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.manage")
    public DataSource getDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean(name = "manageSqlSessionFactory")
    public SqlSessionFactory getSqlSessionFactory(@Qualifier("manageDatasource") DataSource dataSource,
                                                  @Autowired Environment env) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = MyBatisPlusGlobalConfig.createSqlSessionFactory(env);
        sqlSessionFactory.setDataSource(dataSource);
		sqlSessionFactory.setTypeHandlersPackage("com.jvxb.livable.utils");
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("com/jvxb/manage/livable/mapper/xml/*Mapper.xml"));
        return sqlSessionFactory.getObject();
    }

    // 配置事务管理器
    @Bean(name = "manageTransactionManager")
    //	确定此数据源为master
    @Primary
    public DataSourceTransactionManager getTransactionManager(@Qualifier("manageDatasource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    //	确定此数据源为master
    @Primary
    @Bean(name = "manageSqlSessionTemplate")
    public SqlSessionTemplate getSqlSessionTemplate(
            @Qualifier("manageSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
