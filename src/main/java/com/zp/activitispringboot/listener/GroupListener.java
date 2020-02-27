package com.zp.activitispringboot.listener;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * 会签监听
 * @author xuyl 2019年5月7日 上午11:12:42
 *
 */
//@Component
public class GroupListener implements TaskListener {

	/**
	 * 监听器里不能自动注入runtimeService之类的activiti对象
	 * 需要手动获取
	 */
	//	@Autowired
	//	RuntimeService runtimeService;
	//
	//	@Autowired
	//	TaskService taskService;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateTask delegateTask) {
		System.out.println("会签监听");
		//获取流程id
		String exId = delegateTask.getExecutionId();

		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		RuntimeService runtimeService = engine.getRuntimeService();

		Integer all = (Integer) runtimeService.getVariable(exId, "nrOfInstances");
		Integer complete = (Integer) runtimeService.getVariable(exId, "nrOfCompletedInstances");
		System.out.println("会签完成实例数：" + (complete + 1));
		System.out.println("会签总实例数：" + all);
	}

}