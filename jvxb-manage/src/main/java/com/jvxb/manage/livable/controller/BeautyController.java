package com.jvxb.manage.livable.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jvxb.common.base.entity.es.EsDocument;
import com.jvxb.common.utils.BeanUtil;
import com.jvxb.common.utils.Cu;
import com.jvxb.common.web.RespMsg;
import com.jvxb.manage.livable.entity.Beauty;
import com.jvxb.manage.livable.service.BeautyService;

import com.jvxb.manage.remote.service.RemoteVoteService;
import com.jvxb.manage.remote.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jvxb
 * @since 2020-03-28
 */
@RestController
@RequestMapping("/beauty")
@Api(tags = "美女")
public class BeautyController {

    @Autowired
    private BeautyService beautyService;
    @Autowired
    private RemoteVoteService remoteVoteService;
    @Autowired
    private SearchService searchService;

    @GetMapping("listPage")
    @ApiOperation("测试列表（分页）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "姓名", required = false, dataType = "String"),
            @ApiImplicitParam(name = "size", value = "每页条数，默认10条", dataType = "Integer", required = false),
            @ApiImplicitParam(name = "current", value = "第几页,默认第一页", dataType = "Integer", required = false)
    })
    public Object listPage(@RequestParam String name,
                           @RequestParam(defaultValue = "10") Integer size,
                           @RequestParam(defaultValue = "1") Integer current) {
        IPage<Beauty> beautyIPage = new Page<>(current, size);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.like(StringUtils.isNotEmpty(name), Beauty.NAME, name);
        wrapper.orderByDesc(Beauty.PS);
        beautyService.page(beautyIPage, wrapper);
        return RespMsg.ok(beautyIPage);
    }

    @GetMapping("one")
    @ApiOperation("测试单个")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, dataType = "Integer")
    })
    public Object one(Integer id) {
        Beauty beauty = beautyService.getOneAndCache(id);
        return RespMsg.ok(beauty);
    }

    @PostMapping("add")
    @ApiOperation("测试新增")
    public Object add(Beauty beauty) {
        beauty.setCreatetime(new Date());
        beautyService.save(beauty);
        searchService.save(new EsDocument("beauty_index", "beauty", BeanUtil.bean2Map(beauty)));
        return RespMsg.ok(beauty.getId());
    }

    @PostMapping("update")
    @ApiOperation("测试修改")
    public Object update(Beauty beauty) {
        System.out.println(beauty);
        beautyService.updateByIdAndCache(beauty);
        return RespMsg.ok(beauty);
    }

    @PostMapping("delete")
    @ApiOperation("测试删除")
    public Object delete(Integer id) {
        beautyService.removeById(id);
        searchService.delete(new EsDocument("beauty_index", "beauty", Cu.getMap("id", id)));
        return RespMsg.ok();
    }

    @PostMapping("batchDelete")
    @ApiOperation("测试批量删除")
    public Object batchDelete(Integer[] ids) {
        if (ObjectUtil.isEmpty(ids)) {
            return RespMsg.error("参数不正确");
        }
        beautyService.removeByIds(Arrays.asList(ids));
        return RespMsg.ok();
    }

    @GetMapping("vote")
    @ApiOperation("投票")
    public Object vote(HttpServletRequest request, Integer id) {
        RespMsg voteResult = remoteVoteService.vote(request, id);
        return voteResult;
    }

}

