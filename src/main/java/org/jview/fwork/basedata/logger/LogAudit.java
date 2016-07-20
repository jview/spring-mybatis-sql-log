package org.jview.fwork.basedata.logger;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface LogAudit {
	public String noLogReturn="noLogReturn";//不记录返回值
	public String author() default "no";
	
	public String title();

	public String descs() default "";
	
	public String calls() default "";
}
