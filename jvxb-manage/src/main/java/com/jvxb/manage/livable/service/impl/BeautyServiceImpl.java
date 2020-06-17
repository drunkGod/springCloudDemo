package com.jvxb.manage.livable.service.impl;

import com.jvxb.manage.livable.entity.Beauty;
import com.jvxb.manage.remote.mapper.BeautyMapper;
import com.jvxb.manage.livable.service.BeautyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author jvxb
 * @since 2020-04-05
 */
@Service
public class BeautyServiceImpl extends ServiceImpl<BeautyMapper, Beauty> implements BeautyService {

    @Override
    @Cacheable(value = "user", key = "#id")
    public Beauty getOneAndCache(Integer id) {
        return baseMapper.selectById(id);
    }


    @Override
    @CacheEvict(value = "beauty", key = "#beauty.id")
    public Beauty updateByIdAndCache(Beauty beauty) {
        baseMapper.updateById(beauty);
        return beauty;
    }
}
