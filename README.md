# activitispringboot
* 此工程为activiti7与springboot整合
* activiti7默认采用了spring security来做权限控制
* bpmn文件放在resources/processes目录下可以自动部署
* activiti7自动生成的表里没有历史表，需要开启配置
* 7.1.0.M6开始加入了全新的版本控制逻辑，需要在default-project.json文件里指定版本，不然启动实例会报错