package com.netease.course.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.netease.course.meta.Product;
import com.netease.course.meta.Statistics;
import com.netease.course.meta.User;

public interface ProductService {
	
	public List<Product> getProducts();
	
	public List<Product> getAccountProducts();
	
	public Product showProduct(int id);
	
	public Product addFindProduct(String price,String title,String image,String summary,String detail);
	
	public boolean deleteProduct(int id);
	
	public Product updateProduct(String price,String title,String image,String summary,String detail,int id);
	
	public void buyProduct(List<Statistics> products, User user);
	
	public String storePicture(MultipartFile mpf,HttpServletRequest req);
}
