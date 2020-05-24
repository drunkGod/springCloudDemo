package com.jvxb.manage.livable.controller;

import com.baomidou.mybatisplus.core.conditions.Condition;
import com.jvxb.common.utils.BeanUtil;
import com.jvxb.common.web.RespMsg;
import com.jvxb.manage.livable.entity.User;
import com.jvxb.manage.livable.service.UserService;
import com.jvxb.manage.utils.PwdUtil;
import com.jvxb.modules.configuration.security.SecurityConstant;
import com.jvxb.modules.configuration.security.TokenProvider;
import com.jvxb.modules.configuration.security.UserDetail;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "登录")
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenProvider tokenProvider;

    @ApiOperation(value = "用户登录")
    @PostMapping(value = "/login")
    @ApiImplicitParams({@ApiImplicitParam(name = "name", value = "用户名", dataType = "String", required = true),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String", required = true)})
    public RespMsg<String> login(String name, String password) {
        User user = userService.getOne(Condition.<User>create().eq(User.NAME, name));
        if (user == null) {
            return RespMsg.error(SecurityConstant.ERROR_ACCOUNT_OR_PASSWORD, "账号或者密码不正确！");
        }

        if (!PwdUtil.verify(password, user.getPassword())) {
//            return ResponseMessage.error(SecurityConstant.ERROR_ACCOUNT_OR_PASSWORD, "账号或者密码不正确！");
            System.out.println("账号或者密码不正确！");
        }
        UserDetail userDetail = new UserDetail();
        BeanUtil.copy(user, userDetail);
        String token = tokenProvider.createToken(userDetail);
        return RespMsg.ok(token);
    }

    @RequestMapping("/sessionTimeout")
    public RespMsg<String> timeout() {
        return RespMsg.error(4000, "session过期或无效。");
    }
}
