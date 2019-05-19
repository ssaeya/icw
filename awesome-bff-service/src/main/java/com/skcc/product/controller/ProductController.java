package com.skcc.product.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.skcc.product.domain.Product;
import com.skcc.product.service.ProductService;

@Controller
public class ProductController {

	private ProductService productService;
	
	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping("/product")
	public String index(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession();
		if(session.getAttribute("username") == null) {
			return "sign";
		}
		
		List<Product> productsList = this.productService.getAllProducts();
		model.addAttribute(productsList);
		
		return "product";
	}
}
