package com.netease.course.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class ValidateFormUtils {
	
	public static boolean validPrice(String price) {
		String[] nums = price.split("\\.");
		if(nums[0].length()>7) {
			return false;
		}
		return true;
	}
	
	public static boolean validTitle(String title) {
		if(title.length()<2 || title.length()>=80) {
			return false;
		}
		return true;
	}
	
	public static boolean validDetail(String detail) {
		if(detail.length()<2 || detail.length()>=1000) {
			return false;
		}
		return true;
	}
	
	public static boolean validSummary(String summary) {
		if(summary.length()<2 || summary.length()>=140) {
			return false;
		}
		return true;
	}
	
	public static boolean validImage(String image) {
		try {
			if(image.matches("[a-zA-z]+://[^\\s]*")) {
				URL url = new URL(image);
				URLConnection conn = url.openConnection();
				if(conn.getContentLength()<1048576) {
					return true;
				}
			}
			return false;
		} catch (IOException e) {
			return false;
		}
	}
	
	public static boolean validForm(String price, String title, 
			String image, String summary, String detail) {
		if(validPrice(price) && validTitle(title) 
				&& validImage(image) && validSummary(summary) && validDetail(detail)) {
			return true;
		}
		return false;
	}
}
