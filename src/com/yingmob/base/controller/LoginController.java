package com.yingmob.base.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.yingmob.base.common.BaseController;
import com.yingmob.base.model.SysLoginLogModel;
import com.yingmob.base.model.SysMenusModel;
import com.yingmob.base.model.SysUserModel;
import com.yingmob.core.plugin.annotation.Control;
import com.yingmob.tools.SysConstants;

/**
 * 无图标app
 * 
 * @author zyz
 *
 */
@Control(controllerKey = {"/login", "/"})
public class LoginController extends BaseController {

	private static final Logger LOG = Logger.getLogger(LoginController.class);

	public void index() {
		render("login");
	}

	public void loginCheck() {
		Map<String, Object> result = getResultMap();
		try {
			String username = getPara("username");
			String password = getPara("password");
			if (StrKit.isBlank(username) || StrKit.isBlank(password)) {
				result.put(ERROR, "用户名密码错误");
				renderJson(result);
			}
			SysUserModel user = SysUserModel.dao.login(username, password);
			if (null != user) {
				result.put(RESULT, true);
				user.put("menus", SysMenusModel.dao.getMenusByUserID(user.getInt("id")));
				setSessionAttr(SysConstants.SESSION_USER, user);
				SysLoginLogModel.dao.logLogin(user, getRealIpAddr(getRequest()));
			}
		} catch (Exception e) {
			result.put(ERROR, "登录失败");
			LOG.error("登录失败", e);
		}
		renderJson(result);
	}

	public void logout() {
		getSession().removeAttribute(SysConstants.SESSION_USER);
		redirect("/");
	}

	public void main() {
		try {
			SysUserModel user = getUser();
			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			setAttr("sysDate", format.format(Calendar.getInstance().getTime()));
			setAttr("menus", user.get("menus"));
			setAttr("nick", user.get("nick"));
			render("index");
		} catch (Exception e) {
			redirect("/");
		}
	}
}
