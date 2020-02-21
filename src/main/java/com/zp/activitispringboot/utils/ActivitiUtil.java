package com.zp.activitispringboot.utils;

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
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public void rejectFlow(org.activiti.engine.task.Task curTask) {
        String processDefinitionId = curTask.getProcessDefinitionId();


//        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.createProcessDefinitionQuery()
//                .processDefinitionId(processDefinitionId)
//                .singleResult();


        /**
         * findActivity,getIncomingTransitions,getOutgoingTransitions等方法在6开始就已经移除了
         * 最新写法是取得BpmnModel
         */

        BpmnModel bpmnModel = repositoryService
                .getBpmnModel(repositoryService.createProcessDefinitionQuery()
                        .processDefinitionId(processDefinitionId)
                        .singleResult().getId());

        Process process = bpmnModel.getMainProcess();

        FlowElement curFlowElement = process.getFlowElement(curTask.getTaskDefinitionKey());


//        //获取当前activity
//        ActivityImpl curActivity = processDefinitionEntity
//                .findActivity(curTask.getTaskDefinitionKey());
        UserTask current = (UserTask) curFlowElement;

//         取得上一步活动的节点流向
//        List<PvmTransition> incomingTransitions = curActivity.getIncomingTransitions();

        // 取得上一步活动节点的流向
        List<SequenceFlow> incomingFlows = current.getIncomingFlows();

        //清空指定节点所有流向并暂时先将所有流向变量暂时存储在一个新的集合（主要是为后来恢复正常流程走向做准备）

//        List<PvmTransition> pvmTransitionList = new ArrayList<>();
        List<FlowElement> bakOutGoingFlows = new ArrayList<>();

//        List<PvmTransition> outgoingTransitions = curActivity.getOutgoingTransitions();

        // 取得所有出去流向
        List<SequenceFlow> outgoingFlows = current.getOutgoingFlows();


//        for (PvmTransition pvmTransition: outgoingTransitions) {
//            pvmTransitionList.add(pvmTransition);
//        }
//        outgoingTransitions.clear();

        for (SequenceFlow outgoingFlow : outgoingFlows) {
            bakOutGoingFlows.add(outgoingFlow);
        }

        outgoingFlows.clear();

        //创建新的流向并且设置新的流向的目标节点
        // （将该节点的流程走向都设置为上一节点的流程走向，目的是相当于形成一个回路）
//        List<TransitionImpl> newTransitionList = new ArrayList<>();
//
//
//
//        for (PvmTransition pvmTransition : incomingTransitions) {
//            PvmActivity source = pvmTransition.getSource();
//            ActivityImpl inActivity = processDefinitionEntity.findActivity(source.getId());
//            TransitionImpl newOutgoingTransition = curActivity.createOutgoingTransition();
//            newOutgoingTransition.setDestination(inActivity);
//            newTransitionList.add(newOutgoingTransition);
//
//        }




        for(FlowElement flowElement: incomingFlows) {
//            flowElement
        }

        // 完成任务

        List<org.activiti.engine.task.Task> taskList = taskService.createTaskQuery().processInstanceId(curTask.getProcessInstanceId()).list();
        for (org.activiti.engine.task.Task task : taskList) {
            taskService.complete(task.getId());
            historyService.deleteHistoricTaskInstance(task.getId());
        }


        // 恢复方向（实现驳回功能后恢复原来正常的方向）
//        for (TransitionImpl transitionImpl : newTransitionList) {
//            curActivity.getOutgoingTransitions().remove(transitionImpl);
//        }
//
//
//        for (PvmTransition pvmTransition : pvmTransitionList) {
//            outgoingTransitions.add(pvmTransition);
//        }

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
     *
     * @param task
     */
    public static void backProcess(Task task) throws Exception {

        String processInstanceId = task.getProcessInstanceId();

        // 取得所有历史任务按时间降序排序
        List<HistoricTaskInstance> htiList = activitiUtil.historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByTaskCreateTime()
                .desc()
                .list();

        if (ObjectUtils.isEmpty(htiList) || htiList.size() < 2) {
            return;
        }

        // list里的第二条代表上一个任务
        HistoricTaskInstance lastTask = htiList.get(1);

        // list里第二条代表当前任务
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
}
