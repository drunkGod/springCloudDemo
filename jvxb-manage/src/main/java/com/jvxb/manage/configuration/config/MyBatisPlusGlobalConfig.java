package com.jvxb.manage.configuration.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.config.GlobalConfig.DbConfig;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.type.JdbcType;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

/**
 * mybatis plus公共配置
 *
 * @author lcl
 * @since 2019-09-10
 */
public class MyBatisPlusGlobalConfig {

	public static final DbType dbType = DbType.MYSQL;

	/**
	 * 公共配置
	 *
	 * @param env
	 * @return
	 */
	public static MybatisSqlSessionFactoryBean createSqlSessionFactory(Environment env) {
		MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
		sqlSessionFactory.setVfs(SpringBootVFS.class);
		MybatisConfiguration configuration = new MybatisConfiguration();
		configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
		configuration.setJdbcTypeForNull(JdbcType.NULL);
		configuration.setMapUnderscoreToCamelCase(true);
		configuration.setCacheEnabled(false);
		sqlSessionFactory.setConfiguration(configuration);

		GlobalConfig conf = GlobalConfigUtils.defaults();
		DbConfig dbConfig = conf.getDbConfig();
		dbConfig.setDbType(dbType);
		dbConfig.setIdType(IdType.AUTO);

		List<Interceptor> plugins = new ArrayList<>();
		plugins.add(new PaginationInterceptor());

		// 生产环境
		if (env.getActiveProfiles() != null && env.getActiveProfiles().length > 0 && "prod".equals(env.getActiveProfiles()[0].trim())) {
			conf.setRefresh(false);
		} else {
			conf.setRefresh(false);
			plugins.add(new PerformanceInterceptor());
		}
		Interceptor[] integers = new Interceptor[plugins.size()];
		plugins.toArray(integers);
		sqlSessionFactory.setPlugins(integers);
		sqlSessionFactory.setGlobalConfig(conf);

		return sqlSessionFactory;
	}
}
