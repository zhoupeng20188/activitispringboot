package com.zp.activitispringboot.controller;

import com.zp.activitispringboot.dto.MyTaskDto;
import com.zp.activitispringboot.dto.ProcessInstanceDto;
import com.zp.activitispringboot.utils.ActivitiUtil;
import com.zp.activitispringboot.utils.SecurityUtil;
import org.activiti.api.process.model.ProcessDefinition;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.runtime.TaskRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.Objects;

@RestController
public class ControllerTest {


    private Logger logger = LoggerFactory.getLogger(ControllerTest.class);

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

    @RequestMapping("/process/definition/list")
    public Object testDefinition() {
//        securityUtil.logInAs("f1");
        Page processDefinitionPage = processRuntime
                .processDefinitions(Pageable.of(0, 100));
        logger.info("> Available Process definitions: " +
                processDefinitionPage.getTotalItems());
        for (Object pd : processDefinitionPage.getContent()) {
            logger.info("\t > Process definition: " + pd);
        }

        return processDefinitionPage;
    }

    @RequestMapping("/process/definition/get")
    public Object testDefinitionGet(@RequestParam String processDefinitionId) {
//        securityUtil.logInAs("f1");
        ProcessDefinition processDefinition = processRuntime
                .processDefinition(processDefinitionId);

        return processDefinition;
    }

    @RequestMapping("/process/instance/get")
    public Object testInstanceGet(@RequestParam String processInstanceId) {
//        securityUtil.logInAs("f1");
        ProcessInstance processInstance = processRuntime
                .processInstance(processInstanceId);

        return processInstance;
    }

    @RequestMapping("/process/instance/start")
    public Object testInstance(@RequestBody ProcessInstanceDto dto) {
        ProcessInstance processInstance = null;
        if(Objects.isNull(dto.getVariables())){

            processInstance =ActivitiUtil.startProcessInstance(
                    "zhangsan",dto.getProcessDefinitionKey(), dto.getProcessInstanceName(),
                    dto.getBusinessKey());
        }

        return processInstance;
    }

    @RequestMapping("/test")
    public Object test(@RequestParam String a){
        System.out.println(a);
        MyTaskDto myTaskDto = new MyTaskDto();
        myTaskDto.setTaskName("tetststtttt");
        return myTaskDto;

    }
}
