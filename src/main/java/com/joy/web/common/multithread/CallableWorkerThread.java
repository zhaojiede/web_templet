/**
 * 
 */
package com.joy.web.common.multithread;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import org.springframework.util.ReflectionUtils;

public class CallableWorkerThread implements Callable<Object> {
	private Object targetObject;
	private Method method;
	private Object[] args;
	@SuppressWarnings("unused")
	private Object proxy;

	public void setImplementor(final Object targetObject, final Object proxy, final Method method, final Object[] args) {
		this.targetObject = targetObject;
		this.method = method;
		this.args = args;
		this.proxy = proxy;

	}

	public void clean() {
		this.targetObject = null;
		this.method = null;
		this.args = null;
		this.proxy = null;
	}

	public Object call() {
		return ReflectionUtils.invokeMethod(method, targetObject, args);
	}
}
