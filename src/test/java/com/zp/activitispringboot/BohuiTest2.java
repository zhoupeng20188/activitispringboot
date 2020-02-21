package com.zp.activitispringboot;

import com.zp.activitispringboot.utils.ActivitiUtil;
import org.activiti.api.task.model.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

/**
 * candidate user的场合
 * 由于候选用户有多个，直接连线时返回到上一步无法确定之前是哪个候选人执行的，所以这种方式不行
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BohuiTest2 {

    @Test
    public void testProcessInstance(){
        String key = "bohui2";
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
    public void testCompleteTask3(){
        String assignee = "c3";
        HashMap<String, Object> map = new HashMap<>();
        // 完成任务时同时指定审核为驳回
        map.put("audit", false);
        ActivitiUtil.completeTaskWithGroupWithVariables(assignee, map);
    }

    @Test
    public void testQueryTask2(){
        String assignee = "c1";
        ActivitiUtil.printTaskList(assignee, 0, 10);
    }





}
