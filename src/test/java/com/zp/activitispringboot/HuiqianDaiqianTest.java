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
 * 节点多实例实现代签
 * 将节点设为多实例，串行
 * 将任务执行设为用户组
 * 完成条件设为${nrOfCompletedInstances == 1}，即有一个人完成时完成任务
 * 因为是串行，所以保证当前任务只有一个人能拾取，拾取后可以放弃并转给组内的另一个人，相当于是单实例代签的效果
 * 
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HuiqianDaiqianTest {

    @Test
    public void testDefiniton(){
        String username = "zhangsan";
        ActivitiUtil.printProcessDefinitionList(username);
    }

    @Test
    public void testProcessInstance(){
        String key = "huiqiandaiqian";
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
      //设置会签人员
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
                
                String assigneeOther = "wangwu";
                String taskId = task.getId();
                
                // 拾取任务
                ActivitiUtil.claimTask(assignee, taskId);
                               
				// 放弃并把任务转给wangwu
                ActivitiUtil.transferTask(taskId, assigneeOther);
               
                
            }
        }
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
