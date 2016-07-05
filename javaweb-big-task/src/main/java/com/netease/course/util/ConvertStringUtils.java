package com.netease.course.util;

public class ConvertStringUtils {
	
	public static String removePoint(String sNum) {
		String result = "";
		String[] nums = sNum.split("\\.");
		if(nums[0].charAt(0)!='0') {
			result += nums[0];
		}
		if(nums.length==1) {
			result += "00";
		} else {
			int length = nums[1].length();
			if(nums[1].charAt(0)=='0' && length==1) {
				result += "00";
			} else if(nums[1].charAt(0)!='0' && length==1) {
				result += nums[1].charAt(0)+"0";
			} else {
				result += nums[1].charAt(0);
				result += nums[1].charAt(1);
			}
		}
		return result;
	}
	public static String addPoint(String sNum) {
		String result = "";
		if(sNum.length()==1) {
			result += "0.0"+sNum;
		} else if(sNum.length()==2) {
			result += "0."+sNum;
		} else {
			int length = sNum.length();
			char[] nums = new char[length];
			sNum.getChars(0, length, nums, 0);
			for(int i=0; i<nums.length-2; i++) {
				result += nums[i];
			}
			result += "."+nums[nums.length-2];
			result += nums[nums.length-1];
		}
		return result;
	}
}
