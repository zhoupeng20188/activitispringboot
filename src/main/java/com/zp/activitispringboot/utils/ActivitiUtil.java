package com.zp.activitispringboot.utils;

import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Component
public class ActivitiUtil {
    private static Logger logger = LoggerFactory.getLogger(ActivitiUtil.class);

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

    @Autowired
    private RepositoryService repositoryService;

    private static ActivitiUtil activitiUtil;

    @PostConstruct
    public void init() {
        // processRuntime等为static时无法注入，必须用activitiUtil.processRuntime形式
        activitiUtil = this;
    }

    /**
     * 流程部署
     * @param bpmnName bpmn文件名(不包含扩展名)
     * @param deployName 流程部署名称
     */
    public static void deploy(String bpmnName, String deployName){
        // 进行部署
        Deployment deployment = activitiUtil.repositoryService.createDeployment()
                // 文件夹的名称不能是process
                .addClasspathResource("processes/" + bpmnName + ".bpmn")
//                .addClasspathResource("processes/" + bpmnName + ".png")
                .name(deployName)
                .deploy();

        System.out.println("流程部署成功！ " + "部署id:"
                + deployment.getId() + "  " + "部署名称:" + deployment.getName());
    }


    /**
     * 打印所有流程定义
     * @param username 用户名
     */
    public static void printProcessDefinitionList(String username){
        activitiUtil.securityUtil.logInAs(username);
        Page processDefinitionPage = getProcessDefinitionList(0, 10);
        // getTotalItems()取得的是包含所有版本的总数
        // getContent().size()取得的是流程数，多个版本的同一流程只算一次
        logger.info("流程定义数: " +
//                processDefinitionPage.getTotalItems());
                processDefinitionPage.getContent().size());
        for (Object pd : processDefinitionPage.getContent()) {
            logger.info("\t > 流程定义: " + pd);
        }
    }

    /**
     * 获取所有流程定义
     * @param startNum 分页开始下标
     * @param endNum 分页结束下标
     * @return 流程定义list
     */
    public static Page getProcessDefinitionList(Integer startNum, Integer endNum){
        return activitiUtil.processRuntime
                .processDefinitions(Pageable.of(startNum, endNum));
    }

    /**
     * 完成任务
     * @param assignee 指派人
     */
    public static void completeTask(String assignee){

        Page<Task> tasks = getTaskList(assignee,0, 10);
        if( tasks.getTotalItems() > 0) {
            // 有任务时，完成任务
            for (Task task : tasks.getContent()) {
                System.out.println(task);
                // 完成任务
                activitiUtil.taskRuntime.complete(
                        TaskPayloadBuilder.complete().withTaskId(task.getId()).build());
                logger.info(assignee + "完成任务");
            }
        }
    }

    /**
     * 完成任务
     * @param assignee 指派人
     * @param variables 变量map
     */
    public static void completeTaskWithVariables(String assignee, HashMap variables){

        Page<Task> tasks = getTaskList(assignee,0, 10);
        if( tasks.getTotalItems() > 0) {
            // 有任务时，完成任务
            for (Task task : tasks.getContent()) {
                System.out.println(task);
                // 完成任务
                activitiUtil.taskRuntime.complete(
                        TaskPayloadBuilder.complete()
                                .withTaskId(task.getId())
                                .withVariables(variables).build());
                logger.info(assignee + "完成任务");
            }
        }
    }

    /**
     * 完成任务
     * @param assignee 指派人
     */
    public static void completeTaskWithGroup(String assignee){

        Page<Task> tasks = getTaskList(assignee,0, 10);
        if( tasks.getTotalItems() > 0) {
            // 有任务时，完成任务
            for (Task task : tasks.getContent()) {
                System.out.println(task);
                // 拾取任务
                activitiUtil.taskRuntime.claim(
                        TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
                logger.info(assignee + "拾取任务成功");
                // 完成任务
                activitiUtil.taskRuntime.complete(
                        TaskPayloadBuilder.complete().withTaskId(task.getId()).build());
                logger.info(assignee + "完成任务");
            }
        }
    }

    /**
     * 打印指派人所有任务
     * @param assignee 指派人
     * @param startNum 分页开始下标
     * @param endNum 分页结束下标
     */
    public static void printTaskList(String assignee, Integer startNum, Integer endNum){
        Page<Task> tasks = getTaskList(assignee,0, 10);
        if( tasks.getTotalItems() > 0) {
            // 有任务时，完成任务
            for (Task task : tasks.getContent()) {
                logger.info("任务: " + task);
            }
        }
    }

    /**
     * 查询当前指派人的任务
     * @param assignee 指派人
     * @param startNum 分页开始下标
     * @param endNum 分页结束下标
     * @return 任务list
     */
    public static Page<Task> getTaskList(String assignee, Integer startNum, Integer endNum){
        activitiUtil.securityUtil.logInAs(assignee);
        return activitiUtil.taskRuntime.tasks(Pageable.of(startNum, endNum));
    }


    /**
     * 流程实例启动
     * @param processKey 流程Key => 对应bpmn文件里的id
     * @param processName 流程实例名
     */
    public static void startProcessInstance(String username, String processKey, String processName){
        activitiUtil.securityUtil.logInAs(username);
        ProcessInstance processInstance = activitiUtil.processRuntime
                .start(ProcessPayloadBuilder
                        .start()
                        .withProcessDefinitionKey(processKey)
                        .withName(processName)
                        .build());
        logger.info("流程实例启动成功: " + processInstance);
    }

    /**
     *
     * @param username 用户名
     * @param processKey 流程Key => 对应bpmn文件里的id
     * @param processName 流程实例名
     * @param variables 变量map
     */
    public static void startProcessInstanceWithVariables(String username,
            String processKey, String processName, HashMap variables){
        activitiUtil.securityUtil.logInAs(username);
        ProcessInstance processInstance = activitiUtil.processRuntime
                .start(ProcessPayloadBuilder
                        .start()
                        .withProcessDefinitionKey(processKey)
                        .withName(processName)
                        .withVariables(variables)
                        .build());
        logger.info("流程实例启动成功: " + processInstance);
    }
}
