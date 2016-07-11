package com.netease.course.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.netease.course.meta.Product;
import com.netease.course.meta.Statistics;
import com.netease.course.meta.User;

public interface ProductService {
	
	public List<Product> getProducts();
	
	public Product showProduct(int id);
	
	public List<Product> accountProducts();
	
	public String buyProduct(List<Statistics> products, User user);
	
	public Product addProduct(String price,String title,String image,String summary,String detail);
	
	public Product updateProduct(String price,String title,String image,String summary,String detail,int id);
	
	public String deleteProduct(int id);
	
	public String storePicture(MultipartFile mpf);
}
