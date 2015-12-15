package xyz.daisong.service;

import xyz.daisong.zookeeper.ZkClient;
import xyz.daisong.zookeeper.exceptions.ZkConnectException;

public interface ZKConnectService {
	/**
	 * 连接zk
	 * @return
	 */
	public ZkClient connect() throws ZkConnectException;
}
