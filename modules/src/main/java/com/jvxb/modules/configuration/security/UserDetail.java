package com.jvxb.modules.configuration.security;

import cn.hutool.core.bean.BeanUtil;
import com.jvxb.modules.livable.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@ApiModel(value = "当前登录用户信息", description = "")
@NoArgsConstructor
public class UserDetail implements Serializable {

	private static final long serialVersionUID = -2465090938483636062L;

	@ApiModelProperty(value = "用户ID")
	private Integer id;

	@ApiModelProperty(value = "登陆名")
	private String login;

	@ApiModelProperty(value = "密码")
	private String password;

	@ApiModelProperty(value = "用户名")
	private String name;

	@ApiModelProperty(value = "电话号码")
	private String tel;

	@ApiModelProperty(value = "用户角色")
	private String roles;


	public UserDetail(User user) {
		if (user != null) {
			BeanUtil.copyProperties(user, this);
		}
	}

}
