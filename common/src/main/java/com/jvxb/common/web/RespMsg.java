package com.jvxb.common.web;

import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.beans.Transient;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

@ApiModel(value = "返回值对象", description = "")
public class RespMsg<T> implements Serializable {

	private static final String SUCCESS_MSG = "操作成功！";

	private static final String FAIL_MSG = "服务器繁忙，请稍后重试！";

	public static final String UNAUTHORIZED_MSG = "用户未登陆或已在其它设备上登录！";

	private static final long serialVersionUID = 6814687266629850294L;

	private static final Integer HTTP_OK = 200;

	private static final int HTTP_INTERNAL_ERROR = 500;

	/**
	 * 响应状态码
	 *
	 * @see HttpStatus
	 */
	@ApiModelProperty("响应状态码")
	protected Integer code;

	/**
	 * 响应消息，用于描述响应结果
	 */
	@ApiModelProperty("响应消息，用于描述响应结果")
	protected String message;

	/**
	 * 响应数据
	 */
	@ApiModelProperty("响应数据")
	protected T data;

	/**
	 * 服务器时间戳
	 */
	@ApiModelProperty("服务器时间戳")
	private Long timestamp;

	/**
	 * 过滤字段：指定需要序列化的字段
	 */
	@JsonIgnore
	private transient Map<Class<?>, Set<String>> includes;

	/**
	 * 过滤字段：指定不需要序列化的字段
	 */
	@JsonIgnore
	private transient Map<Class<?>, Set<String>> excludes;

	public boolean success() {
		return code == HTTP_OK;
	}

	public static <T> RespMsg<T> error(String message) {
		return error(HTTP_INTERNAL_ERROR, message);
	}

	public static <T> RespMsg<T> error(int code, String message) {
		return error(code, message, null);
	}

	public static <T> RespMsg<T> error(int code, String message, T result) {
		RespMsg<T> msg = new RespMsg<>();
		if (code == HTTP_INTERNAL_ERROR && message == null) { // 500错误统一返回错误信息
			message = FAIL_MSG;
		}
		msg.setData(result);
		msg.setMessage(message);
		return msg.code(code).putTimeStamp();
	}

	public static <T> RespMsg<T> ok() {
		return ok(null);
	}

	public static <T> RespMsg<T> ok(T result) {
		return new RespMsg<T>().code(HTTP_OK).putTimeStamp().result(result).message(SUCCESS_MSG);
	}

	public RespMsg<T> code(int code) {
		this.code = code;
		return this;
	}

	public RespMsg<T> result(T result) {
		this.data = result;
		return this;
	}

	public RespMsg<T> message(String message) {
		this.message = message;
		return this;
	}

	private RespMsg<T> putTimeStamp() {
		this.timestamp = System.currentTimeMillis();
		return this;
	}

	public RespMsg<T> include(Class<?> type, String... fields) {
		return include(type, Arrays.asList(fields));
	}

	public RespMsg<T> include(Class<?> type, Collection<String> fields) {
		if (includes == null) {
			includes = new HashMap<>();
		}
		if (fields == null || fields.isEmpty()) {
			return this;
		}
		fields.forEach(field -> {
			if (field.contains(".")) {
				String[] tmp = field.split("[.]", 2);
				try {
					Field field1 = type.getDeclaredField(tmp[0]);
					if (field1 != null) {
						include(field1.getType(), tmp[1]);
					}
				} catch (Throwable e) {
				}
			} else {
				getStringListFromMap(includes, type).add(field);
			}
		});
		return this;
	}

	public RespMsg<T> exclude(Class type, Collection<String> fields) {
		if (excludes == null) {
			excludes = new HashMap<>();
		}
		if (fields == null || fields.isEmpty()) {
			return this;
		}
		fields.forEach(field -> {
			if (field.contains(".")) {
				String[] tmp = field.split("[.]", 2);
				try {
					Field field1 = type.getDeclaredField(tmp[0]);
					if (field1 != null) {
						exclude(field1.getType(), tmp[1]);
					}
				} catch (Throwable e) {
				}
			} else {
				getStringListFromMap(excludes, type).add(field);
			}
		});
		return this;
	}

	public RespMsg<T> exclude(Collection<String> fields) {
		if (excludes == null) {
			excludes = new HashMap<>();
		}
		if (fields == null || fields.isEmpty()) {
			return this;
		}
		Class type;
		if (getData() != null) {
			type = getData().getClass();
		} else {
			return this;
		}
		exclude(type, fields);
		return this;
	}

	public RespMsg<T> include(Collection<String> fields) {
		if (includes == null) {
			includes = new HashMap<>();
		}
		if (fields == null || fields.isEmpty()) {
			return this;
		}
		Class type;
		if (getData() != null) {
			type = getData().getClass();
		} else {
			return this;
		}
		include(type, fields);
		return this;
	}

	public RespMsg<T> exclude(Class type, String... fields) {
		return exclude(type, Arrays.asList(fields));
	}

	public RespMsg<T> exclude(String... fields) {
		return exclude(Arrays.asList(fields));
	}

	public RespMsg<T> include(String... fields) {
		return include(Arrays.asList(fields));
	}

	protected Set<String> getStringListFromMap(Map<Class<?>, Set<String>> map, Class type) {
		return map.computeIfAbsent(type, k -> new HashSet());
	}

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Map<Class<?>, Set<String>> getExcludes() {
		return excludes;
	}

	public Map<Class<?>, Set<String>> getIncludes() {
		return includes;
	}

	@Transient
	public boolean isSuccess() {
		return HTTP_OK.equals(this.getCode());
	}
}
