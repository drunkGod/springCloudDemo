package com.jvxb.modules.configuration.security;

/**
 * 安全常量
 *
 * @author lcl
 * @since 2019-09-10
 */
public class SecurityConstant {

	/**
	 * 前端请求，token的字段
	 */
	public static final String AUTH_TOKEN = "auth-token";

	/**
	 * 平台保存用户信息的redis key 前缀
	 */
	public static final String AUTH_TOKEN_REDIS_KEY_PERFIX = "sys-auth-token";

	/**
	 * 平台保存URL权限的redis key 前缀
	 */
	public static final String AUTH_URL_REDIS_KEY_PERFIX = "sys-auth-url";

	/**
	 * 平台保存各系统登录用户id的redis key 前缀
	 */
	public static final String AUTH_USER_ID_REDIS_KEY_PERFIX = "sys-auth-user-id";

	/**
	 * 未授权错误码
	 */
	public static final Integer UNAUTHORIZED = 401;

	/**
	 * 账号冻结
	 */
	public static final Integer ACCOUNT_FREEZE = 4013;

	/**
	 * 账号或者密码错误
	 */
	public static final Integer ERROR_ACCOUNT_OR_PASSWORD = 4011;

	/**
	 * 单点登录用户安全等级不够
	 */
	public static final Integer SECURITY_LEVEL_ERROR = 4014;

	/**
	 * 单点登录用户类型不对
	 */
	public static final Integer USER_TYPE_ERROR = 4015;

	/**
	 * 单点登录用户在平台数据为空
	 */
	public static final Integer USER_DATA_EMPTY = 4016;

	/**
	 * 单点登录系统错误
	 */
	public static final Integer SSO_LOGIN_ERROR = 4019;

	/**
	 * 需要重新登录
	 */
	public static final Integer NEED_RELOGIN = 4020;

	/**
	 * 禁止外部访问，只能内部访问
	 */
	public static final String FORBIDDEN = "ROLE_INTERNAL";

}
