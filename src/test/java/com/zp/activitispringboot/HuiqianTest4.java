package com.zp.activitispringboot;

import com.zp.activitispringboot.cmd.AddMultiInstanceCmd;
import com.zp.activitispringboot.cmd.JumpCmd;
import com.zp.activitispringboot.utils.ActivitiUtil;
import org.activiti.api.task.model.Task;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 多实例实现会签 并行
 * 在会签节点动态增加人员
 * 此示例通过条件为${nrOfInstances == nrOfCompletedInstances}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HuiqianTest4 {
    @Autowired
    RuntimeService runtimeService;

    @Autowired
    RepositoryService repositoryService;

    @Test
    public void testDefiniton(){
        String username = "zhangsan";
        ActivitiUtil.printProcessDefinitionList(username);
    }

    @Test
    public void testProcessInstance(){
        String key = "huiqian4";
        String username = "zhangsan";

        HashMap<String, Object> map = new HashMap<>();
        // 启动流程实例时给变量赋值
        map.put("assignee1", username);

        ActivitiUtil.startProcessInstanceWithVariables(username, key,"会签", map);
    }

    @Test
    public void testQueryTask(){
        String assignee = "zhangsan";
        ActivitiUtil.printTaskList(assignee, 0, 10);
    }

    @Test
    public void testCompleteTask(){
        String assignee = "zhangsan";
        //设置会签人员
        HashMap map = new HashMap<String, Object>();
        List<String> signList = new ArrayList<String>();
        signList.add("lisi");
//        signList.add("wangwu");
        map.put("assignee2List", signList);
        map.put("assignee3","zhaoliu");
        ActivitiUtil.completeTaskWithVariables(assignee, map);
    }


    @Test
    public void testQueryTask2(){
        String assignee = "lisi";
        ActivitiUtil.printTaskList(assignee, 0, 10);
    }

    @Test
    public void testAdd(){
        String assignee = "lisi";
        Task task = ActivitiUtil.getTaskList(assignee, 0, 10).getContent().get(0);
        Execution execution = runtimeService.createExecutionQuery().processInstanceId(task.getProcessInstanceId()).list().get(0);
        String executionId = execution.getId();
        String taskId = task.getId();
        System.out.println(executionId);
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
    public void testCompleteTask2(){
        String assignee = "lisi";
        ActivitiUtil.completeTask(assignee);
    }
    
    @Test
    public void testCompleteTask3(){
        String assignee = "wangwu";
        ActivitiUtil.completeTask(assignee);
    }


    @Test
    public void testCompleteTask4(){
        String assignee = "zhaoliu";
        ActivitiUtil.completeTask(assignee);
    }
}
