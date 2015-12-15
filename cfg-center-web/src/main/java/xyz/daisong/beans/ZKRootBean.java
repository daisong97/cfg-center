package xyz.daisong.beans;

import java.util.Date;

public class ZKRootBean{
	/*
	 czxid. 节点创建时的zxid.
	mzxid. 节点最新一次更新发生时的zxid.
	ctime. 节点创建时的时间戳.
	mtime. 节点最新一次更新发生时的时间戳.
	dataVersion. 节点数据的更新次数.
	cversion. 其子节点的更新次数.
	aclVersion. 节点ACL(授权信息)的更新次数.
	ephemeralOwner. 如果该节点为ephemeral节点, ephemeralOwner值表示与该节点绑定的session id. 如果该节点不是ephemeral节点, ephemeralOwner值为0. 至于什么是ephemeral节点, 请看后面的讲述.
	dataLength. 节点数据的字节数.
	numChildren. 子节点个数. 
	 
	 */
	
	
	private Long id;
	
	private String rName;
	
	private String rData;
	
	private Integer version;
	
	private Date updateTime;
	
	private String path;
	
	private String _parentId;
	
	private String state;

	public String getrName() {
		return rName;
	}

	public void setrName(String rName) {
		this.rName = rName;
	}

	public String getrData() {
		return rData;
	}

	public void setrData(String rData) {
		this.rData = rData;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String get_parentId() {
		return _parentId;
	}

	public void set_parentId(String _parentId) {
		this._parentId = _parentId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
