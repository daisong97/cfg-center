package xyz.daisong.zookeeper.exceptions;

/**
 * zk连接异常类
 * @author dais
 *
 */
public class ZkConnectException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9191190531714785996L;

	
	public ZkConnectException() {
		super();
	}

	public ZkConnectException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ZkConnectException(String message, Throwable cause) {
		super(message, cause);
	}

	public ZkConnectException(String message) {
		super(message);
	}

	public ZkConnectException(Throwable cause) {
		super(cause);
	}
	
}
