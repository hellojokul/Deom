package com.netease.course.util;


public class MixUtils {
	
	public static boolean checkFileType(String fileName, byte[] bytes) {
		String[] names = fileName.split("\\.");
		
		if((names[1].toLowerCase().equals("jpg") || names[1].toLowerCase().equals("gif")
				|| names[1].toLowerCase().equals("bmp") || names[1].toLowerCase().equals("jpeg")
				|| names[1].toLowerCase().equals("tiff") || names[1].toLowerCase().equals("jpe")
				|| names[1].toLowerCase().equals("png")) && bytes.length<=1048576) {
			return true;
		}
		return false;
	}
}
