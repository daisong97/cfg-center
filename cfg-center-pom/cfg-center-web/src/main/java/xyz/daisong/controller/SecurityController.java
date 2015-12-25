package xyz.daisong.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import xyz.daisong.constants.Constants;
import xyz.daisong.service.ZKConnectService;
import xyz.daisong.utils.ServletContext;
import xyz.daisong.zookeeper.exceptions.ZkConnectException;

@Controller
public class SecurityController {
	/**给每一个连接的用户一个连接，方便以后zk权限控制*/
	@Autowired
	private ZKConnectService zkconnectService;
	
	@RequestMapping("/loginPage")
	public String loginPage(){
		return "login";
	}
	@RequestMapping("/checkUser")
	public String checkUser(@RequestParam String username
			,@RequestParam String password,HttpServletRequest request,HttpServletResponse response) 
					throws ZkConnectException{
		
		if("admin".equals(username) && "123".equals(password)){
		    ServletContext context = ServletContext.getContext();
		    context.setUsername(username);
		    context.setPassword(password);
		    context.setRequest(request);
		    context.setZkClient(zkconnectService.connect());
			request.getSession().setAttribute(Constants.CURRENT_USER_SESSION, context);
			ServletContext.setContext(context);
			return "redirect:home.htm";
		}else{
			return "login";
		}
	}
}
