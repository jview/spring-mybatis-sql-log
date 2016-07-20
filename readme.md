1，schedule准实时高性能处理框架
=========================================
##主要是分两个任务来处理，一个用来载入数据到待处理MAP，另一个用来监测待处理MAP，有就处理，没有就空着。 
##重置待发邮件数据：update msgs.tf_log_msg set send_time=now(),send_flag=0 
**运行的代码是**：DubboScheduleProvider.java 
##注：代码是从dubbo服务中提取出来，会有点dubbo相关的文件，必要的依赖已去掉。 



2，java接口调用链分析
=========================================
#对接口服务进行日志，日志时采用异步线程池，以便于不影响正常功能的性能，且就算日志失败或异常都不会影响原有功能的使用。
	*服务接口通过注解@LogService进行处理。
	*sql操作对应mapper的所有数据查询，不用加注解，全部支持自动日志到数据库，可通过logInfo,logWarn配置对应的执行时间，如果想减少sql操作的日志量，可以把这两个值设大一些。  
##然后通过查询的日志及对应的线程ID，可以知道是否是同一个线程的记录，以分析一个线程下所有接口及服务的日志。 
##运行的代码是：TestLogMsgService的testFindPageByLogMsg方法  
##查看效果：select * from v_log_db where class_name like 'LogMsg%' order by cid desc 
##sql的日志的sql语句，可以拿出来在数据库直接执行。 

##一般情况下，可以忽略select_count, select_seq的查询，以减少日志量，可以修改Sysconfigs.getEnvMap()的控制参数
