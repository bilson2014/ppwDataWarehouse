package com.paipianwang.SmartReport.web.test;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.paipianwang.SmartReport.service.ReportingService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class MetaColumnTest {
	
	@Resource
	private ReportingService dao ;

	@Test
	public void test1() {
		System.err.println(dao);
	}
}
