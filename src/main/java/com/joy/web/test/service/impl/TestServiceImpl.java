package com.joy.web.test.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.joy.web.test.dao.TestDao;
import com.joy.web.test.service.TestService;

@Service("testService")
public class TestServiceImpl implements TestService{
	
	@Autowired 
	private TestDao testDao;

	@Override
	public int getTestListCnt(Map<String, Object> param) throws Exception {
		return testDao.getTestListCnt(param);
	}

}
