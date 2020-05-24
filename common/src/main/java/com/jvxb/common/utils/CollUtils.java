package com.jvxb.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import cn.hutool.core.collection.CollUtil;

/**
 * <丰富集合工具类>
 */
public class CollUtils extends CollUtil {

	/**
	 * 组装集合，提取重复代码
	 * 
	 * @param action
	 * @param list
	 * @return
	 */
	public static <S, T> List<T> instance(Function<S, T> action, List<S> list) {
		if (CollUtil.isEmpty(list))
			return null;

		List<T> target = new ArrayList<>();

		list.forEach(s -> {
			// 根据不同节点构建不同特征
			target.add(action.apply(s));
		});

		return target;
	}

}
