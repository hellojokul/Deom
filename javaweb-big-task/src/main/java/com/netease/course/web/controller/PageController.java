package com.netease.course.web.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.netease.course.meta.Product;
import com.netease.course.meta.Statistics;
import com.netease.course.meta.User;
import com.netease.course.service.ProductService;
import com.netease.course.service.UserService;
import com.netease.course.util.CookieUtils;

@Controller
public class PageController {
	@Autowired
	private ProductService productService;
	@Autowired
	private UserService userService;
	
	private final String BUYER = "buyer";
	private final String SELLER = "seller";
	//调用RequestMapping前进行判断并封装数据
	@ModelAttribute
	public void userModel(Model model, HttpServletRequest req, HttpServletResponse resp) {
		Map<String, Object> reqParameters = new HashMap<String, Object>();
		User user = (User)req.getSession().getAttribute("user");
		if(user!=null) {
			model.addAttribute("user", user);
		}
		String type = req.getParameter("type");
		String id = req.getParameter("id");
		
		reqParameters.put("id", id);
		if(type!=null && type.equals("1")) {
			reqParameters.put("type", 1);
		} else {
			reqParameters.put("type", 0);
		}
		model.addAttribute("RequestParameters", reqParameters);
	}
	/*
	 * Synchronous Method
	 * */
	@RequestMapping("/")
	public String indexController(ModelMap root, HttpServletRequest req) {
		List<Product> productList = productService.getProducts();
		root.addAttribute("productList", productList);
		return "index";
	}
	
	@RequestMapping("/show")
	public ModelMap showController(ModelMap map, @RequestParam int id) {
		map.addAttribute("product", productService.showProduct(id));
		return map;
	}

	@RequestMapping("/settleAccount")
	public String settleAccount() {
		return "settleAccount";
	}

	@RequestMapping("/login")
	public String loginController() {
		return "login";
	}

	@RequestMapping("/logout")
	public String logoutController(HttpSession session) {
		session.removeAttribute("user");
		session.invalidate();
		return "forward:/login";
	}

	@RequestMapping("/edit")
	public ModelMap editController(ModelMap map, HttpServletRequest req) {
		Enumeration<String> names = req.getParameterNames();
		while(names.hasMoreElements()) {
			if(names.nextElement().equals("id")) {
				int id = Integer.valueOf(req.getParameter("id"));
				map.addAttribute("product", productService.showProduct(id));
			}
		}
		return map;
	}
	
	@RequestMapping("/public")
	public String publicController() {
		return "public";
	}
	
	@RequestMapping("/account")
	public ModelMap accoundController(ModelMap map, HttpSession session) {
		List<Product> buyList = productService.checkAccountProducts(BUYER,session);
		map.addAttribute("buyList", buyList);
		return map;
	}
	
	@RequestMapping("/editSubmit")
	public ModelMap editSubmitController(ModelMap map,
			@RequestParam String price, @RequestParam String title,
			@RequestParam String image, @RequestParam String summary,
			@RequestParam String detail, @RequestParam int id, HttpSession session) {
		Product product = productService.checkUpdateProduct(price, title, image, summary, detail, id, SELLER, session);
		map.addAttribute("product", product);
		return map;
	}
	@RequestMapping("/publicSubmit")
	public ModelMap publicSubmitController(ModelMap map,
			@RequestParam String price, @RequestParam String title,
			@RequestParam String summary, @RequestParam String image,
			@RequestParam String detail, HttpSession session) {
		Product product = productService.checkAddProduct(price, title, image,summary, detail, SELLER, session);
		map.addAttribute("product", product);
		return map;
	}
	/*
	 * Asynchronous Method
	 * */
	@RequestMapping(path="/api/login",method=RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> userController(
			HttpSession session,
			HttpServletResponse resp,
			@RequestParam String userName,
			@RequestParam String password) {
		User loginUser = userService.getUser(userName, password);
		if(loginUser!=null) {
			session.setAttribute("user", loginUser);
			return analysisEntity(200, "登陆成功", "1");
		} else {
			return analysisEntity(400, "用户名或密码错误", "0");
		}
	}

	@RequestMapping(path="/api/buy", method=RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> buyerControler(
			RequestEntity<List<Statistics>> RequestEntiry, HttpSession session, 
			HttpServletRequest req, HttpServletResponse resp) {
		List<Statistics> statis = RequestEntiry.getBody();
		String status = productService.checkBuyProduct(statis, (User)session.getAttribute("user"), BUYER, session);
		if(status!=null) {
			return analysisEntity(200, "购买成功", "1");
		} else {
			Cookie cookie = CookieUtils.createCookie("products", "/", 0, "");
			resp.addCookie(cookie);
			return analysisEntity(400, "购买失败", "0");
		}
	}

	@RequestMapping(path="/api/delete", method=RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> deleteController(
			@RequestParam int id, HttpSession session) {
		if(productService.checkDeleteProduct(id,SELLER,session)!=null) {
			return analysisEntity(200, "删除成功", "1");
		} else {
			return analysisEntity(400, "删除失败", "0");
		}
	}

	@RequestMapping(path="/api/upload", method=RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> uploadFileControler(
			@RequestParam("file") MultipartFile mpf, HttpSession session) {
		String url = productService.checkStorePicture(mpf,SELLER,session);
		if(url!=null) {
			return analysisEntity(200, "上传成功", url);
		} else {
			return analysisEntity(400, "上传失败", url);
		}
	}
	//封装ResponseEntity并返回
	private ResponseEntity<Map<String,Object>> analysisEntity(int code, String message, String result) {
		Map<String,Object> jsonMap = new HashMap<String,Object>();
		jsonMap.put("code", code);
		jsonMap.put("message", message);
		if(result==null) {
			jsonMap.put("result", result);
			return new ResponseEntity<Map<String,Object>>(jsonMap,HttpStatus.BAD_REQUEST);
		} else if(result.equals("1")) {
			jsonMap.put("result", true);
			return new ResponseEntity<Map<String,Object>>(jsonMap,HttpStatus.OK);
		} else if(result.equals("0")){
			jsonMap.put("result", false);
			return new ResponseEntity<Map<String,Object>>(jsonMap,HttpStatus.BAD_REQUEST);
		} else {
			jsonMap.put("result", result);
			return new ResponseEntity<Map<String,Object>>(jsonMap,HttpStatus.OK);
		}
	}
}


