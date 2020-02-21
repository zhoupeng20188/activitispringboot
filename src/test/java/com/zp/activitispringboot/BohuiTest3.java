package com.zp.activitispringboot;

import com.zp.activitispringboot.utils.ActivitiUtil;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.task.model.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

/**
 * candidate user的场合
 * 获取当前节点和上个节点的定义信息，将当前节点的流向指向上个节点后完成任务，然后将流向复原
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BohuiTest3 {

    @Test
    public void testProcessInstance(){
        String key = "bohui3";
        String username = "zhangsan";

        HashMap<String, Object> map = new HashMap<>();
        // 启动流程实例时给变量赋值
        map.put("assignee1", username);
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
        ActivitiUtil.completeTask(assignee);
    }

    @Test
    public void testCompleteTask2(){
        String assignee = "c1";
        ActivitiUtil.completeTaskWithGroup(assignee);
    }


    @Test
    public void testCompleteTask3() throws Exception {
        String assignee = "c3";
        HashMap<String, Object> map = new HashMap<>();
        // 完成任务时同时指定审核为驳回
        map.put("audit", false);

        // 驳回
        ActivitiUtil.auditByCandidate(assignee, false, map);
    }

    @Test
    public void testQueryTask2(){
        String assignee = "c1";
        ActivitiUtil.printTaskList(assignee, 0, 10);
    }

    @Test
    public void testCompleteTask4(){
        String assignee = "c1";
        ActivitiUtil.completeTask(assignee);
    }

    @Test
    public void testCompleteTask5() throws Exception {
        String assignee = "c4";
        HashMap<String, Object> map = new HashMap<>();
        // 完成任务时同时指定审核为通过
        map.put("audit", true);

        // 审核通过
        ActivitiUtil.auditByCandidate(assignee, true, map);
    }





}
