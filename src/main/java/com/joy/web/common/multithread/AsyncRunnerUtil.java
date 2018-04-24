/**
 * 
 */
package com.joy.web.common.multithread;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
/**
 * @author shenghui
 *
 */
public class AsyncRunnerUtil {
	private static final int POOL_SIZE = 100;
	private static final Logger LOGGER = Logger.getLogger(AsyncRunnerUtil.class);
	private static final ExecutorService EXECUTORSERVICE = Executors.newFixedThreadPool(POOL_SIZE);
	private static final ReentrantLock LOCK = new ReentrantLock();
	private static SortedMap<String, CallableResult> callContainer = Collections.synchronizedSortedMap(new TreeMap<String, CallableResult>());;
	private static Thread shutdownThread = null;
	public static final Deque<CallableWorkerThread> CALLABLEWORKERTHREADDEQUEUE = new ArrayDeque<CallableWorkerThread>();

	public static void registerShutdownHook() {
		LOGGER.debug("registerShutdownHook");
		AsyncRunnerUtil.shutdownThread = new Thread("ProxyInvokerHook") {
			public void run() {
				AsyncRunnerUtil.destroy();
			}
		};
		Runtime.getRuntime().addShutdownHook(AsyncRunnerUtil.shutdownThread);
	}

	static {
		registerShutdownHook();
	}

	public static void destroy() {
		LOGGER.debug("destroy proxy Invoker Thread");
		EXECUTORSERVICE.shutdownNow();
	}

	public static String run(Object targetObject, AsyncRunnerParams param) throws IllegalArgumentException {
		String[] ids = run(targetObject, null, new AsyncRunnerParams[] {param});
		return ids == null ? null : ids[0];
	}

	@SuppressWarnings("rawtypes")
	public static String[] run(Object targetObject, String[] runnerIds, AsyncRunnerParams[] idParams) throws IllegalArgumentException {
		if (targetObject == null) {
			throw new IllegalArgumentException("Clazz is null");
		}
		String[] ids = runnerIds == null ? new String[1] : new String[runnerIds.length];
		try {
			LOCK.lock();
			Class clazz = targetObject.getClass();
			Method[] methods = clazz.getDeclaredMethods();
			if (methods != null && methods.length > 0) {
				Annotation[] annotations = null;
				String id = null, name = null;
				for (Method method : methods) {
					if (method.isAnnotationPresent(AsyncRunner.class)) {
						annotations = method.getDeclaredAnnotations();
						for (Annotation ano : annotations) {
							AsyncRunner anorun = (AsyncRunner)ano;
							id = "".equals(anorun.id()) ? UUID.randomUUID().toString() : anorun.id();
							name = method.getName();
							LOGGER.debug("asyncRunner : " + method.getName() + " [id = " + id + ", name = " + name + "]");
							if (runnerIds != null && runnerIds.length > 0) {
								int len = runnerIds.length;
								for (int i = 0; i < len; i++) {
									String rid = runnerIds[i];
									LOGGER.debug("annotation id : " + id + "; need aync run id : " + rid);
									if (id.equalsIgnoreCase(rid)) {
										ids[i] = id;
										setRunnerThread(id, name, method, targetObject, idParams[i].get());
									}
								}
							} else {
								ids[0] = id;
								setRunnerThread(id, name, method, targetObject, idParams[0].get());
							}
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("", e);
		} finally {
			StringBuilder sb = new StringBuilder();
			for (String id : ids) {
				sb.append(id).append(", ");
			}
			LOGGER.debug("");
			LOGGER.debug("*********************************************");
			LOGGER.debug(" id : [" + sb.toString() + "]");
			LOGGER.debug("*********************************************");
			LOGGER.debug("");
			LOCK.unlock();
		}

		return ids;
	}

	public static Object get(String id) {
		if (id == null) {
			return null;
		}
		Object obj = null;
		LOCK.lock();
		CallableWorkerThread callableWorkerThread = null;
		try {
			CallableResult worker = callContainer.get(id);
			if (worker != null) {
				Future<Object> returnObject = (Future<Object>)worker.getFuture();
				callableWorkerThread = worker.getCallableWorkerThread();
				obj = returnObject == null ? null : returnObject.get();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} finally {
			callContainer.remove(id);
			if (callableWorkerThread != null) {
				callableWorkerThread.clean();
				synchronized (AsyncRunnerUtil.CALLABLEWORKERTHREADDEQUEUE) {
					if (AsyncRunnerUtil.CALLABLEWORKERTHREADDEQUEUE.size() < POOL_SIZE) {
						AsyncRunnerUtil.CALLABLEWORKERTHREADDEQUEUE.add(callableWorkerThread);
					}
				}
			}
			LOCK.unlock();
		}

		return obj;
	}

	private static void setRunnerThread(String id, String name, final Method method, final Object targetObject,
		final Object... params) {
		try {
			CallableWorkerThread callableWorkerThread = null;
			synchronized ( AsyncRunnerUtil.CALLABLEWORKERTHREADDEQUEUE ) {
				callableWorkerThread = CALLABLEWORKERTHREADDEQUEUE.poll(); 
			}
			if ( callableWorkerThread == null ) {
				callableWorkerThread = new CallableWorkerThread();
			}
			callableWorkerThread.setImplementor( targetObject, null, method, params );

			
			Future<Object> returnObject = EXECUTORSERVICE.submit(callableWorkerThread);
			CallableResult asyncInvoker = new CallableResult( returnObject, callableWorkerThread );
			callContainer.put(id, asyncInvoker);
		} catch (Exception ex) {
			LOGGER.error("", ex);
		} finally {

		}
	}

	public static SortedMap<String, CallableResult> getFutureContainer() {
		return callContainer;
	}
}
