package com.jvxb.modules.configuration.err;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jvxb.common.web.RespMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常控制器：访问接口异常返回特定信息。
 * @author jvxb
 * @since 2019-09-10
 */
@Component
public class BaseErrorController implements ErrorController {

	Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 错误页面的路径
	 */
	@Override
	public String getErrorPath() {
		return "/error";
	}

	/**
	 * @Description : 处理错误请求，返回Json数据
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/error")
	@ResponseBody
	public Object handleError(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("statusCode", request.getAttribute("javax.servlet.error.status_code"));
		resultMap.put("url", request.getAttribute("javax.servlet.error.request_uri"));
		resultMap.put("ex", request.getAttribute("javax.servlet.error.exception"));
		logger.error(resultMap.toString());
		return RespMsg.error("服务器繁忙，请稍后重试！");
	}
}
