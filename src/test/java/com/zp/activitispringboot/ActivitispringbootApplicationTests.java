package com.zp.activitispringboot;

import com.zp.activitispringboot.config.SecurityUtil;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.runtime.TaskRuntime;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
class ActivitispringbootApplicationTests {


    private Logger logger = LoggerFactory.getLogger(ActivitispringbootApplicationTests.class);

    /**
     * 流程定义相关操作
     */
    @Autowired
    private ProcessRuntime processRuntime;

    /**
     * 任务相关操作
     */
    @Autowired
    private TaskRuntime taskRuntime;

    /**
     * security工具类
     */
    @Autowired
    private SecurityUtil securityUtil;

    /**
     * 查看流程定义
     */
    @Test
    void testDefinition() {
        securityUtil.logInAs("f1");
        Page processDefinitionPage = processRuntime
                .processDefinitions(Pageable.of(0, 10));
        logger.info("> Available Process definitions: " +
                processDefinitionPage.getTotalItems());
        for (Object pd : processDefinitionPage.getContent()) {
            logger.info("\t > Process definition: " + pd);
        }
    }

    /**
     * 流程实例启动
     */
    @Test
    void testStartInstance(){
        securityUtil.logInAs("f1");
        String key = "springboot01";
        String content = "test instance";
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
        logger.info("> Processing content: " + content
                + " at " + formatter.format(new Date()));
        ProcessInstance processInstance = processRuntime
                .start(ProcessPayloadBuilder
                        .start()
                        .withProcessDefinitionKey(key)
//                        .withProcessInstanceName("Processing Content: " + content)
//                        .withName("Processing Content: " + content)
                        .withVariable("content", content)
                        .build());
        logger.info(">>> Created Process Instance: " + processInstance);
    }

}
