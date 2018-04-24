package com.joy.web.login.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.joy.web.common.action.BaseAction;

/**
 * @ClassName: TestAction
 * @Description: TODO
 * @author: yjzhao
 * @date: 2018年3月30日 下午4:32:32
 */	
@Controller
public class LoginAction extends BaseAction  {
	
	@Autowired 
	
	@RequestMapping(value = "/login.joy")
	public ModelAndView login() throws Exception{
//		testService.getTestListCnt(param);
		ModelAndView mav = super.getModelAndView();
		mav.setViewName("login/login");
		return mav;
	}

}
