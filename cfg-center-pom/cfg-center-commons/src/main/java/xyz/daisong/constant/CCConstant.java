package xyz.daisong.constant;

public final class CCConstant {
	/**连接zk时间*/
	public static final long WAIT_CONECTION_ZK_TIME = 10*1000L;
	/**在线*/
	public static String IS_ONLINE_TRUE = "online";
	/**离线*/
	public static String IS_ONLINE_FALSE = "offline";
	/**系统基本路径*/
	public static String SYS_ROOT_PATH = "/cgf_center";
	/**引用地址,多个路径","分开*/
	public static String QUOTE_PATH = "QUOTE_PATH";
	
	public static String ZOOKEEPER_SCHEME_WORLD = "world";
	
	public static String ZOOKEEPER_SCHEME_AUTH  = "auth";
	
	public static String ZOOKEEPER_SCHEME_DIGEST = "digest";
	
	public static String ZOOKEEPER_SCHEME_IP = "ip";
}
