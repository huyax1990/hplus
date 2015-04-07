$(function() {
	var grid = $('#grid').datagrid({
		checkOnSelect : false,
		selectOnCheck : false,
		fitColumns : true,
		frozenColumns : [ [ {
			field : 'ck',
			checkbox : true
		} ] ],
		toolbar : [ {
			text : '创建用户',
			iconCls : 'icon-add',
			handler : handler_add
		}, '-', {
			text : '删除所选',
			iconCls : 'icon-remove',
			handler : batch_del
		}, '-' ]
	});

	/* 新增用户 */
	function handler_add() {
		$('#userEditForm').attr('action', 'user/add').resetForm();
		$('#username').removeAttr('readonly');
		$('#id').val('');
		// $('#roleID')._pullDownTree('clear');
		$('#userEditDialog').dialog('open').dialog("setTitle", "新增用户");
	}
	/* 批量删除 */
	function batch_del() {
		var check = $('#grid').datagrid('getChecked');
		if (check.length > 0) {
			$.messager.confirm('操作提示', '确定要删除所选用户？', function(r) {
				if (r) {
					var userIds = new Array();
					for ( var i in check) {
						userIds[i] = check[i].id;
					}
					$._ajaxPost('user/batch_del', {
						userIds : userIds.join('|')
					}, function(r) {
						if (r.r) {
							$('#grid').datagrid('reload');
						} else {
							$.messager.alert('操作提示', r.m, 'error');
						}
					});
				}
			});
		}
	}
});
var formatter = {
	status : function(value, rowData, rowIndex) {
		if (value == 1) {
			return '<font color=green>正常</font>';
		} else {
			return '<font color=red>停用</font>';
		}
	},
	roles : function(value, rowData, rowIndex) {
		return value;
	},
	posts : function(value, rowData, rowIndex) {
		var arr = [];
		for (i in value) {
			arr.push($.fn.display.post[value[i]]);
		}
		return arr.join(',');
	},
	opt : function(value, rowData, rowIndex) {
		var html = '<button type="button" onclick="updUser(' + rowIndex
				+ ');" class="btn btn-xs btn-success">修改</button>&nbsp;';
		html += '<button type="button" onclick="updRole(' + rowIndex
				+ ');" class="btn btn-xs btn-primary">设置角色</button>&nbsp;';
		html += '<button type="button" onclick="resetPassword(' + rowIndex
				+ ');" class="btn btn-xs btn-warning">重置密码</button>&nbsp;';
		html += '<button type="button" onclick="delUser(' + rowIndex
				+ ');" class="btn btn-xs btn-danger">删除</button>';

		return html;
	}
};
/* 分配角色 */
function updRole(rowIndex) {
	var data = $('#grid').datagrid('getRows')[rowIndex];
	$('#roleZtee').data('userID', data.id).data('rowIndex', rowIndex);
	var zTree = $.fn.zTree.getZTreeObj("roleZtee");
	zTree.checkAllNodes(false);
	var temp = data.roles && data.roles.split(",");
	for ( var i in temp) {
		zTree.checkNode(zTree.getNodeByParam('id', temp[i], null), true, false,
				false);
	}
	$('#userRoleDialog').dialog({
		closed : false
	});
}
/* 修改用户 */
function updUser(rowIndex) {
	$('#userEditForm').attr('action', 'user/update')
	var data = $('#grid').datagrid('getRows')[rowIndex];
	$('#userEditForm')._jsonToForm(data);
	$("#myModal").modal();
}
/* 删除用户 */
function delUser(rowIndex) {
	$.messager.confirm('操作提示', '确定要删除该用户？', function(r) {
		if (r) {
			var data = $('#grid').datagrid('getRows')[rowIndex];
			$._ajaxPost('user/del', {
				id : data.id
			}, function(r) {
				if (r.r) {
					$('#grid').datagrid('reload');
				} else {
					$.messager.alert('操作提示', r.m, 'error');
				}
			});
		}
	});
}
function resetPassword(rowIndex) {
	$.messager.confirm('操作提示', '确定重置该用户密码？', function(r) {
		if (r) {
			var data = $('#grid').datagrid('getRows')[rowIndex];
			$._ajaxPost('user/resetPassword', {
				id : data.id
			}, function(r) {
				if (r.r) {
					$('#grid').datagrid('reload');
				} else {
					$.messager.alert('操作提示', r.m, 'error');
				}
			});
		}
	});
}

function submit() {
//	if (!$('#userEditForm').form('validate')) {
//		return;
//	}
	var vali = $('#userEditForm').validate();

	$('#userEditForm')._ajaxForm(function(r) {
		if (r.r) {
			$('#userEditDialog').dialog('close');
			$('#grid').datagrid('reload');
		} else {
			$.messager.alert('操作提示', r.m, 'error');
		}
	});
}