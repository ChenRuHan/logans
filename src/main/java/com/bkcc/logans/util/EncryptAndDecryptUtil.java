package com.bkcc.logans.util;

/**
 * 【描 述】：自定义加解密算法
 * 【环 境】：J2SE 1.8
 *
 * @author 陈汝晗
 * @version v1.0 Jun 10, 2019 新建
 * @since Jun 10, 2019
 */
public class EncryptAndDecryptUtil {
	
	private static final String PASSWORD = "!+bkcc2018";

	/**
	 * 【描 述】：加密算法
	 *
	 * @param sourceString
	 * @return
	 * @since Jun 10, 2019
	 */
	public static String encrypt(String sourceString) {
		char[] p = PASSWORD.toCharArray(); // 字符串转字符数组
		int n = p.length; // 密码长度
		char[] c = sourceString.toCharArray();
		int m = c.length; // 字符串长度
		for (int k = 0; k < m; k++) {
			int mima = c[k] + p[k / n]; // 加密
			c[k] = (char) mima;
		}
		return BASE64Utils.encode(new String(c));
	}

	/**
	 * 【描 述】：解密
	 *
	 * @param sourceString
	 * @return
	 * @since Jun 10, 2019
	 */
	public static String decrypt(String sourceString) {
		sourceString = BASE64Utils.decode(sourceString);
		char[] p = PASSWORD.toCharArray(); // 字符串转字符数组
		int n = p.length; // 密码长度
		char[] c = sourceString.toCharArray();
		int m = c.length; // 字符串长度
		for (int k = 0; k < m; k++) {
			int mima = c[k] - p[k / n]; // 解密
			c[k] = (char) mima;
		}
		return new String(c);
	}
	
	private EncryptAndDecryptUtil() {}

}///:~
