package com.jvxb.manage.livable.service.impl;

import com.jvxb.manage.livable.entity.User;
import com.jvxb.manage.livable.mapper.UserMapper;
import com.jvxb.manage.livable.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author jvxb
 * @since 2020-04-05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
