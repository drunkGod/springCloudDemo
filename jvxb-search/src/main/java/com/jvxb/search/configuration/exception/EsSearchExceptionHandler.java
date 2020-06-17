package com.jvxb.search.configuration.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jvxb
 * @since 2020-04-13
 */
@ControllerAdvice
@Slf4j
public class EsSearchExceptionHandler {

    @ResponseBody
    @ExceptionHandler(EsSearchException.class)
    public Map<String, Object> handleException(EsSearchException e) {
        log.error(getStackTrace(e));
        Map<String, Object> map = new HashMap<>();
        map.put("code", e.getCode());
        map.put("message", e.getMessage());
        return map;
    }

    public String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            throwable.printStackTrace(pw);
            String allStackTrace = sw.toString();
            String[] stackTraceArr = allStackTrace.split("\r\n");
            StringBuffer errInfo = new StringBuffer();
            errInfo.append(stackTraceArr[0]).append("\r\n").append(stackTraceArr[1]);
            return errInfo.toString();
        } finally {
            pw.close();
        }
    }

}