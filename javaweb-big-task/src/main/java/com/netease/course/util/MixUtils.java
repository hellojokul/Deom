package com.netease.course.util;

import java.security.MessageDigest;

import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
public class MixUtils {
	
	public static boolean checkFileType(String fileType, byte[] bytes) {
		
		if((fileType.toLowerCase().equals("jpg") || fileType.toLowerCase().equals("gif")
				|| fileType.toLowerCase().equals("bmp") || fileType.toLowerCase().equals("jpeg")
				|| fileType.toLowerCase().equals("tiff") || fileType.toLowerCase().equals("jpe")
				|| fileType.toLowerCase().equals("png")) && bytes.length<=1048576) {
			return true;
		}
		return false;
	}
	
	public static String getMD5(String value) {
		try {
			MessageDigest md = MessageDigest.getInstance("md5");
			byte[] bytes = md.digest(value.getBytes());
			BASE64Encoder base = new BASE64Encoder();
			return base.encode(bytes);
		} catch (Exception e) {
			return null;
		}
	}
}
