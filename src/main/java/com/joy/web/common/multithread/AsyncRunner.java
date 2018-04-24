/**
 * 
 */
package com.joy.web.common.multithread;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author shenghui
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME) 
public @interface AsyncRunner { 
	String id() default ""; 
	String name() default "asyncRunner";
}
