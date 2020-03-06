package com.zp.activitispringboot.cmd;

import org.activiti.bpmn.model.Activity;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.MultiInstanceLoopCharacteristics;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.ParallelMultiInstanceBehavior;
import org.activiti.engine.impl.history.HistoryManager;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntityManager;
import org.activiti.engine.impl.util.ProcessDefinitionUtil;

public class AddMultiInstanceCmd implements Command {
    protected final String NUMBER_OF_INSTANCES = "nrOfInstances";
    protected final String NUMBER_OF_ACTIVE_INSTANCES = "nrOfActiveInstances";
    private String taskId;
    private String addUserTaskAssign;

    public AddMultiInstanceCmd(String taskId, String addUserTaskAssign) {
        this.taskId = taskId;
        this.addUserTaskAssign = addUserTaskAssign;
    }


    @Override
    public Object execute(CommandContext commandContext) {

        TaskEntityManager taskEntityManager = commandContext.getTaskEntityManager();
        ExecutionEntityManager executionEntityManager = commandContext.getExecutionEntityManager();

        //根据任务id获取任务实例
        TaskEntity taskEntity = taskEntityManager.findById(taskId);
        //根据执行实例ID获取当前执行实例
        ExecutionEntity multiExecutionEntity = executionEntityManager.findById(taskEntity.getExecutionId());

        // 获取流程执行实例（即当前执行实例的父实例）
        ExecutionEntity parentExecutionEntity = multiExecutionEntity.getParent();

        //判断当前执行实例的节点是否是多实例节点
        BpmnModel bpmnModel = ProcessDefinitionUtil.getBpmnModel(multiExecutionEntity.getProcessDefinitionId());
        Activity miActivityElement = (Activity) bpmnModel.getFlowElement(multiExecutionEntity.getCurrentActivityId());
        MultiInstanceLoopCharacteristics loopCharacteristics = miActivityElement.getLoopCharacteristics();
        if(loopCharacteristics == null){
            throw new ActivitiException("此节点不是多实例节点");
        }
        //判断是否是并行多实例
        if(loopCharacteristics.isSequential()){
            throw new ActivitiException("此节点为串行节点");
        }
        //创建新的子实例
        ExecutionEntity childExecution = executionEntityManager.createChildExecution(parentExecutionEntity);
        //获取并为新的执行实例设置当前活动节点
        UserTask currentFlowElement = (UserTask) multiExecutionEntity.getCurrentFlowElement();
        //设置处理人
        currentFlowElement.setAssignee(addUserTaskAssign);
        childExecution.setCurrentFlowElement(currentFlowElement);

        //获取设置变量
        Integer nrOfInstances = (Integer) parentExecutionEntity.getVariableLocal(NUMBER_OF_INSTANCES);
        Integer nrOfActiveInstances = (Integer) parentExecutionEntity.getVariableLocal(NUMBER_OF_ACTIVE_INSTANCES);

        parentExecutionEntity.setVariableLocal(NUMBER_OF_INSTANCES,nrOfInstances+1);
        parentExecutionEntity.setVariableLocal(NUMBER_OF_ACTIVE_INSTANCES,nrOfActiveInstances+1);

        //通知活动开始
        HistoryManager historyManager = commandContext.getHistoryManager();
        historyManager.recordActivityStart(childExecution);
        //获取处理行为类
        ParallelMultiInstanceBehavior prallelMultiInstanceBehavior = (ParallelMultiInstanceBehavior) miActivityElement.getBehavior();
        AbstractBpmnActivityBehavior innerActivityBehavior = prallelMultiInstanceBehavior.getInnerActivityBehavior();
        //执行
        innerActivityBehavior.execute(childExecution);
        return null;
    }

}