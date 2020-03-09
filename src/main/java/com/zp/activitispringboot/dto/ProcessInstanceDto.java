package com.zp.activitispringboot.dto;

import lombok.Data;

import java.util.HashMap;

/**
 * @Author zhoupeng
 * @Date 2020-03-09 15:25
 */
@Data
public class ProcessInstanceDto {
    private String user;
    private String processDefinitionKey;
    private String processInstanceName;
    private String businessKey;
    private HashMap variables;
}
