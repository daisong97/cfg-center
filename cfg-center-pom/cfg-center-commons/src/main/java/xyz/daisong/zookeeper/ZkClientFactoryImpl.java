package xyz.daisong.zookeeper;

import xyz.daisong.zookeeper.exceptions.ZkConnectException;

public class ZkClientFactoryImpl implements ZkClientFactory {
	@Override
	public ZkClient newClient(String zkAddress) throws ZkConnectException{
		return new ZkClientDefault(zkAddress);
	}
}
