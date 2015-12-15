package xyz.daisong.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService {
	@Value("${jdbc.url}")
	private String url;
	
	@Value("${pname}")
	private String username = "";
	
	@Value("${commons2}")
	private String commons2 ="";
	
	
	public void print(){
		System.out.println("url:" + url + "    -->pname:" + username + " --->commons2:" + commons2);
	}
}
