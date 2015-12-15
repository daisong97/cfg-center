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
		ZkClientFactory factory = new ZkClientFactoryImpl();
		ZkClient zkClient  = factory.newClient(zkAddress);
		return zkClient;
	}
	public static void main(String[] args) throws Exception {
		//127.0.0.1:2181
		ZKConnectServiceImpl pool = new ZKConnectServiceImpl();
		ZkClient connect = pool.getConnect();
		
	//	connect.mkdir("/p1/p2/p3/p4/p5","hello world!");
		
		//写
		/*List<ACL> acls = new ArrayList<ACL>();
		ACL acl = new ACL(ZooDefs.Perms.ALL, new Id("digest", DigestAuthenticationProvider.generateDigest("admin:123")));
		acls.add(acl);
		connect.getZookeeper().setACL("/p1/p2/p3/p4", acls, -1);*/
		//读
		/*connect.getZookeeper().addAuthInfo("digest", "admin:123".getBytes());
		String data = connect.data("/p1/p2/p3/p4", null);
		System.out.println("----->" + data);*/
		//connect.mkdir("/a1/a2","hello world!");
		//connect.setAuthUserInfo("/a1", "admin", "admin");
		
		//connect.addAuthInfoByUserInfo("admin", "admin");
		//connect.setAuthNull("/k1");
	/*	try{
			connect.children("/k22",null);
		}catch(Exception e){
			if(e.getCause() instanceof NoAuthException){
				System.out.println("没有权限!!!!");
			}else if(e.getCause() instanceof NoNodeException){
				System.out.println("没有节点!!!");
			}
		}
		connect.close();*/

	}
}
