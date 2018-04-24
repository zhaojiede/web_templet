package com.joy.web.test.action;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.joy.web.common.action.BaseAction;
import com.joy.web.test.service.TestService;

/**
 * @ClassName: TestAction
 * @Description: TODO
 * @author: yjzhao
 * @date: 2018年3月30日 下午4:32:32
 */
@Controller
public class TestAction extends BaseAction  {
	
	@Autowired 
	private TestService testService;
	
	@RequestMapping(value = "/main.joy")
	public ModelAndView testList() throws Exception{
//		testService.getTestListCnt(param);
		ModelAndView mav = super.getModelAndView();
		mav.setViewName("main/main");
		return mav;
	}

}
