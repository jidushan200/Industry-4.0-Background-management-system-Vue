package org.tsinghuatj.support.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OperateLog {

    /**
     * 日志记录信息
     * 
     * @return
     */
    String info();

    /**
     * 从请求参数时获取变量值
     * 
     * @return
     */
    String[] params() default {};
}
