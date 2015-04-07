$(function() {
	$("#province").change(function(){
		var pid = $(this).attr("value");
		$("#city").combobox("clear");
		fillOption(pid);
	});
	
//	$("#btn").click(function(){
//		$("#city").combobox("setValue",14);
//		$("#city").combobox("setText","aaa123");
//	});
	
	function fillOption(pid){
		var data = [];
		$.each(cities,function(i,v){
			if(v.provinceID==pid){
				data.push(v);
			}
		});
		$("#city").combobox("loadData",data);
	}
	
    $('#userEditDialog').dialog({
        buttons:[{text:'保存',handler:function(){
            if(!$('#userEditForm').form('validate')){return;}
            $('#userEditForm')._ajaxForm(function(r){
                if(r.r){$('#userEditDialog').dialog('close');$('#grid').datagrid('reload');}else{$.messager.alert('操作提示', r.m,'error');}
            });
        }},{text:'关闭',handler:function(){$('#userEditDialog').dialog('close');}}]
    });
    
    var grid = $('#grid')._datagrid({
        checkOnSelect:false,
        selectOnCheck:false,
        frozenColumns:[[
            {field:'ck',checkbox:true}
        ]],
        idField:'id',
        toolbar : [{
                    text : '创建用户',
                    iconCls : 'icon-add',
                    handler : handler_add
                }, '-', {
                    text : '批量删除',
                    iconCls : 'icon-remove',
                    handler : batch_del
                }, '-' ]
    });
    
    $('#queryButton').click(function(){
        var params = $('#queryForm')._formToJson();
        $(grid).datagrid('load',params);
    });

    /*新增用户*/
    function handler_add() {
    	$("#province option:eq(0)").attr("selected","selected");
        $('#userEditForm').attr('action','channel/add').resetForm();
        $('#id').val('');
        $('#userEditDialog').dialog('open').dialog("setTitle","新增渠道商");
    }
    /*批量删除*/
    function batch_del() {
        var check = $('#grid').datagrid('getChecked');
        if(check.length > 0){
            $.messager.confirm('操作提示', '确定要删除所选用户？', function(r){
                if (r){
                    var channelIds = new Array();
                    for(var i in check){
                    	channelIds[i] = check[i].id;
                    }
                    $._ajaxPost('channel/batch_del',{channelIds:channelIds.join(',')},function(r){
                        if(r.r){$('#grid').datagrid('reload');}else{$.messager.alert('操作提示', r.m,'error');}
                    });
                }
            });
        }
    }
});

var formatter = {
	type:function(value,rowData,rowIndex){
		if(value==1){
			return "公司";
		}else if(value==2){
			return "个人";
		}
	},
    opt : function(value, rowData, rowIndex) {
        var html = '<a class="spacing a-blue" onclick="updChannel('+rowIndex+');" href="javascript:void(0);">修改</a>';
            html+= '<a class="spacing a-red" onclick="delChannel('+rowIndex+');" href="javascript:void(0);">删除</a>';
        return html;
    }
};
/*修改渠道商*/
function updChannel(rowIndex) {
    $('#userEditForm').attr('action','channel/update').resetForm();
    var data = $('#grid').datagrid('getRows')[rowIndex];
    $('#userEditForm')._jsonToForm(data);
    $('#userEditDialog').dialog('open').dialog('setTitle','修改渠道商');
    $("#city").combobox("setValue",data.city);
    var cityData = $("#city").combobox("getData");
    $.each(cityData,function(i,v){
    	if(v.id==data.city){
    		$("#city").combobox("setText",v.name);
    	}
    });
}
/*删除渠道商*/
function delChannel(rowIndex) {
    $.messager.confirm('操作提示', '确定要删除该渠道商？', function(r){
        if (r){
            var data = $('#grid').datagrid('getRows')[rowIndex];
            $._ajaxPost('channel/del',{id:data.id}, function(r){
                if(r.r){$('#grid').datagrid('reload');}else{$.messager.alert('操作提示', r.m,'error');}
            });
        }
    });
}
