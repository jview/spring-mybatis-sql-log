package org.jview.fwork.schedule;

import javax.mail.internet.MimeMessage;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import junit.framework.TestCase;

public class TestSendMail extends TestCase{
	private static  JavaMailSender mailSender;
	private static ApplicationContext act;
	public static boolean isSend=false;
	
	static{
//		SqlHelper.addIgnore(OptDataPO.class, "name,version,currentUserId");
		System.out.println("--------start-----");
		if(isSend){
			act = new ClassPathXmlApplicationContext(new String[]{"spring/spring-context.xml"});
	//		optDataService = (IOptDataService)act.getBean("optDataService");
			mailSender = (JavaMailSender) act.getBean("mailSender");  
			System.out.println("--------load end-----");
		}
	}
	
	/**
	 * 只能用来发送text格式的邮件
	 */
	@Test
	public void testSendMailTxt(){
		if(!isSend){
			return;
		}
		SimpleMailMessage mail = new SimpleMailMessage(); // <span style="color:
															// #ff0000;">注意SimpleMailMessage只能用来发送text格式的邮件</span>

		try {
			mail.setTo("jinhe.chen@yc.ai");// 接受者
			mail.setFrom("ycstest@126.com");// 发送者,这里还可以另起Email别名，不用和xml里的username一致
			mail.setSubject("spring mail test!");// 主题
			mail.setText("springMail的简单发送测试");// 邮件内容
			mailSender.send(mail);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送html邮件
	 */
	@Test
	public void testSendMailHtml() throws Exception{
		if(!isSend){
			return;
		}
		// 获取JavaMailSender bean
		JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
		MimeMessage mailMessage = senderImpl.createMimeMessage();
		// 设置utf-8或GBK编码，否则邮件会有乱码
		MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "utf-8");
		try {
			messageHelper.setTo("jinhe.chen@yc.ai");// 接受者
			messageHelper.setFrom("ycstest@126.com");// 发送者
			messageHelper.setSubject("测试邮件");// 主题
			// 邮件内容，注意加参数true，表示启用html格式
			messageHelper.setText("<html><head></head><body><h1>hello!!chao.wang</h1></body></html>", true);
			mailSender.send(mailMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 发送html邮件并含附件
	 */
	@Test
	public void testSendMailHtmlAttach()throws Exception{
		if(!isSend){
			return;
		}
		// 获取JavaMailSender bean
		JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
		MimeMessage mailMessage = senderImpl.createMimeMessage();
		// 设置utf-8或GBK编码，否则邮件会有乱码
		MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "utf-8");
		try {
			messageHelper.setTo("jinhe.chen@yc.ai");// 接受者
			messageHelper.setFrom("ycstest@126.com");// 发送者
			messageHelper.setSubject("测试邮件附件");// 主题
			// 邮件内容，注意加参数true
			messageHelper.setText("<html><head></head><body><h1>hello!!chao.wang</h1></body></html>", true);
			// 附件内容
//			messageHelper.addInline("a", new File("E:/xiezi.jpg"));
//			messageHelper.addInline("b", new File("E:/logo.png"));
//			File file = new File("E:/edu_demo测试.sql");
			// 这里的方法调用和插入图片是不同的，使用MimeUtility.encodeWord()来解决附件名称的中文问题
//			messageHelper.addAttachment(MimeUtility.encodeWord(file.getName()), file);
			mailSender.send(mailMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
