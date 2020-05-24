package com.jvxb.modules.configuration.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//@Component
//@Aspect
public class MyAop {

    // 警告时间：方法执行超过5秒
    long warningThreshold = 5000;
    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 切点位置：使用 ResponseBody和 RestController的方法
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.ResponseBody)" +
            " || @annotation(org.springframework.web.bind.annotation.RestController)" +
            " || @within(org.springframework.web.bind.annotation.RestController)")
    public void methodExecuteAspect() {
    }

    @Around("methodExecuteAspect()")
    public Object timeSpentOfMethod(ProceedingJoinPoint pjp) throws Throwable {

        // 切点方法执行前的时间
        long start = System.currentTimeMillis();
        // 执行方法
        Object retVal = pjp.proceed();
        // 切点方法执行所花的时间
        long duration = System.currentTimeMillis() - start;
        // 记录
        String operateClassName = pjp.getTarget().getClass().getName();
        String operateMethodName = pjp.getSignature().getName();
        String operateArgs = "";
        Object[] argsArr = pjp.getArgs();
        for (Object arg : argsArr) {
            if (arg == null) {
                continue;
            }
            operateArgs += arg.toString() + ",";
        }

        if (duration > warningThreshold) {
            // 记录到专门的日志, 以供优化
            logger.warn(String.format("[%s#%s] " + "方法执行时间为  %d(ms), 超过 %d ms, 需优化。 args is [%s]",
                    operateClassName, operateMethodName, duration, warningThreshold, operateArgs));
        } else {
//            logger.warn(String.format("[%s#%s()] " + "方法执行时间为  %d(ms), 未超过 %d ms, 不需优化。 args is [%s]",
//                    operateClassName, operateMethodName, duration, warningThreshold, operateArgs));
        }
        return retVal;
    }

    /**
     * 方法执行出现未捕获的异常，可以记录到error日志，并插入数据库。     */
//    @AfterThrowing(throwing = "ex", pointcut = "methodExecuteAspect()")
//    public void exceptionOfMethod(Throwable ex) throws Throwable {
//        StackTraceElement stackTraceElement = ex.getStackTrace()[0];
//        String errClassName = stackTraceElement.getClassName();
//        String errMethodName = stackTraceElement.getMethodName();
//        int errLine = stackTraceElement.getLineNumber();
//        String errMsg = ex.getMessage();
//        // 记录到专门的error日志, 以供查找原因
//        logger.error(String.format("[%s#%s()] " + " 方法执行异常。line is [%d], errMsg is [%s]", errClassName,
//                errMethodName, errLine, errMsg));
//    }

}
