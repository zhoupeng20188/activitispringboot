package com.zp.activitispringboot;

import com.zp.activitispringboot.utils.ActivitiUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 多实例节点变量设定测试
 * 结论：必须设置assignee
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MyProcessTest {

    @Test
    public void testDefiniton(){
        String username = "zhangsan";
        ActivitiUtil.printProcessDefinitionList(username);
    }

    @Test
    public void testProcessInstance(){
        String key = "myProcess";
        String username = "zhangsan";

        HashMap<String, Object> map = new HashMap<>();
        // 启动流程实例时给变量赋值
        map.put("assignee1", username);
        List<String> signList = new ArrayList<String>();
        signList.add("lisi");
        signList.add("wangwu");
        map.put("assignee2List", signList);
        map.put("assignee3", "zhaoliu");
        map.put("assignee4", "sunqi");
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
        ActivitiUtil.completeTask(assignee);
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


    @Test
    public void testCompleteTask5(){
        String assignee = "sunqi";
        ActivitiUtil.completeTask(assignee);
    }
}
