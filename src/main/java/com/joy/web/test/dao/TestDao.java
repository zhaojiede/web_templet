package com.joy.web.test.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository("testDao")
public interface TestDao {

	public int getTestListCnt(Map<String, Object> param) throws Exception;

}
