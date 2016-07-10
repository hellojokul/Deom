package com.netease.course.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

import com.netease.course.meta.Product;
import com.netease.course.meta.Statistics;
import com.netease.course.meta.User;

public interface ProductService {
	
	public List<Product> getProducts();
	
	public Product showProduct(int id);
	
	public List<Product> checkAccountProducts(String type, HttpSession session);
	
	public String checkBuyProduct(List<Statistics> products, User user, String type, HttpSession session);
	
	public Product checkAddProduct(String price,String title,String image,String summary,String detail, String type, HttpSession session);
	
	public Product checkUpdateProduct(String price,String title,String image,String summary,String detail,int id, String type, HttpSession session);
	
	public String checkDeleteProduct(int id, String type, HttpSession session);
	
	public String checkStorePicture(MultipartFile mpf, String type, HttpSession session);
}
