package org.tsinghuatj.framework.web.support.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Secure {
	/**
	 * 拥有全部的权限
	 * 
	 * @return
	 */
	String[] has() default {};

	/**
	 * 拥有任意的权限
	 * 
	 * @return
	 */
	String[] any() default {};

	/**
	 * 不拥有其中的权限
	 * 
	 * @return
	 */
	String[] not() default {};
}
