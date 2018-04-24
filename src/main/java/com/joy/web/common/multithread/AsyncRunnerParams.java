/**
 * 
 */
package com.joy.web.common.multithread;

/**
 * @author shenghui
 *
 */
public class AsyncRunnerParams {
	private Object[] parmsArry;

	public AsyncRunnerParams() {
	}

	public AsyncRunnerParams(Object... objects) {
		this.parmsArry = objects;
	}

	public void set(Object... objects) {
		if (objects != null) {
			this.parmsArry = objects;
		}
	}

	public Object[] get() {
		return this.parmsArry;
	}

	public String toString() {
		if (this.parmsArry == null) {
			return "";
		} else {
			StringBuilder sb = new StringBuilder();
			for (Object obj : this.parmsArry) {
				sb.append(obj).append("; ");
			}
			return sb.toString();
		}
	}
}
