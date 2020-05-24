package com.jvxb.manage.utils;

import java.security.MessageDigest;
import java.util.Random;

import org.apache.commons.codec.binary.Hex;

/**
 * 系统密码校验工具
 *
 * @since 2019年1月2日
 */
public class PwdUtil {
	/**
	 * 加盐MD5
	 *
	 * @param password
	 * @return
	 */
	public static String generate(String password) {
		Random r = new Random();
		StringBuilder sb = new StringBuilder(16);
		sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
		int len = sb.length();
		if (len < 16) {
			for (int i = 0; i < 16 - len; i++) {
				sb.append("0");
			}
		}
		String salt = sb.toString();
		password = md5Hex(password + salt);
		char[] cs = new char[48];
		for (int i = 0; i < 48; i += 3) {
			cs[i] = password.charAt(i / 3 * 2);
			char c = salt.charAt(i / 3);
			cs[i + 1] = c;
			cs[i + 2] = password.charAt(i / 3 * 2 + 1);
		}
		return new String(cs);
	}

	/**
	 * 校验加盐后是否和原文一致
	 *
	 * @param password
	 * @param md5
	 * @return
	 */
	public static boolean verify(String password, String md5) {
		char[] cs1 = new char[32];
		char[] cs2 = new char[16];
		try {
			for (int i = 0; i < 48; i += 3) {
				cs1[i / 3 * 2] = md5.charAt(i);
				cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
				cs2[i / 3] = md5.charAt(i + 1);
			}
			String salt = new String(cs2);
			return md5Hex(password + salt).equals(new String(cs1));
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 获取十六进制字符串形式的MD5摘要
	 */
	private static String md5Hex(String src) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] bs = md5.digest(src.getBytes());
			return new String(new Hex().encode(bs));
		} catch (Exception e) {
			return null;
		}
	}

}
