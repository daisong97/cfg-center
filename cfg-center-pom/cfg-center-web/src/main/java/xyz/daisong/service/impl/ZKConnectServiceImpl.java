package xyz.daisong.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import xyz.daisong.service.ZKConnectService;
import xyz.daisong.zookeeper.ZkClient;
import xyz.daisong.zookeeper.ZkClientFactory;
import xyz.daisong.zookeeper.ZkClientFactoryImpl;
import xyz.daisong.zookeeper.exceptions.ZkConnectException;

@Service("zKConnectService")
public class ZKConnectServiceImpl implements ZKConnectService {
	
	//private ZkClient zkClient;
	
	@Value("${zk-address}")
	private String zkAddress = "127.0.0.1:2181";
	
	@Value("${zk-scheme}")
	private String scheme;
	
	@Value("${zk-auth}")
	private String auth;
	
			
	@Override
	public ZkClient connect() throws ZkConnectException {
		return getConnect();
	}

	private synchronized ZkClient getConnect() throws ZkConnectException{
		/*if(zkClient != null 
				&& zkClient.getZookeeper().getState().isAlive()){
				return zkClient;
		}*/
		ZkClientFactory factory = new ZkClientFactoryImpl();
		ZkClient client = null;
		client = factory.newClient(zkAddress);
		//client.getZookeeper().addAuthInfo(scheme, auth.getBytes());
		return client;
	}
	public static void main(String[] args) throws Exception {
		//127.0.0.1:2181
		ZKConnectServiceImpl pool = new ZKConnectServiceImpl();
		ZkClient connect = pool.getConnect();
		
		connect.mkdir("/p1/p2/p3/p4/p5","hello world!");
		connect.close();
	}
}
