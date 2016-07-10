package com.netease.course.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.netease.course.dao.AccountDao;
import com.netease.course.dao.ProductDao;
import com.netease.course.meta.Account;
import com.netease.course.meta.Product;
import com.netease.course.meta.Statistics;
import com.netease.course.meta.User;
import com.netease.course.service.ProductService;
import com.netease.course.util.MixUtils;
import com.netease.course.util.ValidateFormUtils;

@Transactional
@Component
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductDao productDao;
	@Autowired
	private AccountDao accountDao;
	@Value("${filePath}")
	private String folderPath;
	@Value("${fileUrl}")
	private String fileUrl;
	
	private final String SUCCESS = "success";
	
	@Override
	public Product showProduct(int id) {
		Product product = productDao.findProductById(id);
		return product;
	}
	
	@Override
	public List<Product> getProducts() {
		return productDao.getProducts();
	}

	@Override
	public List<Product> checkAccountProducts(String type, HttpSession session) {
		return productDao.getBuyProducts();
	}
	
	@Override
	public String checkBuyProduct(List<Statistics> statis, User user, String type, HttpSession session) {
		Map<String,List<Integer>> productMap = new HashMap<String,List<Integer>>();
		Map<String,List<Account>> accountMap = new HashMap<String,List<Account>>();
		List<Integer> ids = new ArrayList<Integer>();
		List<Integer> numbers = new ArrayList<Integer>();
		
		Iterator<Statistics> iter = statis.iterator();
		while(iter.hasNext()) {
			Statistics s = iter.next();
			ids.add(s.getId());
			numbers.add(s.getNumber());
		}
		productMap.put("productIds", ids);
		List<Product> products = productDao.getProductByIds(productMap);
		if(numbers.size()==products.size()) {
			List<Account> accountList = new ArrayList<Account>();
			for(int i=0; i<products.size(); i++) {
				Product p = products.get(i);
				int number = numbers.get(i);
				Account account = new Account(p.getId(),user.getId(),p.getPrice(),number,System.currentTimeMillis());
				accountList.add(account);
			}
			accountMap.put("accountList", accountList);
			accountDao.addRecord(accountMap);
			return SUCCESS;
		}
		return null;
	}
	
	
	@Override
	public Product checkAddProduct(String price,String title,String image,String summary,String detail,String type, HttpSession session) {
		int total = Integer.valueOf(productDao.productTotal());
		if(total<1000 && ValidateFormUtils.validForm(price, title, image, summary, detail)) {
			productDao.addProduct(price, title, image, summary, detail);
			return productDao.findProductByMaxId();
		}
		return null;
	}

	@Override
	public Product checkUpdateProduct(String price,String title,String image,String summary,String detail,int id,String type, HttpSession session) {
		if(ValidateFormUtils.validForm(price, title, image, summary, detail)) {
			productDao.updateProduct(price, title, image, summary, detail, id);
			return productDao.findProductById(id);
		}
		return null;
	}

	@Override
	public String checkDeleteProduct(int id, String type, HttpSession session) {
		Product product = showProduct(id);
		if(product!=null && !product.getIsBuy()){
			productDao.deleteProductById(id);
			return SUCCESS;
		} else {
			return null;
		}
	}

	//保存图片到本地的image文件下并返回该图片的URL地址
	@Override
	public String checkStorePicture(MultipartFile mpf, String type, HttpSession session) {
		String suffixName = mpf.getOriginalFilename().split("\\.")[1];
		String fileName = System.currentTimeMillis()+"."+suffixName;
		BufferedOutputStream out = null;
		File folder = new File(System.getProperty("user.dir")+folderPath);
		if(!folder.isDirectory()) {
			folder.mkdir();
		}
		String reallyPath = folder.getPath()+"/"+fileName;
		String reallyUrl = fileUrl+fileName;
		try {
			byte[] bytes = mpf.getBytes();
			if(!MixUtils.checkFileType(suffixName, bytes)) {
				return null;
			}
			out = new BufferedOutputStream(
					new FileOutputStream(new File(reallyPath)));
			out.flush();
			out.write(bytes);
		} catch (IOException e) {
			//ignore
		} finally {
			try {
				if(out!=null) {
					out.close();
				}
			} catch (IOException e) {
				//ignore
			}
		}
		return reallyUrl;
	}
}




