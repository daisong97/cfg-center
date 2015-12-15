package xyz.daisong.zookeeper;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import xyz.daisong.constant.CCConstant;
import xyz.daisong.zookeeper.exceptions.ZkConnectException;
import xyz.daisong.zookeeper.exceptions.ZkOperationException;

public class ZkClientDefault implements ZkClient {
	private Logger logger = LoggerFactory.getLogger(ZkClientDefault.class);	
	private ZooKeeper zookeeper;
	
	public ZkClientDefault(String zkAddress) throws ZkConnectException {
		CountDownLatch connectedLatch = new CountDownLatch(1);
		try {
			//等待CCConstant.WAIT_CONECTION_ZK_TIME后放弃连接!
			Thread thread = new Thread(){
				@Override
				public void run() {
					try {
						sleep(CCConstant.WAIT_CONECTION_ZK_TIME);
						connectedLatch.countDown();
					} catch (InterruptedException e) {
						//ignore exception
					}
				}
			};
			thread.start();
			zookeeper = new ZooKeeper(zkAddress, 5000, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					if(event.getState() == KeeperState.SyncConnected){
						connectedLatch.countDown();
						//停止延时线程
						//thread.stop();
						thread.interrupt();
					}
				}
			});
			logger.info("Connection waiting for ZK!");
			connectedLatch.await();
		} catch (Exception e) {
			throw new ZkConnectException(e);
		}finally{
			connectedLatch.countDown();
		}
	}
	
	@Override
	public List<String> getChildren(String path) throws ZkOperationException{
		try {
			return zookeeper.getChildren(CCConstant.SYS_ROOT_PATH + path, false);
		} catch (KeeperException | InterruptedException e) {
			throw new ZkOperationException(e);
		}
	}
	
	@Override
	public String getData(String path) throws ZkOperationException{
		try {
			return new String(zookeeper.getData(CCConstant.SYS_ROOT_PATH + path, false, new Stat()));
		} catch (KeeperException | InterruptedException e) {
			throw new ZkOperationException(e);
		}
	}
	
	@Override
	public Map<String,String> getPathProps(String path) throws ZkOperationException{
		Map<String,String> props = new HashMap<String, String>();
		List<String> children = getChildren(path);
		String appPathData = getData(path);
		if(CCConstant.IS_ONLINE_TRUE.equals(appPathData) 
				&& !CollectionUtils.isEmpty(children)){
			for(String s : children){
				//引用路径
				if(CCConstant.QUOTE_PATH.equals(s)){
					String qPath = getData(path + "/" + s);
					for(String q : qPath.split(",")){
						Map<String, String> d = getPathProps("/" + q);
						props.putAll(d);
					}
				}else{
					props.put(s, getData(path + "/" + s));
				}
			}
		}
		return props;
	}
	
	public void close() throws InterruptedException{
		zookeeper.close();
	}

	@Override
	public ZooKeeper getZookeeper() {
		return this.zookeeper;
	}

	@Override
	public List<String> children(String path,Stat stat) throws ZkOperationException {
		try{
			return zookeeper.getChildren(path, false,stat);
		}catch(Exception e){
			throw new ZkOperationException(e);
		}
	}

	@Override
	public String data(String path,Stat stat) throws ZkOperationException {
		try {
			byte[] data = zookeeper.getData(path, false, stat);
			if(data == null){
				return "";
			}
			return new String(data);
		} catch (KeeperException | InterruptedException e) {
			throw new ZkOperationException(e);
		}
	}

	@Override
	public void update(String path, String rData) throws ZkOperationException {
		try{
			zookeeper.setData(path, rData.getBytes(), -1);
		}catch(Exception e){
			throw new ZkOperationException(e);
		}
	}

	@Override
	public void delete(String path) throws ZkOperationException {
		try {
			zookeeper.delete(path,  -1);
		} catch (InterruptedException | KeeperException e) {
			throw new ZkOperationException(e);
		}
	}

	@Override
	public void create(String path, String data) throws ZkOperationException {
		try {
			byte[] tempBytes= null;
			if(data != null){
				tempBytes = data.getBytes();
			}
			zookeeper.create(path, tempBytes,Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		} catch (KeeperException | InterruptedException e) {
			throw new ZkOperationException(e);
		}
	}

	@Override
	public boolean exists(String path) throws ZkOperationException {
		try{
			return zookeeper.exists(path, false) != null;
		}catch(Exception e){
			throw new ZkOperationException(e);
		}
	}
	
	@Override
	public void mkdir(String path, String data) throws ZkOperationException {
		if(exists(path)){
			update(path, data);
			return ;
		}
		String tempPath = "";
		if(StringUtils.isNotBlank(path)){
			String[] split = path.split("/");
			for(int i = 0 ; i<split.length ; i ++){
				if(StringUtils.isBlank(split[i])){
					continue;
				}
				tempPath += ("/" + split[i]);
				if(!exists(tempPath)){
					if(i == (split.length -1)){
						create(tempPath, data);
					}else{
						create(tempPath, null);
					}
				}
			}
		}
	}

	@Override
	public void setAuthNull(String path) throws ZkOperationException {
		try{
			setAuth(path,Ids.OPEN_ACL_UNSAFE);
		}catch(Exception e){
			throw new ZkOperationException(e);
		}
	}

	@Override
	public void setAuthUserInfo(String path, String username, String password,Perms perms) throws ZkOperationException {
		try{
			List<ACL> acls = new ArrayList<ACL>();
			ACL acl = new ACL(ZooDefs.Perms.ALL, new Id("digest", 
					DigestAuthenticationProvider.generateDigest(username + ":"+ password)));
			acls.add(acl);
			setAuth(path,acls);
		}catch(Exception e){
			throw new ZkOperationException(e);
		}
	}

	@Override
	public void setAuthIp(String path, String ip,Perms perms) throws ZkOperationException{
		try{
			List<ACL> acls = new ArrayList<ACL>();
			ACL acl = new ACL(ZooDefs.Perms.ALL, new Id("ip", ip));
			acls.add(acl);
			setAuth(path, acls);
		}catch(Exception e){
			throw new ZkOperationException(e);
		}

	}

	@Override
	public void addAuthInfoByUserInfo(String username, String password) {
		zookeeper.addAuthInfo("digest", (username + ":" + password).getBytes());
	}

	@Override
	public void addAuthInfoByIp(String ip) {
		zookeeper.addAuthInfo("ip", ip.getBytes());
	}
	/**
	 * 设置权限
	 * @param path
	 * @param username
	 * @param password
	 * @param acls
	 * @throws NoSuchAlgorithmException
	 * @throws KeeperException
	 * @throws InterruptedException
	 */
	private void setAuth(String path,List<ACL> acls)
			throws NoSuchAlgorithmException, KeeperException,
			InterruptedException {
		List<String> children =  zookeeper.getChildren(path, false);
		zookeeper.setACL(path, acls, -1);
		if(!CollectionUtils.isEmpty(children)){
			for(String p : children){
				setAuth(path + "/" + p, acls);
			}
		}
		
	}
}
