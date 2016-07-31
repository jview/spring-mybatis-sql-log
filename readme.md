1，schedule准实时高性能处理框架
=========================================
主要是分两个任务来处理，一个用来载入数据到待处理MAP，另一个用来监测待处理MAP，有就处理，没有就空着。  
并提供 一个待处理MAP更新接口，使在消息保存后可以调一下这个接口IScheduleToDoService.addToDo，以便于马上处理这个消息。      
**重置待发邮件数据：** update msgs.tf_log_msg set send_time=now(),send_flag=0   
**运行的代码是：** ScheduleProvider.java   
**注：**代码是从dubbo服务中提取出来，会有点dubbo相关的文件，必要的依赖已去掉。    
**数据脚本** /src/test/sql/msgs.sql



2，java接口调用链分析
=========================================
功能实现从调用开始，可以是从控制层->业务层->dao层及数据库执行的全过程接口调用关系的日志与分析。  
对接口服务进行日志，日志时采用异步线程池，以便于不影响正常功能的性能，且就算日志失败或异常都不会影响原有功能的使用。  
**@LogService** 服务接口通过注解进行处理，支持author，以便于分析时，发现是谁提供的接口有问题。  
**sql操作** 对应mapper的所有数据查询，不用加注解，全部支持自动日志到数据库，可通过logInfo,logWarn配置对应的执行时间，如果想减少sql操作的日志量，可以把这两个值设大一些。    
然后通过查询的日志及对应的线程ID，可以知道是否是同一个线程的记录，以分析一个线程下所有接口及服务的日志。   
**运行的代码是：** TestLogMsgService的testFindPageByLogMsg方法    
**查看效果：** select * from v_log_db where class_name like 'LogMsg%' order by cid desc   
**sql的日志的sql语句，**可以拿出来在数据库直接执行。   

一般情况下，可以忽略select_count, select_seq的查询，以减少日志量，可以修改系统配置参数sysconfigs(spring-context.xml)的控制参数  


使用说明：
========================================
1，	安装mysql数据库，略   
2，	创建数据库msgs   
3，	导入/src/test/sql/msgs.sql的table及数据   
4，	修改jdbc.properties对应的数据库信息   
5，	运行对应的测试。   
6，	查询日志数据：select * from v_log_db where class_name like 'LogMsg%' order by cid desc    
