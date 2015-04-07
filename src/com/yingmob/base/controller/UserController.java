package com.yingmob.base.controller;

import java.util.Date;

import com.jfinal.aop.Before;
import com.jfinal.kit.EncryptionKit;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.yingmob.base.common.BaseController;
import com.yingmob.base.common.DisplayTool;
import com.yingmob.base.common.Pager;
import com.yingmob.base.model.SysRolesModel;
import com.yingmob.base.model.SysUserModel;
import com.yingmob.base.model.SysUserRoleModel;
import com.yingmob.core.plugin.annotation.Control;
import com.yingmob.tools.SysConstants;

/**
 * 用户
 * 
 *
 */
@Control(controllerKey = "/user")
public class UserController extends BaseController {

//	private static final Logger LOG = Logger.getLogger(UserController.class);

	public void index() {
//		setAttr("posts", SysPostsModel.dao.allPosts());
		setAttr("rolesJson", JsonKit.toJson(SysRolesModel.dao.allRoles()));
		render("tissue/user_mgr");
	}

	public void list() {
		SysUserModel user = getUser();
		Pager pager = createPager();

		pager.addParam("username", getPara("username"));
		pager.addParam("status", getPara("status"));
		pager.addParam("roleID", getPara("roleID"));
		pager.addParam("nick", getPara("nick"));
		pager.addParam("role", user.get("roleID"));

		Page<?> page = SysUserModel.dao.page(pager);

		setAttr("total", page.getTotalRow());
		setAttr("rows", page.getList());
		renderJson();
	}

	public void roleChecked() {
		Boolean checked = getParaToBoolean("checked");
		SysUserRoleModel userRole = getModelWithOutModelName(SysUserRoleModel.class, true);
		if (checked != null && userRole.get("roleID") != null && userRole.get("userID") != null) {
			setAttr(RESULT, true);
			SysUserRoleModel.dao.changeUserRole(userRole, checked);
			setAttr(MESSAGE, "修改成功！");
		} else {
			setAttr(RESULT, false);
			setAttr(MESSAGE, "参数错误，获取数据失败！");
		}
		renderJson();
	}

	@Before(Tx.class)
	public void add() {
		SysUserModel user = getModelWithOutModelName(SysUserModel.class, true);
		user.set("password", EncryptionKit.md5Encrypt(SysConstants.DEFAULT_PASSWORD));
		user.set("createDate", new Date());
		user.save();

//		modifyAuth(user);

		setAttr(RESULT, true);
		setAttr(MESSAGE, "新增成功！");
		setAttr("id", user.get("id"));
		DisplayTool.removeCache(DisplayTool.USER_KEY);
		renderJson();
	}

	@Before(Tx.class)
	public void update() {
		SysUserModel user = getModelWithOutModelName(SysUserModel.class, true);
		if (user.get("id") == null) {
			setAttr(RESULT, false);
			setAttr(MESSAGE, "更新失败！");
			renderJson();
			return;
		}
		user.set("updateDate", new Date());
		user.update();

		SysUserModel _user = getUser();
		if (user.get("id").equals(_user.get("id"))) {//更新session
			_user.set("nick", user.get("nick"));
			_user.set("email", user.get("email"));
		}

//		modifyAuth(user);

		setAttr(RESULT, true);
		setAttr(MESSAGE, "更新成功！");
		DisplayTool.removeCache(DisplayTool.USER_KEY);
		renderJson();
	}

	public void del() {
		Integer id = getParaToInt("id");
		if (id != null && SysUserModel.dao.deleteById(id)) {
			setAttr(RESULT, true);
			setAttr(MESSAGE, "删除成功！");
		} else {
			setAttr(RESULT, false);
			setAttr(MESSAGE, "删除失败！");
		}
		renderJson();
	}

	public void userMessage() {
		SysUserModel user=SysUserModel.dao.findById(getUser().get("id"));
		setAttr("user", user);
		//判断当前用户是否是渠道用户
		//String cid = ChannelInfoModel.cmdao.getIdByName(user.getStr("username"));
		setAttr("num", 0);
//		if (cid != null && !"".equals(cid)) {
//			setAttr("num", 1);
//		}
		render("tissue/user_message");
	}

	public void resetPassword() {
		SysUserModel user = new SysUserModel();
		String id = getPara("id");
		user.set("id", id);
		user.set("password", EncryptionKit.md5Encrypt(SysConstants.DEFAULT_PASSWORD));
		SysUserModel user2=SysUserModel.dao.findById(id);
//		ChannelModel channel=ChannelModel.dao.getByUserName(user2.getStr("username"));
//		String channelInfoId=ChannelInfoModel.cmdao.getIdByName(user2.getStr("username"));
//		if(channel!=null||(channelInfoId!=null&&!"".equals(channelInfoId))){
//			user.set("showPW", SysConstants.DEFAULT_PASSWORD);
//		}
		if (StrKit.notBlank(id) && user.update()) {
			setAttr(RESULT, true);
			setAttr(MESSAGE, "密码重置成功！");
		} else {
			setAttr(RESULT, false);
			setAttr(MESSAGE, "密码重置失败！");
		}
		renderJson();
	}

	public void password() {
		String oldPassword = getPara("oldPassword");
		String newPassword = getPara("newPassword");
		String entPassword = getPara("entPassword");

		boolean isError = true;
		if (StrKit.isBlank(oldPassword)) {
			setAttr(ERROR, "旧密码不能为空！");
		} else if (StrKit.isBlank(newPassword)) {
			setAttr(ERROR, "新密码不能为空！");
		} else if (StrKit.isBlank(entPassword)) {
			setAttr(ERROR, "确认密码不能为空！");
		} else if (!newPassword.equals(entPassword)) {
			setAttr(ERROR, "两次密码不相同！");
		} else {
			isError = false;
		}

		if (isError) {
			renderJson();
			return;
		}

		SysUserModel user = getUser();
//		ChannelModel channel=ChannelModel.dao.getByUserName(user.getStr("username"));
//		if(channel!=null){
//			user.set("showPW",newPassword);
//		}
		String oldPass = EncryptionKit.md5Encrypt(oldPassword);
		if (!user.getStr("password").equals(oldPass)) {
			setAttr(ERROR, "旧密码错误！");
		} else {
			user.set("password", EncryptionKit.md5Encrypt(newPassword));
			if (user.update()) {
				setAttr(RESULT, true);
				setAttr(MESSAGE, "密码修改成功！");
			} else {
				setAttr(RESULT, false);
				setAttr(ERROR, "密码修改失败！");
			}
		}
		renderJson();
	}

	public void batch_del() {
		String ids = getPara("userIds");
		try {
			String[] arrIds = ids.split("\\|");
			for (String id : arrIds) {
				if ("".equals(id))
					continue;
				SysUserModel.dao.deleteById(id);
			}
			setAttr(RESULT, true);
			setAttr(MESSAGE, "删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			setAttr(RESULT, false);
			setAttr(MESSAGE, "删除失败！");
		}
		renderJson();
	}
}
