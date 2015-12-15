<#assign basePath = req.contextPath>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>zk管理</title>
    <link rel="stylesheet" type="text/css" href="${basePath}/commons/css/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="${basePath}/commons/css/themes/icon.css">
    <script type="text/javascript" src="${basePath}/commons/js/jquery.min.js"></script>
    <script type="text/javascript" src="${basePath}/commons/js/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="${basePath}/commons/js/locale/easyui-lang-zh_CN.js"></script>
    
    <style type="text/css">  
    html,body  
    {  
        height:100%;  
        margin:0 auto;  
    }  
</style> 
</head>
<body class="easyui-layout">
        <div data-options="region:'north'" style="height:50px;">
        	<h2>配置中心</h2>
        </div>
        <div data-options="region:'south',split:true" style="height:50px;"></div>
        <!--
        <div class="easyui-accordion" data-options="region:'west',split:true" title="菜单" style="width:20%;">
        	<div class="easyui-accordion" data-options="fit:true">
        		<div title="配置管理" data-options="iconCls:'icon-edit'" style="overflow:auto;padding:10px;">
		            <div  class="panel-body panel-body-noheader layout-body">
			        	<h2>配置中心</h2>
			        </div>
		        </div>
		        <div title="ZK管理" data-options="iconCls:'icon-help'" style="padding:10px;">
		                     
		        </div>
		    </div>
        </div>
        -->
         <div id="dialog-frm" class="easyui-dialog" title="管理" style="width:400px;height:200px;padding:10px"
	            data-options="
	                iconCls: 'icon-save',
	                modal:true,
	                closed:true,
	                buttons: [{
	                    text:'确定',
	                    iconCls:'icon-ok',
	                    handler:function(json){
	                    	$('#zkFrm').form('submit');
	                    	$('#dialog-frm').dialog('close');
	                    	//$('#zkGrid').treegrid('reload');
	                    	//window.location.reload();
	                    	alert(json.message);
	                    	if(json.success){
	                    		window.location.reload();
	                    	}
	                    }
	                },{
	                    text:'取消',
	                    handler:function(){
	                    	$('#dialog-frm').dialog('close');
	                    }
	                }]
	            ">
	         <form id="zkFrm" method="post" action="updateOrSaveZkNodeValue.htm">
	            <table cellpadding="5">
	                <tr>
	                    <td>节点路径:</td>
	                    <td><input class="easyui-textbox" id="zkFrmPath" type="text" name="path" readonly data-options="required:true"/></td>
	                </tr>
	                <tr>
	                    <td>节点值:</td>
	                    <td><input class="easyui-textbox" type="text" name="rData" data-options="required:true"/></td>
	                </tr>
	            </table>
	        </form>
	    </div>
        
        <div class="easyui-tabs" data-options="region:'center',title:'工作区',iconCls:'icon-ok'">
            <div title="配置管理" >
            	
	        </div>
	        <div title="ZK管理" >
	            <table id="zkGrid" title="ZK 管理" class="easyui-treegrid" 
			            data-options="
			                url: 'queryZkTreeData.htm',
			                method: 'post',
			                rownumbers: true,
			                idField: 'path',
			                treeField: 'rName',
			                pageSize: 10,
			                fitColumns:true,
			                toolbar:toolbar,
			                onBeforeLoad: onBeforeLoad
			            ">
			        <thead>
			            <tr>
			                <th data-options="field:'rName',editor:'text'">借点名称</th>
			                <th data-options="field:'rData',editor:'text'" >节点数据</th>
			                <th data-options="field:'updateTime'" >修改时间</th>
			                <th data-options="field:'version'" >版本</th>
			            </tr>
			        </thead>
			    </table>
			    <script type="text/javascript">
			    	function onBeforeLoad(row,param){
			    		if (!row) {    
			                param.id = null;
			            }
			    	}
			        var toolbar = [{
			            text:'添加',
			            iconCls:'icon-add',
			            handler:function(){
			            	var t = $("#zkGrid").datagrid('getSelected');
			            	var temp = {};
			            	if(!t){
			            		temp.path = '/';
			            	}else{
			            		temp.path = toZkPath(t.path);
			            	}
			            	$('#zkFrm').form('load',temp);
			            	$('#dialog-frm').dialog('open');
			            	$("#zkFrmPath").textbox({readonly:false});
			            }
			        },{
			            text:'删除',
			            iconCls:'icon-remove',
			            handler:function(){
			            	var t = $("#zkGrid").datagrid('getSelected');
			            	if(t){
				            	$.messager.confirm('提示','你确定要删除改节点?',function(r){
								    if (r){
								    	$.post('deleteNode.htm',{path:toZkPath(t.path)},function(json){
								    		alert(json.message);
								    		if(json.success){
								    			window.location.reload();
								    		}
								    	},'json');
								    }
								});
							}else{
								alert('请选择节点!');
							}
			            }
			        },'-',{
			            text:'修改',
			            iconCls:'icon-save',
			            handler:function(){
			            	$('#zkFrm').form('clear');
			            	var t = $("#zkGrid").datagrid('getSelected');
			            	if(t){
			            		var temp = {};
			            		temp.rData = t.rData;
			            		temp.path = toZkPath(t.path);
			            		$('#zkFrm').form('load',temp);
			            		$("#zkFrmPath").textbox({readonly:true});
			            		$('#dialog-frm').dialog('open');
			            		
			            	}else{
			            		alert('请选择节点!');
			            	}
			            	
			            }
			        }];
			        function toZkPath(str){
			        	return str.replace(new RegExp('--','gm'),'/');
			        }
			    </script>
	        </div>
        </div>
</body>
</html>