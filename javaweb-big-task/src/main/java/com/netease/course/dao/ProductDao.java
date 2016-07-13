package com.netease.course.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.netease.course.meta.Product;

public interface ProductDao {
	
	public List<Product> getProducts();
	
	public Product findNewProduct();
	
	public Product findProductById(int id);
	
	public List<Product> getBuyProducts();
	
	@Select("select count(id) from content")
	public String productTotal();
	
	@Delete("delete from content where id=#{0}")
	public boolean deleteProductById(int id);
	
	public boolean updateProduct(String price,String title,String image,String summary,String detail,int id);
	
	public boolean addProduct(String price,String title,String image,String summary,String detail);
}
