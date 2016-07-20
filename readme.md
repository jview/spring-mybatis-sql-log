1，schedule准实时高性能处理框架
=========================================
主要是分两个任务来处理，一个用来载入数据到待处理MAP，另一个用来监测待处理MAP，有就处理，没有就空着。
重置待发邮件数据：update msgs.tf_log_msg set send_time=now(),send_flag=0
运行的代码是：DubboScheduleProvider.java



2，java接口调用链分析
=========================================
通过注解@LogService，用拦截器，对接口服务进行日志，日志时采用异步线程池，以便于不影响正常功能的性能。
同时支持所有sql操作的日志
然后通过查询的日志及对应的线程ID，可以知道是否是同一个线程的记录，以分析一个线程下所有接口及服务的日志。
运行的代码是：TestLogMsgService的testFindPageByLogMsg方法
查看效果：select * from v_log_db where class_name like 'LogMsg%' order by cid desc
sql的日志的sql语句，可以拿出来在数据库直接执行。

一般情况下，可以忽略select_count, select_seq的查询，以减少日志量，可以修改Sysconfigs.getEnvMap()的控制参数
