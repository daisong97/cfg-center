package xyz.daisong.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import xyz.daisong.test.UserService;

public class MainClass {
	public static void main(String[] args) {
		ApplicationContext context =
			    new ClassPathXmlApplicationContext(new String[] {"spring-app.xml"});
		UserService bean = context.getBean(UserService.class);
		bean.print();
		
	}
}
