package xyz.daisong.service;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.Ordered;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import xyz.daisong.zookeeper.ZkClient;
import xyz.daisong.zookeeper.ZkClientFactory;
import xyz.daisong.zookeeper.ZkClientFactoryImpl;
import xyz.daisong.zookeeper.exceptions.ZkConnectException;
import xyz.daisong.zookeeper.exceptions.ZkOperationException;

/**
 * 
 * @author dais
 *
 */
public class ConfigurationCenterImpl extends PropertyPlaceholderConfigurer {
	//zk物理地址
	private static final String ZK_ADDRESS="zk-address";
	//应用地址
	private static final String APP_PATH = "app-path";

	private Logger logger = LoggerFactory.getLogger(ConfigurationCenterImpl.class);
	
	
	
	private int order = Ordered.LOWEST_PRECEDENCE;
	
	private BeanFactory beanFactory;
	
	private String beanName;
	
	@Override
	public int getOrder() {
		return order;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public String getBeanName() {
		return beanName;
	}
	
	@Override
	protected Properties mergeProperties() throws IOException {
		logger.info("Get ZK configuration information!");
		//zookeeper获取配置信息
		String zkAddress = null;
		String appPath = null;
		
		Properties resultTemp = new Properties();
		loadProperties(resultTemp);
		
		zkAddress = (String)resultTemp.get(ZK_ADDRESS);
		appPath = (String)resultTemp.getProperty(APP_PATH);
		
		for (Properties localProp : this.localProperties) {
			Object zkAddressObj = localProp.get(ZK_ADDRESS);
			Object appPathObj = localProp.get(APP_PATH);
			if(zkAddressObj != null){
				zkAddress = zkAddressObj.toString();
			}
			if(appPathObj != null){
				appPath = appPathObj.toString();
			}
		}
		logger.info("Start connection zk!");
		Properties result = new Properties();
		if(StringUtils.hasText(zkAddress) 
				&& StringUtils.hasText(appPath)){
			ZkClientFactory factory = new ZkClientFactoryImpl();
			ZkClient client = null;
			try {
				client = factory.newClient(zkAddress);
				Map<String, String> pathProps = client.getPathProps(appPath);
				logger.info("Start printing configuration center data!");
				for(Map.Entry<String, String> entry : pathProps.entrySet()){
					logger.info(entry.getKey() + "--------->" + entry.getValue());
				}
				logger.info("End print configuration center data!");
				result.putAll(pathProps);
			} catch (ZkConnectException | ZkOperationException e) {
				throw new IOException("Zookeeper Operation failure!",e);
			}finally{
				if(client != null){
					try {
						client.close();
					} catch (InterruptedException e) {
						// ignore exception
					}
				}
			}
		}else{
			throw new IOException("Configuration center configuration not found!");
		}
		logger.info("zk connection end!");
		
		//非配置中心加载配置
		if (this.localOverride) {
			// Load properties from file upfront, to let local properties override.
			loadProperties(result);
		}
		if (this.localProperties != null) {
			for (Properties localProp : this.localProperties) {
				CollectionUtils.mergePropertiesIntoMap(localProp, result);
			}
		}
		if (!this.localOverride) {
			// Load properties from file afterwards, to let those properties override.
			loadProperties(result);
		}
		return result;
	}
}
