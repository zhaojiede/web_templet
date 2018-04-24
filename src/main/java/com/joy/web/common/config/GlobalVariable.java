/**
 * 
 */
package com.joy.web.common.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author shenghui
 *
 */
public class GlobalVariable {
	protected static ApplicationContext ctx = null;
	static {
		try {
			String[] configLocations = {"applicationContext-config.xml"};
			ctx = new ClassPathXmlApplicationContext(configLocations);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected static PropertyConfig getPropertyConfig() {
		return (PropertyConfig) ctx.getBean("hljGlobalConfig");
	}
}
