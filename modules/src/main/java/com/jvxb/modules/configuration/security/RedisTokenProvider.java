package com.jvxb.modules.configuration.security;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;


/**
 * 使用redis的方式存储token
 *
 * @author lcl
 * @since 2019-09-10
 */
public class RedisTokenProvider implements TokenProvider {

	private RedisTemplate<String, Object> redisTemplate;

	private long tokenValidityInseconds;

	private String perfix;

	public RedisTokenProvider() {
		super();
	}

	public RedisTokenProvider(RedisTemplate<String, Object> redisTemplate, long tokenValidityInseconds, String perfix) {
		super();
		this.redisTemplate = redisTemplate;
		this.tokenValidityInseconds = tokenValidityInseconds;
		this.perfix = perfix;
	}

	@Override
	public String createToken(UserDetail detail) {
		String token = UUID.randomUUID().toString().replace("-", "");
		redisTemplate.opsForValue().set(createRedisTokenKey(token), detail, tokenValidityInseconds, TimeUnit.SECONDS);
		return token;
	}

	@Override
	public boolean checkToken(String token) {
		return redisTemplate.hasKey(createRedisTokenKey(token));
	}

	@Override
	public void refreshToken(String token) {
		redisTemplate.expire(createRedisTokenKey(token), tokenValidityInseconds, TimeUnit.SECONDS);
	}

	@Override
	public UserDetail getUserDetail(String token) {
		return (UserDetail) redisTemplate.opsForValue().get(createRedisTokenKey(token));
	}

	/**
	 * redis key格式
	 *
	 * @param token
	 * @return
	 */
	public String createRedisTokenKey(String token) {
		return SecurityConstant.AUTH_TOKEN_REDIS_KEY_PERFIX + ":" +(perfix + ":" + token);
	}

	@Override
	public void clearToken(String token) {
		redisTemplate.delete(createRedisTokenKey(token));
	}

	@Override
	public void updateUser(String token, UserDetail detail) {
		redisTemplate.opsForValue().set(createRedisTokenKey(token), detail, tokenValidityInseconds, TimeUnit.SECONDS);
	}
}
