package com.jvxb.manage.configuration.config.security;


import com.jvxb.modules.configuration.security.RedisTokenProvider;
import com.jvxb.modules.configuration.security.SecurityUtil;
import com.jvxb.modules.configuration.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 使用redis的方式存储token
 *
 * @author lcl
 * @since 2019-09-10
 */
@Configuration
public class SecurityConfig {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Value("${server.token.timeoutInseconds:1800}")
	private long tokenValidityInseconds;

	@Value("${server.token.perfix:manage}")
	private String perfix;

	@Bean
	public TokenProvider tokenProvider() {
		return new RedisTokenProvider(redisTemplate, tokenValidityInseconds, perfix);
	}

	@Bean
	public Object bootstrap(@Autowired TokenProvider provider) {
		SecurityUtil.setTokenProvider(provider);
		return new Object();
	}

}
