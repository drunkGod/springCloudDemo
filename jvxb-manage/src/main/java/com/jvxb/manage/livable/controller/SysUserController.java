package com.jvxb.manage.livable.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jvxb.common.web.RespMsg;
import com.jvxb.manage.livable.entity.SysUser;
import com.jvxb.manage.livable.service.SysUserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统用户表 前端控制器
 * </p>
 *
 * @author jvxb
 * @since 2020-06-07
 */
@RestController
@RequestMapping("/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService userService;

    @GetMapping("list")
    @ApiOperation("测试列表（全量）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "姓名", required = false, dataType = "String")
    })
    public RespMsg<List<SysUser>> list(String name) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.like(StrUtil.isNotEmpty(name), SysUser.USERNAME, name);
        List<SysUser> list = userService.list(wrapper);
        return RespMsg.ok(list);
    }

    @GetMapping("listPage")
    @ApiOperation("测试列表（分页）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "姓名", required = false, dataType = "String"),
            @ApiImplicitParam(name = "size", value = "每页条数，默认10条", dataType = "Integer", required = false),
            @ApiImplicitParam(name = "current", value = "第几页,默认第一页", dataType = "Integer", required = false)
    })
    public RespMsg<IPage<SysUser>> listPage(@RequestParam String name,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            @RequestParam(defaultValue = "1") Integer current) {
        IPage<SysUser> userIPage = new Page<>(current, size);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.like(StrUtil.isNotEmpty(name), SysUser.USERNAME, name);
        userService.page(userIPage, wrapper);
        return RespMsg.ok(userIPage);
    }

    @GetMapping("one")
    @ApiOperation("测试单个")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = false, dataType = "Integer")
    })
    public RespMsg<SysUser> one(Integer id) {
        SysUser user = userService.getById(id);
        return RespMsg.ok(user);
    }

    @PostMapping("add")
    @ApiOperation("测试新增")
    public RespMsg<SysUser> add(SysUser user) {
        userService.save(user);
        return RespMsg.ok(user);
    }

    @PostMapping("update")
    @ApiOperation("测试修改")
    public RespMsg<SysUser> update(SysUser user) {
        userService.updateById(user);
        return RespMsg.ok(user);
    }

    @PostMapping("delete")
    @ApiOperation("测试删除")
    public RespMsg<Void> delete(Integer id) {
        userService.removeById(id);
        return RespMsg.ok();
    }

}

