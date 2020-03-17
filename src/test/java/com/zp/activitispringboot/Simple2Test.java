package com.zp.activitispringboot;

import com.zp.activitispringboot.cmd.AddMultiInstanceCmd;
import com.zp.activitispringboot.dto.MyTaskDto;
import com.zp.activitispringboot.utils.ActivitiUtil;
import com.zp.activitispringboot.utils.SecurityUtil;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.task.model.Task;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * 有分支时取得所有节点和所有任务测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Simple2Test {
    @Autowired
    RuntimeService runtimeService;

    @Autowired
    HistoryService historyService;

    @Autowired
    RepositoryService repositoryService;
    @Autowired
    ProcessRuntime processRuntime;
    /**
     * security工具类
     */
    @Autowired
    private SecurityUtil securityUtil;

    @Test
    public void testDefiniton() {
        String username = "zhangsan";
        ActivitiUtil.printProcessDefinitionList(username);
    }

    @Test
    public void testProcessInstance() {
        String key = "simple2";
        String username = "zhangsan";
//        String businessKey = "iop-open-service.m_process.1";

        HashMap<String, Object> map = new HashMap<>();
        // 启动流程实例时给变量赋值
        map.put("assignee1", username);
        map.put("num", 5);

        ActivitiUtil.startProcessInstanceWithVariables(username, key, "分支", map);
    }

    @Test
    public void testQueryTask() {
        String assignee = "zhangsan";
        ActivitiUtil.printTaskList(assignee, 0, 10);
    }

    @Test
    public void testCompleteTask() {
        String assignee = "zhangsan";
        //设置会签人员
        HashMap map = new HashMap<String, Object>();
        map.put("assignee2", "lisi");
        map.put("assignee3", "wangwu");
        ActivitiUtil.completeTaskWithVariables(assignee, map);
    }

    @Test
    public void addComment() {
        String assignee = "lisi";
        String taskId = "ffea7f1d-6803-11ea-8360-1c1b0d7b318e";
        String comment = "Test Message222";
        ActivitiUtil.addComment(assignee, taskId, comment);
    }

    @Test
    public void getComment() {
        String assignee = "lisi";
        String taskId = "ffea7f1d-6803-11ea-8360-1c1b0d7b318e";
        ActivitiUtil.getComment(assignee, taskId);
    }


    @Test
    public void testQueryTask2() {
        String assignee = "lisi";
        ActivitiUtil.printTaskList(assignee, 0, 10);
    }

    @Test
    public void testAdd() {
        String assignee = "lisi";
        Task task = ActivitiUtil.getTaskList(assignee, 0, 10).getContent().get(0);
        String taskId = task.getId();
//        Execution execution = runtimeService.createExecutionQuery().processInstanceId(task.getProcessInstanceId()).list().get(0);
//        String executionId = execution.getId();
//        System.out.println(executionId);
//        List<String> assignee2List = (List<String>) runtimeService.getVariable(executionId, "assignee2List");
//        assignee2List.add("wangwu");
//        runtimeService.setVariable(executionId,"assignee2List", assignee2List);


        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        ManagementService managementService = defaultProcessEngine.getManagementService();
        // 执行增加实例命令
        String addUserTaskAssign = "wangwu";
        managementService.executeCommand(new AddMultiInstanceCmd(taskId, addUserTaskAssign));
    }

    @Test
    public void testCompleteTask2() {
        String assignee = "lisi";
        ActivitiUtil.completeTask(assignee);
    }

    @Test
    public void testCompleteTask3() {
        String assignee = "wangwu";
        ActivitiUtil.completeTask(assignee);
    }


    @Test
    public void testCompleteTask4() {
        String assignee = "zhaoliu";
        ActivitiUtil.completeTask(assignee);
    }


    @Test
    public void testGetNextTask() {
        String assignee = "zhangsan";
        String processDefinitionId = "simple2:3:8f790f83-681c-11ea-816b-1c1b0d7b318e";
        String processInstanceId = "6186bc42-6825-11ea-bd86-1c1b0d7b318e";

        securityUtil.logInAs(assignee);
        ProcessInstance processInstance =
                processRuntime.processInstance(processInstanceId);

        if(processInstance.getStatus().name().equals("RUNNING")) {
            // 当前实例在运行状态的场合

            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
            List<FlowElement> flowElements = (List<FlowElement>) bpmnModel.getMainProcess().getFlowElements();

            // 取得除开始节点以外的第一个节点
            FlowElement flowNode = flowElements.get(1);
            while (flowNode != null) {
                System.out.println(flowNode.getId() + " " + flowNode.getName());
                flowNode = ActivitiUtil.getAllNode(processDefinitionId,
                        processInstanceId, flowNode.getId());
            }
        } else{
            // 当前实例已结束的场合
        }



    }

    @Test
    public void testGetAllNode() {
        String assignee = "zhangsan";
        String processDefinitionId = "simple2:1:f15b89f1-6803-11ea-9fcb-1c1b0d7b318e";
        String processInstanceId = "f3036d9c-6803-11ea-9fcb-1c1b0d7b318e";
        List<MyTaskDto> allFlowElements = ActivitiUtil.getAllFlowElements(processDefinitionId, processInstanceId);
        for (MyTaskDto allFlowElement : allFlowElements) {
            System.out.println(allFlowElement.getTaskId() + " " + allFlowElement.getTaskName() + " " + allFlowElement.getTaskStatus());
        }
    }

    @Test
    public void testGetTask() {
        String assignee = "zhangsan";
        String taskId = "654a249b-643a-11ea-a385-1c1b0d7b318e";
        org.activiti.engine.task.Task task = ActivitiUtil.getTask(taskId);
        System.out.println(task);
    }

    @Test
    public void testGetCompleteTask() {
        String assignee = "zhangsan";
        List<HistoricActivityInstance> list = historyService.createHistoricActivityInstanceQuery().taskAssignee(assignee).list();
        for (HistoricActivityInstance historicActivityInstance : list) {
            String activityId = historicActivityInstance.getActivityId();
            BpmnModel bpmnModel = repositoryService.getBpmnModel(historicActivityInstance.getProcessDefinitionId());
            Process process = bpmnModel.getMainProcess();
            FlowElement flowElement = process.getFlowElement(activityId);
            System.out.println(flowElement.getId() + " " +
                    flowElement.getName() + " " +
                    historicActivityInstance.getEndTime());
        }

    }

    @Test
    public void testHisTaskInstance() {
        String processInstanceId = "b7d8fea9-643f-11ea-8591-1c1b0d7b318e";
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).list();
        for (HistoricTaskInstance historicTaskInstance : list) {
            System.out.println(historicTaskInstance.getName() + " " +
                    historicTaskInstance.getAssignee() + " " +
                    historicTaskInstance.getEndTime());
        }
    }

    @Test
    public void testHisTaskInstance2() {
        String assignee = "zhangsan";
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(assignee).list();
        for (HistoricTaskInstance historicTaskInstance : list) {
            System.out.println(historicTaskInstance.getName() + " " +
                    historicTaskInstance.getAssignee() + " " +
                    historicTaskInstance.getEndTime());
        }
    }
}
