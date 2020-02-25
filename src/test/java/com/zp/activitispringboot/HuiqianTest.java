package com.zp.activitispringboot;

import com.zp.activitispringboot.utils.ActivitiUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 多实例实现会签
 * nrOfInstances（numberOfInstances）：会签中总共的实例数
 * nrOfCompletedInstances：已经完成的实例数量
 * nrOfActiviteInstances：当前活动的实例数量，即还没有完成的实例数量
 * 条件${nrOfInstances == nrOfCompletedInstances}表示所有人员审批完成后会签结束。
 * 条件${ nrOfCompletedInstances == 1}表示一个人完成审批，该会签就结束。
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HuiqianTest {

    @Test
    public void testDefiniton(){
        String username = "zhangsan";
        ActivitiUtil.printProcessDefinitionList(username);
    }

    @Test
    public void testProcessInstance(){
        String key = "huiqian";
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
        // 审批通过
        HashMap map = new HashMap<String, Object>();
        map.put("pass", true);
        ActivitiUtil.completeTaskWithVariables(assignee, map);
    }

    @Test
    public void testCompleteTask3(){
        String assignee = "wangwu";
     // 审批通过
        HashMap map = new HashMap<String, Object>();
        map.put("pass", true);
        ActivitiUtil.completeTaskWithVariables(assignee, map);
    }

    @Test
    public void testCompleteTask4(){
        String assignee = "zhaoliu";
        ActivitiUtil.completeTask(assignee);
    }
}
