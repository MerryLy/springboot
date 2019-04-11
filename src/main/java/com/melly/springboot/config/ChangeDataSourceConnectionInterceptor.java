//package com.melly.springboot.config;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.Ordered;
//import org.springframework.stereotype.Component;
//
//
//@Aspect
//@Component
//public class ChangeDataSourceConnectionInterceptor implements Ordered {
//	private static Logger log = LoggerFactory.getLogger(ChangeDataSourceConnectionInterceptor.class);
//
//	@Around("execution(* com.melly.springboot.mapper.*.*(..))")
//    public Object proceed(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        try {
//            log.info("set database connection to read only");
//            DataSourceContextHolder.read();
//            return proceedingJoinPoint.proceed();
//        } finally {
//        	DataSourceContextHolder.clearDbType();
//        	log.info("restore database connection");
//        }
//    }
//
//	@Override
//	public int getOrder() {
//		return 0;
//	}
//}
//
