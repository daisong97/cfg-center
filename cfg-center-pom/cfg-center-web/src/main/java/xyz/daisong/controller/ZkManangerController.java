package xyz.daisong.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.daisong.beans.Pageable;
import xyz.daisong.beans.ZKRootBean;
import xyz.daisong.service.ZKConnectService;
import xyz.daisong.zookeeper.ZkClient;

@Controller
public class ZkManangerController {
	private Logger logger = LoggerFactory.getLogger(ZkManangerController.class);	

	@Autowired
	private ZKConnectService zkService;
	
	@RequestMapping("/queryZkTreeData")
	@ResponseBody
	public Pageable queryZkTreeData(String id,Integer page,Integer rows) throws Exception {
		ZkClient connect = null;
		Pageable pageable = new Pageable();
		try{
			id = StringUtils.isBlank(id) ? "/" : id;
			String parentId = id;
			id = id.replace("--", "/");
			connect = zkService.connect();
			List<String> children = connect.children(id, null);
			id = id.endsWith("/") ? id : id + "/";
			List<ZKRootBean> dataRows = new ArrayList<ZKRootBean>();
			pageable.setRows(dataRows);
			if (!CollectionUtils.isEmpty(children)) {
				for (String p : children) {
					ZKRootBean zkb = new ZKRootBean();
					Stat stat = new Stat();
					String chPath = id + p;
					String rData = connect.data(chPath, stat);
					zkb.setrData(rData);
					zkb.setrName(p);
					zkb.setPath(chPath.replace("/", "--"));
					zkb.setVersion(stat.getVersion());
					zkb.setUpdateTime(new Date(stat.getCtime()));
					zkb.setId(stat.getMzxid());
					if(!"/".equals(parentId)){
						zkb.set_parentId(parentId);
					}
					if(stat.getNumChildren() > 0){
						zkb.setState("closed");
					}
					dataRows.add(zkb);
				}
			}
			pageable.setTotal(dataRows.size());
		}finally{
			if(connect != null){
				connect.close();
			}
		}
		return pageable;
	}
	@RequestMapping("/updateOrSaveZkNodeValue")
	@ResponseBody
	public Map<String,Object> updateOrSaveZkNodeValue(@RequestParam String path,@RequestParam String rData) throws Exception{
		Map<String,Object> result = new HashMap<String, Object>();
		ZkClient connect = null;
		try{
			connect = zkService.connect();
			if(connect.exists(path)){
				connect.update(path,rData);
				result.put("success", true);
				result.put("message", "修改成功!");
			}else{
				connect.create(path,rData);
				result.put("success", true);
				result.put("message", "保存成功!");
			}
			return result;
		}finally{
			if(connect != null){
				connect.close();
			}
		}
	}
	@RequestMapping("/deleteNode")
	@ResponseBody
	public Map<String,Object> deleteNode(String path) throws InterruptedException{
		Map<String,Object> result = new HashMap<String, Object>();
		ZkClient connect = null;
		try{
			connect = zkService.connect();
			connect.delete(path);
			result.put("success", true);
			result.put("message", "修改成功!");
			return result;
		}catch(Exception e){
			if(connect != null){
				connect.close();
			}
			result.put("success", false);
			result.put("message", "删除节点失败!");
			logger.error("删除节点失败!",e);
		}finally{
			if(connect != null){
				connect.close();
			}
		}
		return result;
	}
}
