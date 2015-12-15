package xyz.daisong.utils;

public final class ServletContext {
	public static ThreadLocal<User> user_cache = new ThreadLocal<User>(){
		protected User initialValue() {
			return new User();
		};
	};
	
	public static User getCurrentUser(){
		return user_cache.get();
	}
	
	public static class User{
		private String username;
		private String password;
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
	}
}
