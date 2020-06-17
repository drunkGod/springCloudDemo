package com.jvxb.beauty.livable.controller;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jvxb.beauty.livable.service.BeautyService;
import com.jvxb.beauty.livable.service.VoteService;
import com.jvxb.beauty.remote.SearchService;
import com.jvxb.common.utils.Cu;
import com.jvxb.common.utils.EsCondition;
import com.jvxb.common.web.RespMsg;
import com.jvxb.beauty.livable.entity.Beauty;
import com.jvxb.modules.utils.NetUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jvxb
 * @since 2020-04-05
 */
@RestController
@RequestMapping("/beauty")
public class BeautyController {

    @Autowired
    private SearchService searchService;
    @Autowired
    private BeautyService beautyService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private VoteService voteService;

    public static void main(String[] args) {
        EsCondition esCondition = new EsCondition();
        esCondition.eq("field1","value1");
        System.out.println(esCondition);
    }

    @GetMapping("list")
    @ApiOperation("测试列表（全量）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "姓名", required = false, dataType = "String")
    })
    public Object list(String name) {


        List<Beauty> list = searchService.list(name);
        if (Cu.isNullOrEmpty(list)) {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.like(StrUtil.isNotEmpty(name), Beauty.NAME, name);
            wrapper.orderByDesc(Beauty.PS);
            list = beautyService.list(wrapper);
        }
        return RespMsg.ok(list);
    }

    /**
     * 根据投票id，使其票数+1
     * 注意，该方法需要同步。否则并发情况下票数无法准确增加。
     *
     * @param id
     * @return
     */
    @GetMapping("vote")
    @ApiOperation("投票")
    public RespMsg vote(HttpServletRequest request, Integer id) {
        System.out.println(DateUtil.format(new Date(), "HH:mm:ss") + "尝试投票：" + id);
        if (id == null || id < 0) {
            return RespMsg.error("无效投票");
        }

        //每个客户端每天只能投1票
        String clientIp = NetUtil.getIpAddress(request);
        if (!isValidVote(clientIp)) {
            return RespMsg.error("今日您已投票，请明日再来。");
        }

        //投票写入mq。
        try {
            Map<String, Object> voteMap = new HashMap<>();
            voteMap.put("voteId", id);
            voteMap.put("clientIp", clientIp);
            voteMap.put("voteTime", DateUtil.now());
            voteService.vote(voteMap);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return RespMsg.error("投票失败");
        }
        return RespMsg.ok("投票成功");
    }

    /**
     * 检查投票请求是否有效
     *
     * @param clientIp
     * @return
     */
    private boolean isValidVote(String clientIp) {
        String redisKey = "vote:" + clientIp;
        String clientValue = (String) redisTemplate.opsForValue().get(redisKey);
        String today = DateUtil.formatDate(new Date());
        //如果已经投过票，判断其上一次投的时间。
        if (ObjectUtil.isNotEmpty(clientValue)) {
            String lastDay = clientValue.substring(clientValue.length() - 10);
            //如果上一次投票时间为今天，则不允许再投。（白名单除外）
            if (lastDay.equals(today) && !isInWhiteList(clientIp)) {
                return false;
            }
        }
        //redis中形式如： key = "vote:127.0.0.1", value = "2020-02-02,2020-02-03";
        redisTemplate.opsForValue().set(redisKey, (ObjectUtil.isNotEmpty(clientValue) ? (clientValue + ",") : "") + today);
        return true;
    }

    private boolean isInWhiteList(String clientIp) {
        if (clientIp.equals("127.0.0.1")) {
            return true;
        }
        return false;
    }


}

