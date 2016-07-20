package org.jview.basedata.mybatis;

import junit.framework.TestCase;

import org.junit.Test;
import org.jview.fwork.basedata.model.LogMsgDetailPO;
import org.jview.fwork.basedata.service.ILogMsgDetailService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ai.yc.common.core.page.PageVO;

public class TestLogMsgDetailService  extends TestCase{
	private static  ILogMsgDetailService logMsgDetailService;
	private static ApplicationContext act;
	static{
//		SqlHelper.addIgnore(LogMsgDetailPO.class, "name,version,currentUserId");
		System.out.println("--------start-----");
		act = new ClassPathXmlApplicationContext(new String[]{"spring/spring-context-test.xml"});
		logMsgDetailService = (ILogMsgDetailService)act.getBean("logMsgDetailService");
	}
	
//	@Test
//	public void testFindLookupById(){
//		LogMsgDetailPO lookup = logMsgService.findLookupById(1l);
//		System.out.println(lookup);
//	}
	
	@Test
	public void testFindPageByLogMsgDetail(){
		LogMsgDetailPO search = new LogMsgDetailPO();
//		search.setNameCn("test");
		
		PageVO<LogMsgDetailPO> page = new PageVO<LogMsgDetailPO>(1, 10);
		PageVO<LogMsgDetailPO> pages=logMsgDetailService.findPageByLogMsgDetail(search, page);
		System.out.println(pages.getTotal());
		 for(LogMsgDetailPO lp:pages.getDatas()){
	        	System.out.println("cid="+lp.getCid()+" runTime="+lp.getRunTime()+" tos="+lp.getTos());
	        }
	}
	
	@Test
	public void testCreate(){
		LogMsgDetailPO msg=new LogMsgDetailPO();
		msg.setLogMsgId(1l);
		msg.setTos("jview@139.com");
		msg.setRunTime(10l);
		this.logMsgDetailService.create(msg);
	}
	

}
