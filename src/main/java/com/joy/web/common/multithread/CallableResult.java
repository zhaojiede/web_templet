/**
 * 
 */
package com.joy.web.common.multithread;


import java.util.concurrent.Future;

public class CallableResult <T> {
	Future<T> future;
	CallableWorkerThread callableWorkerThread;
	
	public CallableResult(final Future<T> future, final CallableWorkerThread callableWorkerThread) {
		this.future = future;
		this.callableWorkerThread = callableWorkerThread;
	}

	public Future<?> getFuture() {
		return future;
	}

	public void setFuture(Future<T> future) {
		this.future = future;
	}

	public CallableWorkerThread getCallableWorkerThread() {
		return callableWorkerThread;
	}

	public void setCallableWorkerThread(CallableWorkerThread callableWorkerThread) {
		this.callableWorkerThread = callableWorkerThread;
	}
	
	
}
