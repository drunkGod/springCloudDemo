package com.jvxb.modules.configuration.security;


/**
 * 安全工具类
 *
 */
public class SecurityUtil {

	private static final ThreadLocal<UserDetail> userHolder = new InheritableThreadLocal<>();
	private static final ThreadLocal<String> tokenHolder = new InheritableThreadLocal<>();

	private static TokenProvider tp;

	public static void setTokenProvider(TokenProvider tp) {
		SecurityUtil.tp = tp;
	}

	/**
	 * 获取当前登陆用户信息，需要的时候才查询redis
	 *
	 * @return
	 */
	public static UserDetail currentUser() {
		if (userHolder.get() != null) {
			return (UserDetail) userHolder.get();
		}
		if (tokenHolder.get() != null) {
			UserDetail detail = (UserDetail) tp.getUserDetail(tokenHolder.get());
			setCurrentUser(detail);
			return detail;
		}

		return null;
	}

	/**
	 * 获取当前登陆用户ID
	 *
	 * @return
	 */
	public static Integer currentUserId() {
		UserDetail user = currentUser();
		if (user != null) {
			return user.getId();
		}
		return null;
	}

	/**
	 * 设置当前登陆token
	 *
	 * @return
	 */
	public static void setCurrentToken(String token) {
		tokenHolder.remove();
		userHolder.remove();
		if (token != null) {
			tokenHolder.set(token);
		}
	}

	/**
	 * 设置当前登陆用户
	 *
	 * @param userinfo
	 * @return
	 */
	public static void setCurrentUser(UserDetail userinfo) {
		userHolder.remove();
		if (userinfo != null) {
			userHolder.set(userinfo);
		}
	}
}
