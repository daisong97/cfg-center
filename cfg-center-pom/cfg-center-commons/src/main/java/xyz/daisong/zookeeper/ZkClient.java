package xyz.daisong.zookeeper;

import java.util.List;
import java.util.Map;

import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import xyz.daisong.zookeeper.exceptions.ZkOperationException;

public interface ZkClient {
	/**
	 * 加默认路径
	 * @param path
	 * @return
	 * @throws ZkOperationException
	 */
	List<String> getChildren(String path)
			throws ZkOperationException;

	/**
	 * 加默认路径
	 * @param path
	 * @return
	 * @throws ZkOperationException
	 */
	String getData(String path) throws ZkOperationException;
	/**
	 * 加默认路径
	 * @param path
	 * @return
	 * @throws ZkOperationException
	 */
	Map<String, String> getPathProps(String path)
			throws ZkOperationException;
	
	ZooKeeper getZookeeper();
	
	/**
	 * 正常获取
	 * @param path
	 * @return
	 * @throws ZkOperationException
	 */
	List<String> children(String path,Stat stat) throws ZkOperationException;
	/**
	 * 正常获取
	 * @param path
	 * @return
	 * @throws ZkOperationException
	 */
	String data(String path,Stat stat) throws ZkOperationException;

	void close() throws InterruptedException;

	/**
	 * 修改节点数据
	 * @param path
	 * @param rData
	 * @throws ZkOperationException
	 */
	void update(String path, String rData) throws ZkOperationException ;
	
	/**
	 * 删除节点
	 * @param path
	 * @throws ZkOperationException
	 */
	void delete(String path) throws ZkOperationException;
	
	/**
	 * 创建节点
	 * @param path
	 * @param data
	 */
	void create(String path,String data) throws ZkOperationException;

	/**
	 * 节点是否存在
	 * @param path
	 * @return
	 */
	boolean exists(String path) throws ZkOperationException ;
	/**
	 * 迭代创建创建节点
	 */
	void mkdir(String path, String data) throws ZkOperationException ;
}