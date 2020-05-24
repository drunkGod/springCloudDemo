package com.jvxb.manage.livable.controller;


import com.baomidou.mybatisplus.core.conditions.Condition;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jvxb.manage.livable.entity.User;
import com.jvxb.manage.livable.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author jvxb
 * @since 2020-03-11
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("list")
    @ApiOperation("测试列表（全量）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "姓名", required = false, dataType = "String")
    })
    public List<User> list(String name) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.like(StringUtils.isNotEmpty(name), User.NAME, name);
        List<User> list = userService.list(wrapper);
        return list;
    }

    @GetMapping("listPage")
    @ApiOperation("测试列表（分页）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "姓名", required = false, dataType = "String"),
            @ApiImplicitParam(name = "size", value = "每页条数，默认10条", dataType = "Integer", required = false),
            @ApiImplicitParam(name = "current", value = "第几页,默认第一页", dataType = "Integer", required = false)
    })

    public IPage<User> listPage(@RequestParam String name,
                                @RequestParam(defaultValue = "10") Integer size,
                                @RequestParam(defaultValue = "1") Integer current) {
        IPage<User> userIPage = new Page<>(current, size);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.like(StringUtils.isNotEmpty(name), User.NAME, name);
        userService.page(userIPage, wrapper);
        return userIPage;
    }

    @GetMapping("one")
    @ApiOperation("测试单个")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Integer")
    })

    public User one(Integer id) {
        User user = userService.getOne(Condition.<User>create().eq(User.ID, id));
        return user;
    }

    @PostMapping("add")
    @ApiOperation("测试新增")
    public Object add(User user) {
        userService.save(user);
        return "ok";
    }

    @PostMapping("update")
    @ApiOperation("测试修改")
    public Object update(User user) {
        userService.updateById(user);
        return "ok";
    }

    @PostMapping("delete")
    @ApiOperation("测试删除")
    public Object delete(Integer id) {
        userService.removeById(id);
        return "ok";
    }
}

