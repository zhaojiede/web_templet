/**
 * 
 */
package com.joy.web.common.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

public class BaseAction {
	protected Log logger = LogFactory.getLog( BaseAction.class );
	
	protected HttpServletRequest getRequest() {
		return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
	}

	public ModelAndView getModelAndView() {
		ModelAndView modelAndView = new ModelAndView();
		
		return modelAndView;
	}
}
