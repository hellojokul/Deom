package com.netease.course.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

@Controller
public class PageController {
	@Autowired
	private ProductService productService;
	@Autowired
	private UserService userService;
	/*
	 * Synchronous Method
	 * */
	@ModelAttribute   //存储页面所需数据
	public void userModel(Model model, HttpServletRequest req) {
		Map<String, Object> reqParameters = new HashMap<String, Object>();
		Cookie cookie = getCookie(req);
		if(cookie!=null) {
			model.addAttribute("user", userService.getUser(cookie.getValue()));
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
	//找出需要的Cookie
	private Cookie getCookie(HttpServletRequest req) {
		Cookie[] cookies = req.getCookies();
		if(cookies!=null) {
			for(Cookie cookie:cookies) {
				if(cookie.getName().equals("CURRENT_USER")) {
					return cookie;
				}
			}
		}
		return null;
	}
	//检索Cookie
	private boolean checkCookie(HttpServletRequest req, int type) {
		Cookie cookie = getCookie(req);
		if(cookie!=null && cookie.getValue().equals("buyer") && type==0) {
			return true;
		} else if(cookie!=null && cookie.getValue().equals("seller") && type==1) {
			return true;
		}
		return false;
	}
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
	
	@RequestMapping("/account")
	public ModelMap accoundController(ModelMap map, HttpServletRequest req) {
		if(checkCookie(req,0)) {
			List<Product> buyList = productService.getAccountProducts();
			map.addAttribute("buyList", buyList);
		}
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
	public String logoutController(HttpServletRequest req, HttpServletResponse resp) {
		Cookie cookie = getCookie(req);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		resp.addCookie(cookie);
		return "forward:/login";
	}
	
	@RequestMapping("/edit")
	public ModelMap editController(ModelMap map, HttpServletRequest req) {
		if(checkCookie(req,1)) {
			int id = Integer.valueOf(req.getParameter("id"));
			map.addAttribute("product", productService.showProduct(id));
		}
		return map;
	}
	
	@RequestMapping("/editSubmit")
	public ModelMap editSubmitController(ModelMap map,
			@RequestParam String price, @RequestParam String title,
			@RequestParam String image, @RequestParam String summary,
			@RequestParam String detail, @RequestParam int id, HttpServletRequest req) {
		if(checkCookie(req,1)) {
			map.addAttribute("product", productService.
					updateProduct(price, title, image, summary, detail, id));
		}
		return map;
	}
	
	@RequestMapping("/public")
	public String publicController() {
		return "public";
	}

	@RequestMapping("/publicSubmit")
	public ModelMap publicSubmitController(ModelMap map,
			@RequestParam String price, @RequestParam String title,
			@RequestParam String summary, @RequestParam String image,
			@RequestParam String detail,HttpServletRequest req) {
		if(checkCookie(req,1)) {
			map.addAttribute("product", productService.addFindProduct(
					price, title, image,summary, detail));
		}
		return map;
	}
	/*
	 * Asynchronous Method
	 * */
	@RequestMapping(path="/api/login",method=RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> userController(
			HttpServletResponse resp,
			@RequestParam String userName,
			@RequestParam String password) {
		User loginUser = userService.getUser(userName, password);
		if(loginUser!=null) {
			Cookie cookie = new Cookie("CURRENT_USER", loginUser.getNickName());
			cookie.setPath("/");
			resp.addCookie(cookie);
			return analysisData(200, "登陆成功", "1");
		} else {
			return analysisData(400, "用户名或密码错误", "0");
		}
	}

	@RequestMapping(path="/api/delete", method=RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> deleteController(
			@RequestParam int id, HttpServletRequest req) {
		if(productService.deleteProduct(id) && checkCookie(req,1)) {
			return analysisData(200, "删除成功", "1");
		} else {
			return analysisData(400, "删除失败", "0");
		}
	}

	@RequestMapping(path="/api/buy", method=RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> buyerControler(
			RequestEntity<List<Statistics>> RequestEntiry, HttpServletRequest req) {
		List<Statistics> statis = RequestEntiry.getBody();
		if(checkCookie(req,0)) {
			productService.buyProduct(statis, userService.getUser(getCookie(req).getValue()));
			return analysisData(200, "购买成功", "1");
		} else {
			return analysisData(200, "购买失败", "0");
		}
	}
	
	@RequestMapping(path="/api/upload", method=RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> uploadFileControler(
			@RequestParam("file") MultipartFile mpf, HttpServletRequest req) {
		String url = productService.storePicture(mpf,req);
		if(checkCookie(req,1) && url!=null) {
			return analysisData(200, "上传成功", url);
		} else {
			return analysisData(400, "上传失败", url);
		}
	}
	//封装ResponseEntity并返回
	private ResponseEntity<Map<String,Object>> analysisData(int code, String message, String result) {
		Map<String,Object> jsonMap = new HashMap<String,Object>();
		jsonMap.put("code", code);
		jsonMap.put("message", message);
		if(result.isEmpty()) {
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


