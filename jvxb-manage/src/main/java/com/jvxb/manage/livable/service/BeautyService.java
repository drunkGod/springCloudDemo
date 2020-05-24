package com.jvxb.manage.livable.service;

import com.jvxb.manage.livable.entity.Beauty;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jvxb
 * @since 2020-04-05
 */
public interface BeautyService extends IService<Beauty> {

    Beauty getOneAndCache(Integer id);
    Beauty updateByIdAndCache(Beauty beauty);
}
