package com.zp.activitispringboot;

import com.zp.activitispringboot.utils.ActivitiUtil;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.task.model.Task;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QingjiaTests {
    @Test
    public void testDeploy(){
        String key = "qingjia";
        ActivitiUtil.deploy(key,"请假流程");
    }

    @Test
    public void testDefiniton(){
        String username = "zhangsan";
        ActivitiUtil.printProcessDefinitionList(username);
    }

    @Test
    public void testProcessInstance(){
        String key = "qingjia";
        String username = "zhangsan";

        HashMap<String, Object> map = new HashMap<>();
        // 启动流程实例时给变量赋值
        map.put("assignee1", username);
        map.put("dayNum", 5);
        ActivitiUtil.startProcessInstanceWithVariables(username, key,"请假", map);
    }

    @Test
    public void testQueryTask(){
        String assignee = "zhangsan";
        ActivitiUtil.printTaskList(assignee, 0, 10);
    }

    @Test
    public void testCompleteTask(){
        String assignee = "zhangsan";
        HashMap<String, Object> map = new HashMap<>();
        // 完成任务时同时指定之后流程的指派人
        map.put("assignee2", "lisi");
        map.put("assignee3", "wangwu");
        map.put("assignee4", "zhaoliu");
        ActivitiUtil.completeTaskWithVariables(assignee, map);
    }


    @Test
    public void testQueryTask2(){
        String assignee = "lisi";
        ActivitiUtil.printTaskList(assignee, 0, 10);
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

    /**
     * 取得当前任务的下一个任务
     */
    @Test
    public void getNextTask() throws Exception {
        String assignee = "lisi";
        Task task = ActivitiUtil.getTaskList(assignee, 0, 10).getContent().get(0);
        ActivitiUtil.getNextUserFlowElement(task);
    }

    @Test
    public void geProcessDiagram() throws FileNotFoundException {
//        String assignee = "wangwu";
        String filepath = "F:\\processDiagram.png";
//        Task task = ActivitiUtil.getTaskList(assignee, 0, 10).getContent().get(0);
//        String processInstanceId = task.getProcessInstanceId();
        String processInstanceId = "625b652e-5a03-11ea-8131-1c1b0d7b318e";
        ActivitiUtil.getFlowImgByInstanceId(processInstanceId, new FileOutputStream(filepath));
        System.out.println("图片生成成功");
    }
}
