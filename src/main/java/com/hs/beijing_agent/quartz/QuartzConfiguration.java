package com.hs.beijing_agent.quartz;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;


/**
 * quartz自动配置类,因为我们需要数据库配置任务,所以自动生成detail、trigger的bean不注入
 * basic执行方式3种:
 * 1.每隔n秒 @Scheduled(fixedRate = 5000)
 * 2.延迟n秒 @Scheduled(fixedDelay = 5000)
 * 3.表达式  @Scheduled(cron = "*\/5 * * * * *")
 * Note:
 * 异步 @Async 使用注意:
 * 1.static方法不能异步
 * 2.内部调用不能异步
 * 3.重复扫描不能异步
 * Created by zeyuphoenix on 16/6/20.
 */
@Configuration
@EnableScheduling         // 启用定时任务,默认为basic,使用在方法上加入注解方式实现
@EnableAsync              // 开启异步调用管理
public class QuartzConfiguration {

    // ================================================================
    // Constants
    // ================================================================

    /**
     * logger
     */
    private static final Logger logger = LoggerFactory.getLogger(QuartzConfiguration.class);

    // ================================================================
    // Fields
    // ================================================================

    // ================================================================
    // Public or Protected Methods
    // ================================================================

    @PostConstruct
    public void init() {

        logger.info("QuartzConfig initialized.");
    }

    // JobDetailFactoryBean
    // CronTriggerFactoryBean

    /**
     * quartz持久化对象构建
     */
    @Bean(name = "quartzScheduler")
    public SchedulerFactoryBean quartzScheduler() {
        SchedulerFactoryBean quartzScheduler = new SchedulerFactoryBean();

        quartzScheduler.setSchedulerName("quartz-scheduler");
        quartzScheduler.setAutoStartup(true);

        //QuartzScheduler 延时启动，应用启动完20秒后 QuartzScheduler 再启动
        quartzScheduler.setStartupDelay(10);
        //System.out.println("quartzScheduler is starting ");
        //1-定时请求要推送给海关的订单
        CronTriggerFactoryBean triggerBeanOfGetPushOrderList = triggerBeanOfGetPushOrderList();
        //2-定时获取海关返回的信息，并传给业务后台
        CronTriggerFactoryBean triggerBeanOfGetPushOrderResponse = triggerBeanOfGetPushOrderResponse();
       quartzScheduler.setTriggers(
               //triggerBeanOfGetPushOrderList.getObject(),
               triggerBeanOfGetPushOrderResponse.getObject()
               );

        return quartzScheduler;
    }


    //1-定时请求要推送给海关的订单
    @Bean
    public CronTriggerFactoryBean triggerBeanOfGetPushOrderList() {
        CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
        bean.setJobDetail(jobDetailOfGetPushOrderList().getObject());
        bean.setCronExpression("0/10 * * * * ?");    //每分钟执行一次
        return bean;
    }
    @Bean
    public MethodInvokingJobDetailFactoryBean jobDetailOfGetPushOrderList(){
        MethodInvokingJobDetailFactoryBean bean = new MethodInvokingJobDetailFactoryBean();
        bean.setTargetObject(updateJob());
        bean.setTargetMethod("getPushOrderList");

        return bean;
    }
    //2-定时获取海关返回的信息，并传给业务后台
    @Bean
    public CronTriggerFactoryBean triggerBeanOfGetPushOrderResponse() {
        CronTriggerFactoryBean bean = new CronTriggerFactoryBean();
        bean.setJobDetail(jobDetailOfGetPushOrderResponse().getObject());
        bean.setCronExpression("5/10 * * * * ?");    //每分钟执行一次
        return bean;
    }
    @Bean
    public MethodInvokingJobDetailFactoryBean jobDetailOfGetPushOrderResponse(){
        MethodInvokingJobDetailFactoryBean bean = new MethodInvokingJobDetailFactoryBean();
        bean.setTargetObject(updateJob());
        bean.setTargetMethod("getPushOrderResponse");

        return bean;
    }


    @Bean
	public Object updateJob(){
        QuartzJob job = new QuartzJob();
		return job;
	}
	

}
