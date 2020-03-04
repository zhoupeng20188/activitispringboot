package com.zp.activitispringboot;

import com.zp.activitispringboot.dto.MyTaskDto;
import com.zp.activitispringboot.utils.ActivitiUtil;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.task.model.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyExclusiveGatewayTests {
    @Test
    public void testDeploy() {
        String key = "qingjia";
        ActivitiUtil.deploy(key, "请假流程");
    }

    @Test
    public void testDefiniton() {
        String username = "zhangsan";
        ActivitiUtil.printProcessDefinitionList(username);
    }

    @Test
    public void testProcessInstance() {
        String key = "myExclusiveGateway";
        String username = "zhangsan";
        String businessKey = "test.1";

        HashMap<String, Object> map = new HashMap<>();
        // 启动流程实例时给变量赋值
        map.put("assignee1", username);
        map.put("assignee2", "lisi");
        map.put("assignee3", "wangwu");
        map.put("num", 5);
        ActivitiUtil.startProcessInstance(username, key, "请假", businessKey, map);
    }

    /**
     * 知会实现
     * 即新建一个任务,此任务与流程无关,不管此任务完成与否都不影响整个流程的走向
     */
    @Test
    public void createTask() {
        String assignee = "zhangsan";
        String assigneeNewTask = "zhaoliu";
        // 取得业务key
        String businessKey = ActivitiUtil.getTaskList(assignee, 0, 10).getContent().get(0).getBusinessKey();
        // 将业务key设为task名称,为的是以后取得这条记录时能拿到业务key
        String taskName = businessKey;
        Task task = ActivitiUtil.createTask(assignee, taskName, assigneeNewTask);
        System.out.println("任务创建成功:" + task);
    }

    @Test
    public void createSubTask() {
        String assignee = "zhangsan";
        String assigneeNewTask = "zhaoliu";
        String parentTaskId = ActivitiUtil.getTaskList(assignee, 0, 10).getContent().get(0).getId();
        ActivitiUtil.createSubTask(assignee, parentTaskId, assigneeNewTask);
    }

    @Test
    public void testQueryTask() {
        String assignee = "zhangsan";
        ActivitiUtil.printTaskList(assignee, 0, 10);
    }

    @Test
    public void testCompleteTask() {
        String assignee = "zhangsan";
        ActivitiUtil.completeTask(assignee);
    }


    @Test
    public void testQueryTask2() {
        String assignee = "lisi";
        ActivitiUtil.printTaskList(assignee, 0, 10);
    }

    @Test
    public void testQueryTask3() {
        String assignee = "zhaoliu";
        Page<Task> taskList = ActivitiUtil.getTaskList(assignee, 0, 10);
        for (Task task : taskList.getContent()) {
//            String parentTaskId = task.getParentTaskId();
//            System.out.println("父任务id为:" + parentTaskId);
//            if (!StringUtils.isEmpty(parentTaskId)) {
//                org.activiti.engine.task.Task parentTask = ActivitiUtil.getTask(parentTaskId);
//                String businessKey = parentTask.getBusinessKey();
//                System.out.println("业务key为：" + businessKey);
//            }
            String businessKey = task.getName();
                System.out.println("业务key为：" + businessKey);
        }
    }

    @Test
    public void testCompleteTask4() {
        String assignee = "zhaoliu";
        ActivitiUtil.completeTask(assignee);
    }

    @Test
    public void testCompleteTask2() {
        String assignee = "lisi";
        ActivitiUtil.completeTask(assignee);
    }

    @Test
    public void testCompleteTask3() {
        String assignee = "wangwu";
        ActivitiUtil.completeTask(assignee);
    }

    /**
     * 取得当前任务的下一个任务
     */
    @Test
    public void getNextTask() throws Exception {
        String assignee = "zhangsan";
        Task task = ActivitiUtil.getTaskList(assignee, 0, 10).getContent().get(0);
        ActivitiUtil.getNextUserFlowElement(task);
    }

    @Test
    public void geProcessDiagram() throws FileNotFoundException {
//        String assignee = "wangwu";
//        Task task = ActivitiUtil.getTaskList(assignee, 0, 10).getContent().get(0);
//        String processInstanceId = task.getProcessInstanceId();
        String processInstanceId = "75c80ccb-5dc0-11ea-9c53-1c1b0d7b318e";
        String filepath = "F:\\" + processInstanceId + ".png";
        ActivitiUtil.getFlowImgByInstanceId(processInstanceId, new FileOutputStream(filepath), true);
        System.out.println("图片生成成功");
    }

    @Test
    public void getAllFlowElements() {
        String definitionId = "myExclusiveGateway:1:4b156648-5d03-11ea-a956-1c1b0d7b318e";
        String processInstanceId = "7fe8e2bd-5d35-11ea-bd4b-1c1b0d7b318e";
        List<MyTaskDto> allFlowElements = ActivitiUtil.getAllFlowElements(definitionId, processInstanceId);
        for (MyTaskDto myTaskDto : allFlowElements) {
            System.out.println(myTaskDto.getTaskId() + " " + myTaskDto.getTaskName() + " " + myTaskDto.getTaskStatus());
        }
    }
}
