package com.skcc.home.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.skcc.account.domain.Account;
import com.skcc.accountbank.domain.AccountBank;
import com.skcc.accountbank.service.AccountBankService;
import com.skcc.cart.domain.Cart;
import com.skcc.cart.service.CartService;
import com.skcc.product.domain.Product;
import com.skcc.product.service.ProductService;

@Controller
public class HomeController {
	
	private ProductService productService;
	private CartService cartService;
	private AccountBankService accountBankService;
	
	@Autowired
	public HomeController(CartService cartService, ProductService productService, AccountBankService accountBankService) {
		this.productService = productService;
		this.cartService = cartService;
		this.accountBankService = accountBankService;
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		
		if (session.getAttribute("username") != null) {
			session.removeAttribute("id");
			session.removeAttribute("username");
			session.removeAttribute("name");
			session.removeAttribute("mobile");
			session.removeAttribute("address");
			session.removeAttribute("scope");
		}
		
		return "sign";
	}
	
	@GetMapping({"","/","/index","main"})
	public String index(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		if(session.getAttribute("username") == null) {
			return "sign";
		}
		
		List<Product> productsList = this.productService.getAllProducts();
		model.addAttribute(productsList);
		
		return "index";
	}
	
	@GetMapping("/about")
	public String about(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		if(session.getAttribute("username") == null) {
			return "sign";
		}
		
		return "about";
	}
	
	@GetMapping("/blog")
	public String blog(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		if(session.getAttribute("username") == null) {
			return "sign";
		}
		
		return "blog";
	}

	@GetMapping("/blog-detail")
	public String blogDetail(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		if(session.getAttribute("username") == null) {
			return "sign";
		}
		
		return "blog-detail";
	}
	
	@GetMapping("/contact")
	public String contact(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		if(session.getAttribute("username") == null) {
			return "sign";
		}
		
		return "contact";
	}
	
	@GetMapping("/shoping-cart")
	public String shopingCart(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		if(session.getAttribute("username") == null) {
			return "sign";
		}
		
		Account account = new Account();
		account.setId((long) session.getAttribute("id"));
		account.setUsername((String) session.getAttribute("username"));
		account.setName((String) session.getAttribute("name"));
		account.setMobile((String) session.getAttribute("mobile"));
		account.setScope((String) session.getAttribute("scope"));
		account.setAddress((String) session.getAttribute("address"));
		model.addAttribute(account);
		
		List<Cart> cartList = this.cartService.getCartsByAccountId((long) session.getAttribute("id"));
		model.addAttribute(cartList);
		
		AccountBank accountBank = this.accountBankService.findAccountBankByAccountId((long) session.getAttribute("id"));
		model.addAttribute(accountBank);
		
		return "shoping-cart";
	}
	
}
