# activitispringboot
* 此工程为activiti7与springboot整合
* activiti7默认采用了spring security来做权限控制
* bpmn文件放在resources/processes目录下可以自动部署
* activiti7自动生成的表里没有历史表，需要开启配置
* 7.1.0.M6开始加入了全新的版本控制逻辑，需要在default-project.json文件里指定版本，不然启动实例会报错
# 流程图
* springboot01.bpmn candidateGroup为官方示例中的activitiTeam
* springboot01.bpmn candidateGroup为自定义的firstGroup和secondGroup
# 问题点
* activiti7的自动识别processes下的bpmn文件，但是只会识别第一次，如果再次新建一个bpmn文件，再执行代码数据库里不会增加记录
* =》待确认 =》activiti7读的是target/classes下的processes里的bpmn文件，需要重新打包才会读取最新的bpmn文件
* 上面结论不正确，看了源码暂时也没搞清楚怎么回事
* 解决方法：用7之前的deploy方法来部署