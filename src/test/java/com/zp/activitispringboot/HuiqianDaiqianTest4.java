package com.zp.activitispringboot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.task.model.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.zp.activitispringboot.utils.ActivitiUtil;

/**
 * 与HuiqianDaiqianTest3.java是同一个流程定义，模拟单实例和多实例两种情况（此例为模拟多实例）
 * 将节点设为多实例，并行或串行都可
 * 将任务执行设为动态变量
 * 完成条件设为${nrOfInstances == nrOfCompletedInstances}，即所有人完成时才算完成
 * 并行的每个人可以将任务转给其它人来执行
 * 
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HuiqianDaiqianTest4 {

    @Test
    public void testDefiniton(){
        String username = "zhangsan";
        ActivitiUtil.printProcessDefinitionList(username);
    }

    @Test
    public void testProcessInstance(){
        String key = "huiqiandaiqian3";
        String username = "zhangsan";

        HashMap<String, Object> map = new HashMap<>();
        // 启动流程实例时给变量赋值
        map.put("assignee1", username);

        ActivitiUtil.startProcessInstanceWithVariables(username, key,"会签代签", map);
    }

    @Test
    public void testQueryTask(){
        String assignee = "zhangsan";
        ActivitiUtil.printTaskList(assignee, 0, 10);
    }

    @Test
    public void testCompleteTask(){
        String assignee = "zhangsan";
        //设置会签人员(只设一个人，就相当于是单实例节点)
        HashMap map = new HashMap<String, Object>();
        List<String> signList = new ArrayList<String>();
        signList.add("lisi");
        signList.add("wangwu");
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
    public void testCompleteTask2(){
        String assignee = "lisi";
        Page<Task> tasks = ActivitiUtil.getTaskList(assignee,0, 10);
        if( tasks.getTotalItems() > 0) {
            // 有任务时
            for (Task task : tasks.getContent()) {
                System.out.println(task);
                
                String assigneeOther = "sunqi";
                String taskId = task.getId();
                               
				// 放弃并把任务转给sunqi
                ActivitiUtil.transferTask(taskId, assigneeOther);
                
            }
        }
    }
    
    
    @Test
    public void testCompleteTask5(){
        String assignee = "sunqi";
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
