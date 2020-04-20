package com.njit.framework.client;

/**
 * RabbitMQ交换机和队列列表
 * @author dustdawn
 * @date 2020/3/20 11:38
 */
public class RabbitMQList {
    /**
     * 交换机列表
     */
    /**媒资处理相关交换机*/
    public static final String EXCHANGES_MEDIA_PROCESSOR = "exchanges_media_processor";
    /**cms页面发布相关交换机*/
    public static final String EXCHANGES_CMS_POSTPAGE = "exchanges_cms_postpage";

    /**添加选课任务交换机*/
    public static final String EXCHANGES_LEARNING_ADDCHOOSECOURSE = "exchanges_learning_addchoosecourse";





    /**
     * 队列列表
     */
    /**媒资处理相关队列*/
    public static final String QUEUE_MEDIA_PROCESSOR = "queue_media_processor";

    /**添加选课消息队列*/
    public static final String QUEUE_LEARNING_ADDCHOOSECOURSE = "queue_learning_addchoosecourse";
    /**完成添加选课消息队列*/
    public static final String QUEUE_LEARNING_FINISHADDCHOOSECOURSE = "queue_learning_finishaddchoosecourse";
}
