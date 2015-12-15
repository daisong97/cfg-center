package xyz.daisong.zookeeper.exceptions;

/**
 * zk操作异常类
 * @author dais
 *
 */
public class ZkOperationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 316186762130214088L;

	public ZkOperationException() {
	}

	public ZkOperationException(String message) {
		super(message);
	}

	public ZkOperationException(Throwable cause) {
		super(cause);
	}

	public ZkOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ZkOperationException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
