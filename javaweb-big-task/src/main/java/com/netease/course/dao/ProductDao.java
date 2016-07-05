package com.netease.course.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import com.netease.course.meta.Product;

public interface ProductDao {
	
	public List<Product> getProducts();
	
	public Product findProductByMaxId();
	
	public Product findProductById(int id);
	
	public List<Product> getBuyProducts();
	
	@Select("SELECT count(id) FROM content")
	public String productTotal();
	
	@Delete("DELETE FROM content WHERE id=#{0}")
	public void deleteProductById(int id);
	
	public void updateProduct(String price,String title,String image,String summary,String detail,int id);
	
	public void addProduct(String price,String title,String image,String summary,String detail);
}
