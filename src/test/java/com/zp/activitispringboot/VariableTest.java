package com.zp.activitispringboot;

import com.zp.activitispringboot.utils.ActivitiUtil;
import com.zp.activitispringboot.utils.SecurityUtil;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;

/**
 * 获取变量测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class VariableTest {
    @Autowired
    RuntimeService runtimeService;

    @Autowired
    HistoryService historyService;

    /**
     * security工具类
     */
    @Autowired
    private SecurityUtil securityUtil;


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
    public void testCompleteTask() {
        String assignee = "zhangsan";

        HashMap<String, Object> map = new HashMap<>();
        map.put("assignee2", "lisi");
        map.put("assignee3", "wangwu");

        ActivitiUtil.completeTaskWithVariables(assignee, map);
        ActivitiUtil.completeTask("lisi");
    }


    @Test
    public void getVariableByName() {
        String assignee = "zhangsan";
        List<HistoricVariableInstance> num = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId("60cbbb90-98d2-11ea-9a3d-668a46cc06f6")
                .variableName("num")
                .list();
        for (HistoricVariableInstance historicVariableInstance : num) {
            System.out.println(historicVariableInstance.getVariableName());
            System.out.println(historicVariableInstance.getValue());
        }

    }

    @Test
    public void getVariableByExecutionId() {
        String assignee = "zhangsan";
        List<HistoricVariableInstance> num = historyService.createHistoricVariableInstanceQuery()
                .executionId("60cd4234-98d2-11ea-9a3d-668a46cc06f6")
                .list();
        for (HistoricVariableInstance historicVariableInstance : num) {
            System.out.println(historicVariableInstance.getVariableName());
            System.out.println(historicVariableInstance.getValue());
        }

    }

    @Test
    public void getVariableByTaskId() {
        String assignee = "zhangsan";
        List<HistoricVariableInstance> num = historyService.createHistoricVariableInstanceQuery()
                .taskId("60d09d97-98d2-11ea-9a3d-668a46cc06f6")
                .list();
        for (HistoricVariableInstance historicVariableInstance : num) {
            System.out.println(historicVariableInstance.getVariableName());
            System.out.println(historicVariableInstance.getValue());
        }

    }

    @Test
    public void getVariableByProcessInstanceId() {
        String assignee = "zhangsan";
        List<HistoricVariableInstance> num = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId("77955fa4-98d7-11ea-a0ae-668a46cc06f6")
                .excludeTaskVariables()
                .list();
        for (HistoricVariableInstance historicVariableInstance : num) {
            System.out.println(historicVariableInstance.getVariableName());
            System.out.println(historicVariableInstance.getValue());
        }

    }

    @Test
    public void getValueByVariableName() {
        String assignee = "zhangsan";
        HistoricVariableInstance historicVariableInstance = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId("77955fa4-98d7-11ea-a0ae-668a46cc06f6")
                .variableName("num")
                .excludeTaskVariables()
                .singleResult();
        System.out.println(historicVariableInstance.getValue());

    }


}
