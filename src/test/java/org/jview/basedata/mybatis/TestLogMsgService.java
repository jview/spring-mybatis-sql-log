package org.jview.basedata.mybatis;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.jview.fwork.basedata.entity.MailVO;
import org.jview.fwork.basedata.model.LogMsgPO;
import org.jview.fwork.basedata.service.ILogMsgService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ai.yc.common.core.data.RetResult;
import ai.yc.common.core.logger.ILogMsgManager;
import ai.yc.common.core.page.PageVO;



public class TestLogMsgService extends TestCase{
	private static  ILogMsgService logMsgService;
	private static  ILogMsgManager logMsgManager;
	private static ApplicationContext act;
	static{
//		SqlHelper.addIgnore(LogMsgPO.class, "name,version,currentUserId");
		System.out.println("--------start-----");
		act = new ClassPathXmlApplicationContext(new String[]{"spring/spring-context-test.xml"});
		logMsgService = (ILogMsgService)act.getBean("logMsgService");
		logMsgManager = (ILogMsgManager)act.getBean("logMsgManager");
	}
	
//	@Test
//	public void testFindLookupById(){
//		LogMsgPO lookup = logMsgService.findLookupById(1l);
//		System.out.println(lookup);
//	}
	
	@Test
	public void testFindPageByLogMsg() throws Exception{
		LogMsgPO search = new LogMsgPO();
//		search.setNameCn("test");
		
		PageVO<LogMsgPO> page = new PageVO<LogMsgPO>(1, 10);
		PageVO<LogMsgPO> pages=logMsgService.findPageByLogMsg(search, page);
		System.out.println(pages.getTotal());
		 for(LogMsgPO lp:pages.getDatas()){
	        	System.out.println("cid="+lp.getCid()+" title="+lp.getTitle()+" tos="+lp.getTos());
	        }
		 
		 Thread.sleep(500l);//暂停0.5s,以便于异步任务处理完成
	}
	
	@Test
	public void testFindLogMsgList(){
		Date date = new Date();
		date=DateUtils.addHours(date, -1);
		
		RetResult<LogMsgPO> ret=logMsgService.findLogMsgList(date, 2, 0);
		System.out.println("flag="+ret.getFlag()+" size="+ret.getSize());
		 for(LogMsgPO lp:ret.getDataList()){
	        	System.out.println("cid="+lp.getCid()+" title="+lp.getTitle()+" tos="+lp.getTos());
	        }
	}
	
	
	@Test
	public void testSaveMail(){
		MailVO mail=new MailVO();
		mail.setTos("test");
		mail.setTitle("test");
		mail.setContent("test");
		this.logMsgManager.saveMail("test", mail, 1, false);
	}
	
	@Test
	public void testSaveMailAsync(){
		MailVO mail=new MailVO();
		mail.setTos("test");
		mail.setTitle("test");
		mail.setContent("test");
		this.logMsgManager.saveMail("test", mail, 1, true);
	}
	
	@Test
	public void testDate(){
		Date date = new Date();
		date=DateUtils.addHours(date, -1);
		System.out.println(date);
	}
	
//	@Test
	public void te1stCreateBatch(){
		List<LogMsgPO> list=new ArrayList();
		LogMsgPO log = new LogMsgPO();
		log.setMsgType(0);
		log.setMsgCode("test");
		log.setTitle("test");
		log.setSendType(1);
		log.setContent("test");
		list.add(log);
		log = new LogMsgPO();
		log.setMsgType(0);
		log.setMsgCode("test");
		log.setTitle("test1");
		log.setSendType(1);
		log.setContent("test1");
		list.add(log);
		log = new LogMsgPO();
		log.setMsgType(0);
		log.setMsgCode("test");
		log.setTitle("test2");
		log.setSendType(1);
		log.setContent("test2");
		list.add(log);
		try {
			this.logMsgService.insertBatch(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean checkEmail(String email){
        boolean flag = false;
        try{
                String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(email);
                flag = matcher.matches();
            }catch(Exception e){
                flag = false;
            }
        return flag;
    }
	
	@Test
	public void testCheckMail(){
		String mail="test@126.com";
		boolean flag=this.checkEmail(mail);
		System.out.println("----mail="+mail+" flag="+flag);
		
		mail="test@ac.com";
		flag=this.checkEmail(mail);
		System.out.println("----mail="+mail+" flag="+flag);
		
		mail="test";
		flag=this.checkEmail(mail);
		System.out.println("----mail="+mail+" flag="+flag);
	}
	
	@Test
	public void testCreate(){
		MailVO mail=null;
		
		Integer sendType=1;//一起发送
		for(int i=0; i<10; i++){
			mail=new MailVO();
			mail.setTos("ycstest@126.com");
			mail.setTitle("test");
			mail.setContent("test");
			this.logMsgManager.saveMail("test", mail, 1, false);
		}
		
		sendType=2;//一起发送
		for(int i=0; i<10; i++){
			mail=new MailVO();
			mail.setTos("ycstest@126.com");
			mail.setTitle("test");
			mail.setContent("test");
			this.logMsgManager.saveMail("test", mail, 2, false);
		}
		
		
	}
	
//	@Test
//	public void testSelectByLookup(){
//		LogMsgPO search = new LogMsgPO();
//		List<LogMsgPO> list=logMsgService.selectByLogMsg(search, 1, 10);
//		System.out.println(list.size());
//		for(LogMsgPO lp:list){
//        	System.out.println("cid="+lp.getCid()+" code="+lp.getCode()+" nameCn="+lp.getNameCn());
//        }
//	}

}
