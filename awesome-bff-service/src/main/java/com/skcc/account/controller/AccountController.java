package com.skcc.account.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skcc.account.domain.Account;
import com.skcc.account.service.AccountService;

@RestController
@RequestMapping("/v1")
public class AccountController {

	private AccountService accountService;
	
	private static final Logger log = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}
	
	@PostMapping("/login")
	public Account login(HttpServletRequest request
					, @RequestBody Account account) throws Exception {
		
		HttpSession session = request.getSession();

		if (session.getAttribute("username") != null) {
			session.removeAttribute("id");
			session.removeAttribute("username");
			session.removeAttribute("name");
			session.removeAttribute("mobile");
			session.removeAttribute("address");
			session.removeAttribute("scope");
		}

		Account resultAccount = this.accountService.login(account);
		
		if(resultAccount.getUsername() != null) {
			session.setAttribute("id", resultAccount.getId());
			session.setAttribute("username", resultAccount.getUsername());
			session.setAttribute("name", resultAccount.getName());
			session.setAttribute("mobile", resultAccount.getMobile());
			session.setAttribute("address", resultAccount.getAddress());
			session.setAttribute("scope", resultAccount.getScope());
		} else {
			throw new Exception();
		}
		
		return resultAccount;
	}
	
	@PostMapping("/accounts")
	public boolean createAccount(HttpServletRequest request, @RequestBody Account account) throws Exception {
		HttpSession session = request.getSession();

		if (session.getAttribute("username") != null) {
			session.removeAttribute("id");
			session.removeAttribute("username");
			session.removeAttribute("name");
			session.removeAttribute("mobile");
			session.removeAttribute("address");
			session.removeAttribute("scope");
		}
		
		if(!this.accountService.createAccount(account)) {
			throw new Exception();
		}
		
		return true;
	}
	
}
