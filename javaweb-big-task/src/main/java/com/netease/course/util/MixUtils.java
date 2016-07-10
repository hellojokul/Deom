package com.netease.course.util;

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
}
