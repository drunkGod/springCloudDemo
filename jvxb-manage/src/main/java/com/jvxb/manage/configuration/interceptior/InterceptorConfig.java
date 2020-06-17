package com.jvxb.manage.configuration.interceptior;


import com.jvxb.modules.configuration.security.SecurityInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * spring mvc 拦截器配置
 *
 * @author lcl
 * @since 2019-09-10
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

	@Bean
	public HandlerInterceptor requestInterceptor() {
		return new SecurityInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(requestInterceptor()).addPathPatterns("/**")
				.excludePathPatterns("/login")
				.excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**")
				;
	}

}
