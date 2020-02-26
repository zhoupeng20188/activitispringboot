package com.zp.activitispringboot.utils;

import com.zp.activitispringboot.cmd.JumpCmd;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.task.model.Task;
import org.activiti.api.task.model.builders.TaskPayloadBuilder;
import org.activiti.api.task.runtime.TaskRuntime;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.*;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.deploy.ProcessDefinitionCacheEntry;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.util.*;

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

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RuntimeService runtimeService;

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
     * 候选人拾取并完成任务
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
     * 候选人拾取并完成任务并指定变量
     * @param assignee 指派人
     */
    public static void completeTaskWithGroupWithVariables(String assignee, HashMap variables){

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
                        TaskPayloadBuilder.complete()
                                .withTaskId(task.getId())
                                .withVariables(variables)
                                .build());
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
        Page<Task> tasks = getTaskList(assignee,startNum, endNum);
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

    /**
     * 审核(candidate user的场合)
     * @param assignee 执行人
     * @param auditFlg 审核flg, true：通过，false:驳回
     * @param variables 完成任务时带上变量集合
     * @throws Exception
     */
    public static void auditByCandidate(String assignee, boolean auditFlg, HashMap variables) throws Exception {

        Page<Task> tasks = getTaskList(assignee,0, 10);
        if( tasks.getTotalItems() > 0) {
            // 有任务时
            for (Task task : tasks.getContent()) {
                System.out.println(task);
                // 拾取任务
                activitiUtil.taskRuntime.claim(
                        TaskPayloadBuilder.claim().withTaskId(task.getId()).build());
                logger.info(assignee + "拾取任务成功");


                if(auditFlg){
                    // 审核通过的场合
                    // 完成任务
                    activitiUtil.taskRuntime.complete(
                            TaskPayloadBuilder.complete()
                                    .withTaskId(task.getId())
                                    .withVariables(variables)
                                    .build());
                    logger.info(assignee + "完成任务");
                    logger.info(assignee + "审核通过");
                } else{
                    // 驳回的场合
                    backProcess(task);
                    logger.info(assignee + "审核驳回");
                }



            }
        }


    }


    /**
     * 退回到上一节点
     * @param task 当前任务
     */
    public static void backProcess(Task task) throws Exception {

        String processInstanceId = task.getProcessInstanceId();

        // 取得所有历史任务按时间降序排序
        List<HistoricTaskInstance> htiList = getHistoryTaskList(processInstanceId);

        if (ObjectUtils.isEmpty(htiList) || htiList.size() < 2) {
            return;
        }

        // list里的第二条代表上一个任务
        HistoricTaskInstance lastTask = htiList.get(1);

        // list里第一条代表当前任务
        HistoricTaskInstance curTask = htiList.get(0);

        // 当前节点的executionId
        String curExecutionId = curTask.getExecutionId();


        // 上个节点的taskId
        String lastTaskId = lastTask.getId();
        // 上个节点的executionId
        String lastExecutionId = lastTask.getExecutionId();

        if (null == lastTaskId) {
            throw new Exception("LAST TASK IS NULL");
        }

        String processDefinitionId = lastTask.getProcessDefinitionId();
        BpmnModel bpmnModel = activitiUtil.repositoryService.getBpmnModel(processDefinitionId);

        String lastActivityId = null;
        List<HistoricActivityInstance> haiFinishedList = activitiUtil.historyService.createHistoricActivityInstanceQuery()
                .executionId(lastExecutionId).finished().list();

        for (HistoricActivityInstance hai : haiFinishedList) {
            if (lastTaskId.equals(hai.getTaskId())) {
                // 得到ActivityId，只有HistoricActivityInstance对象里才有此方法
                lastActivityId = hai.getActivityId();
                break;
            }
        }

        // 得到上个节点的信息
        FlowNode lastFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(lastActivityId);

        // 取得当前节点的信息
        Execution execution = activitiUtil.runtimeService.createExecutionQuery().executionId(curExecutionId).singleResult();
        String curActivityId = execution.getActivityId();
        FlowNode curFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(curActivityId);

        //记录当前节点的原活动方向
        List<SequenceFlow> oriSequenceFlows = new ArrayList<>();
        oriSequenceFlows.addAll(curFlowNode.getOutgoingFlows());

        //清理活动方向
        curFlowNode.getOutgoingFlows().clear();

        //建立新方向
        List<SequenceFlow> newSequenceFlowList = new ArrayList<>();
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlowId");
        newSequenceFlow.setSourceFlowElement(curFlowNode);
        newSequenceFlow.setTargetFlowElement(lastFlowNode);
        newSequenceFlowList.add(newSequenceFlow);
        curFlowNode.setOutgoingFlows(newSequenceFlowList);

        // 完成任务
        activitiUtil.taskService.complete(task.getId());

        //恢复原方向
        curFlowNode.setOutgoingFlows(oriSequenceFlows);

        org.activiti.engine.task.Task nextTask = activitiUtil.taskService
                .createTaskQuery().processInstanceId(processInstanceId).singleResult();

        // 设置执行人
        if(nextTask!=null) {
            activitiUtil.taskService.setAssignee(nextTask.getId(), lastTask.getAssignee());
        }
    }

    public static List<HistoricTaskInstance> getHistoryTaskList(String processInstanceId) {
        return activitiUtil.historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByTaskCreateTime()
                .desc()
                .list();
    }

    /**
     * 动态增加任务
     * @param task 当前任务
     * @param assignee 增加任务的指派人
     */
    public static void addTask(Task task, String assignee) throws Exception {
        String processInstanceId = task.getProcessInstanceId();

        // 取得所有历史任务按时间降序排序
        List<HistoricTaskInstance> htiList = getHistoryTaskList(processInstanceId);

        if (ObjectUtils.isEmpty(htiList) || htiList.size() < 2) {
            return;
        }

        // list里第一条代表当前任务
        HistoricTaskInstance curTask = htiList.get(0);

        // 当前节点的executionId
        String curExecutionId = curTask.getExecutionId();

        String processDefinitionId = task.getProcessDefinitionId();
        BpmnModel bpmnModel = activitiUtil.repositoryService.getBpmnModel(processDefinitionId);

        // 取得当前节点的信息
        Execution execution = activitiUtil.runtimeService.createExecutionQuery().executionId(curExecutionId).singleResult();
        String curActivityId = execution.getActivityId();
        FlowNode curFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(curActivityId);

        //记录当前节点的原活动方向
        List<SequenceFlow> oriSequenceFlows = new ArrayList<>();
        oriSequenceFlows.addAll(curFlowNode.getOutgoingFlows());

        //清理活动方向
        curFlowNode.getOutgoingFlows().clear();


        // 创建新节点
        UserTask newUserTask = new UserTask();
        newUserTask.setId("destinyD");
        newUserTask.setName("加签节点 destinyD");
        newUserTask.setAssignee(assignee);

        // 设置新节点的出线为当前节点的出线
        newUserTask.setOutgoingFlows(oriSequenceFlows);

        // 当前节点与新节点的连线
        SequenceFlow sequenceFlow = new SequenceFlow();
        sequenceFlow.setId("extra");
        sequenceFlow.setSourceFlowElement(curFlowNode);
        sequenceFlow.setTargetFlowElement(newUserTask);

        // 将当前节点的出线设置为指向新节点
        curFlowNode.setOutgoingFlows(Arrays.asList(sequenceFlow));

        // 取得流程定义缓存
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        ProcessEngineConfigurationImpl processEngineConfiguration = (ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration();
        ProcessDefinitionCacheEntry cacheEntry = processEngineConfiguration.getProcessDefinitionCache().get(processDefinitionId);

        // 从缓存中取得process对象
        Process process = cacheEntry.getProcess();
        // 添加新节点到process中
        process.addFlowElement(newUserTask);
        // 添加新连线到process中
        process.addFlowElement(sequenceFlow);

        // 更新缓存
        cacheEntry.setProcess(process);

//        // 完成任务
//        activitiUtil.taskService.complete(task.getId());
//        System.out.println("完成任务");

//        // 取得managementService
//        ManagementService managementService = processEngine.getManagementService();
//
//        // 立即生效
//        managementService.executeCommand(new JumpCmd(task.getId(), newUserTask.getId()));

        System.out.println("加签成功");



//        FlowElement nextUserFlowElement = getNextUserFlowElement(task);
//        System.out.println("下个任务为：" + nextUserFlowElement.getName());
    }


    /**
     * 创建任务,指定执行人
     * @param assignee 登录用户
     * @param assigneeNewTask 执行人
     */
    public static void createTask(String assignee,String parentTaskId, String assigneeNewTask){
        activitiUtil.securityUtil.logInAs(assignee);
        activitiUtil.taskRuntime.create(
                TaskPayloadBuilder.create()
                        .withName("First Team Task")
                        .withDescription("This is something really important")
                        .withParentTaskId(parentTaskId)
                        .withAssignee(assigneeNewTask)
                        .withPriority(10)
                        .build());
    }


    /**
     * 获取当前任务节点的下一个任务节点
     * @param task 当前任务节点
     * @return 下个任务节点
     * @throws Exception
     */
    public static FlowElement getNextUserFlowElement(Task task) throws Exception {
        // 取得已提交的任务
        HistoricTaskInstance historicTaskInstance = activitiUtil.historyService.createHistoricTaskInstanceQuery()
                .taskId(task.getId()).singleResult();

        // 取得正在流转的流程实例,若已完成则为null
//        ProcessInstance processInstance = processRuntime.processInstance(historicTaskInstance.getProcessInstanceId());

        //每个流程实例只有一个executionId
        //获得流程定义
        ProcessDefinition processDefinition=activitiUtil.repositoryService.getProcessDefinition(historicTaskInstance.getProcessDefinitionId());

        //获得当前流程的活动ID
        ExecutionQuery executionQuery =activitiUtil.runtimeService.createExecutionQuery();
        Execution execution =executionQuery.executionId(historicTaskInstance.getExecutionId()).singleResult();
        String activityId=execution.getActivityId();
        UserTask userTask =null;
        while (true){
            //根据活动节点获取当前的组件信息
            FlowNode flowNode =getFlowNode(processDefinition.getId(),activityId);
            //获取该流程组件的之后/之前的组件信息
            List<SequenceFlow> sequenceFlowListOutGoing=flowNode.getOutgoingFlows();
//        List<SequenceFlow> sequenceFlowListIncoming=flowNode.getIncomingFlows();

            //获取的下个节点不一定是userTask的任务节点，所以要判断是否是任务节点
            //sequenceFlowListOutGoing数量可能大于1,可以自己做判断,此处只取第一个
            FlowElement flowElement =sequenceFlowListOutGoing.get(0).getTargetFlowElement();
            if (flowElement instanceof UserTask){
                userTask =(UserTask)flowElement;
                System.out.println("获取该任务节点的审批信息...");
                break;
            }else {
                //下一节点不是任务userTask的任务节点,所以要获取再下一个节点的信息,直到获取到userTask任务节点信息
                String flowElementId=flowElement.getId();
                activityId=flowElementId;
                continue;
            }
        }
        return userTask;
    }

    /**
     * 根据活动节点和流程定义ID获取该活动节点的组件信息
     */
    public static FlowNode getFlowNode(String processDefinitionId,String activityId){
        BpmnModel bpmnModel = activitiUtil.repositoryService.getBpmnModel(processDefinitionId);
        FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityId);
        return flowNode;
    }

    /**
     * 跳转任务
     * @param assignee
     */
    public static void jumpTask(String assignee) {
        activitiUtil.securityUtil.logInAs(assignee);
        Page<Task> tasks = getTaskList(assignee,0, 10);
        if( tasks.getTotalItems() > 0) {
            // 有任务时，完成任务
            for (Task task : tasks.getContent()) {
                System.out.println(task);
                ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
                ManagementService managementService = defaultProcessEngine.getManagementService();
                // 跳转到userTask1
                managementService.executeCommand(new JumpCmd(task.getId(),"usertask1"));
            }
        }
    }
}
