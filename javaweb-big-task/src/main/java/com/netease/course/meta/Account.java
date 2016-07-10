package com.netease.course.meta;

public class Account {
	private int contentId;
	private int personId;
	private String price;
	private int number;
	private long time;
	
	public Account() {
	}
	public Account(int contentId, int personId, String price, int number, long time) {
		this.contentId = contentId;
		this.personId = personId;
		this.price = price;
		this.number = number;
		this.time = time;
	}
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getContentId() {
		return contentId;
	}
	public void setContentId(int contentId) {
		this.contentId = contentId;
	}
	public int getPersonId() {
		return personId;
	}
	public void setPersonId(int personId) {
		this.personId = personId;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
}
