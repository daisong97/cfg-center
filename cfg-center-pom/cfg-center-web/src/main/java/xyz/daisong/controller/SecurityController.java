package xyz.daisong.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xyz.daisong.constants.Constants;
import xyz.daisong.utils.ServletContext;
import xyz.daisong.utils.ServletContext.User;

@Controller
public class SecurityController {
	
	@RequestMapping("/loginPage")
	public String loginPage(){
		return "login";
	}
	@RequestMapping("/checkUser")
	public String checkUser(@RequestParam String username
			,@RequestParam String password,HttpServletRequest request,HttpServletResponse response){
		
		if("admin".equals(username) && "123".equals(password)){
			User currentUser = ServletContext.getCurrentUser();
			currentUser.setUsername(username);
			currentUser.setPassword(password);
			request.getSession().setAttribute(Constants.CURRENT_USER_SESSION, currentUser);
			return "redirect:home.htm";
		}else{
			return "login";
		}
	}
}
