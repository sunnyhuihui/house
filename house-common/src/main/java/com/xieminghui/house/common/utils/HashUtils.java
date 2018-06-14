package com.xieminghui.house.common.utils;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;

public class HashUtils {
	
	private static final HashFunction FUNCTION = Hashing.md5();
	
	private static final String SALT = "xmh";
	//加盐的作用，就是让用户的密码 与上面的字符串同时进行md5加密，这样防止暴力破解


	public static String encryPassword(String password){
	   HashCode hashCode =	FUNCTION.hashString(password+SALT, Charset.forName("UTF-8"));
	   return hashCode.toString();
	}

}
