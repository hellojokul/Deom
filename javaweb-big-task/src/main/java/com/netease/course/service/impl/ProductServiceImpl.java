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
	@Value("${urlPath}")
	private String relativeUrl;
	
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
	public List<Product> getAccountProducts() {
		return productDao.getBuyProducts();
	}

	@Override
	public Product addFindProduct(String price,String title,String image,String summary,String detail) {
		int total = Integer.valueOf(productDao.productTotal());
		if(total<1000 && ValidateFormUtils.validForm(price, title, image, summary, detail)) {
			productDao.addProduct(price, title, image, summary, detail);
			return productDao.findProductByMaxId();
		}
		return null;
	}

	@Override
	public Product updateProduct(String price,String title,String image,String summary,String detail,int id) {
		if(ValidateFormUtils.validForm(price, title, image, summary, detail)) {
			productDao.updateProduct(price, title, image, summary, detail, id);
			return productDao.findProductById(id);
		}
		return null;
	}

	@Override
	public boolean deleteProduct(int id) {
		Product product = showProduct(id);
		if(product==null || product.getIsBuy()){
			return false;
		} else {
			productDao.deleteProductById(id);
			return true;
		}
	}
	
	@Override
	public void buyProduct(List<Statistics> statis, User user) {
		Map<String,List<Account>> map = new HashMap<String,List<Account>>();
		Iterator<Statistics> iter = statis.iterator();
		while(iter.hasNext()) {
			Statistics st = iter.next();
			Product p = productDao.findProductById(st.getId());
			int number = st.getNumber();
			if(p.getPrice()!=null && !p.getIsBuy() && number>0) {
				List<Account> accountList = new ArrayList<Account>();
				Account account = new Account(p.getId(),user.getId(),p.getPrice(),System.currentTimeMillis());
				for(int i=0; i<number; i++) {
					accountList.add(account);
				}
				map.put("accountList", accountList);
				accountDao.addRecord(map);
				map.clear();
			}
		}
	}
	//保存图片到本地的image文件下并返回该图片的URL地址
	@Override
	public String storePicture(MultipartFile mpf) {
		String fileName = mpf.getOriginalFilename();
		BufferedOutputStream out = null;
		File folder = new File(System.getProperty("user.dir")+folderPath);
		if(!folder.isDirectory()) {
			folder.mkdir();
		}
		String reallyPath = folder.getPath()+"/"+fileName;
		String reallyUrl = relativeUrl+fileName;
		try {
			byte[] bytes = mpf.getBytes();
			if(!MixUtils.checkFileType(fileName, bytes)) {
				return null;
			}
			out = new BufferedOutputStream(
					new FileOutputStream(new File(reallyPath)));
			out.flush();
			out.write(bytes);
		} catch (IOException e) {
			return null;
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




