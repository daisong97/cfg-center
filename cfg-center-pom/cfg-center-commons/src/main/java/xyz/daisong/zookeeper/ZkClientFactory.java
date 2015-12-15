package xyz.daisong.zookeeper;

import xyz.daisong.zookeeper.exceptions.ZkConnectException;

public interface ZkClientFactory {

	ZkClient newClient(String zkPath) throws ZkConnectException;

}