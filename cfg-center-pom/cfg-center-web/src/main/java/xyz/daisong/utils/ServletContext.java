package xyz.daisong.utils;

import javax.servlet.http.HttpServletRequest;

import xyz.daisong.zookeeper.ZkClient;

public class ServletContext {
	public static ThreadLocal<ServletContext> user_cache = new ThreadLocal<ServletContext>();
	
	private String username;
	private String password;
	private ZkClient zkClient;
	
	private HttpServletRequest request;
	
	public ZkClient getZkClient(){
		return zkClient;
	}
	
	public static ServletContext getContext(){
		return user_cache.get();
	}
	
	public static void setContext(ServletContext servletContext){
		user_cache.set(servletContext);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setZkClient(ZkClient zkClient) {
		this.zkClient = zkClient;
	}
}
