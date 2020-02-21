package com.zp.activitispringboot;

import com.zp.activitispringboot.utils.ActivitiUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

/**
 * 单个执行人的场合
 * 直接通过流程图里连线即可
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BohuiTest {

    @Test
    public void testProcessInstance(){
        String key = "bohui";
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
        HashMap<String, Object> map = new HashMap<>();
        // 完成任务时同时指定之后流程的指派人
        map.put("assignee2", "lisi");
        ActivitiUtil.completeTaskWithVariables(assignee, map);
    }

    @Test
    public void testCompleteTask2(){
        String assignee = "lisi";
        HashMap<String, Object> map = new HashMap<>();
        // 完成任务时同时指定审核为驳回
        map.put("audit", false);
        ActivitiUtil.completeTaskWithVariables(assignee, map);
    }

    @Test
    public void testCompleteTask3(){
        String assignee = "zhangsan";
        ActivitiUtil.completeTask(assignee);
    }

    @Test
    public void testCompleteTask4(){
        String assignee = "lisi";
        HashMap<String, Object> map = new HashMap<>();
        // 完成任务时同时指定审核为通过
        map.put("audit", true);
        ActivitiUtil.completeTaskWithVariables(assignee, map);
    }



}
