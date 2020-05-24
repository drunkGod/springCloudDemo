package com.jvxb.common.exception;

/**
 * 业务异常封装，msg会返回到前端
 * 
 * @author warrior
 *
 * @Date 2018年11月30日
 */
public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 2439358964127843952L;

	/**
	 * 通用异常code
	 */
	private Integer code = 999;

	public ServiceException(String msg) {
		super(msg);
	}

	public ServiceException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public ServiceException(Integer code, String msg) {
		super(msg);
		this.code = code;
	}

	public ServiceException(Integer code, String msg, Throwable cause) {
		super(msg, cause);
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

}
