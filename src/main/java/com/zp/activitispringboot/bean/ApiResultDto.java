package com.zp.activitispringboot.bean;


import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * <p>Title: ApiResultDto</p>
 * <p>Description: 封装接口返回值</p>
 * @author JIMO
 * @date 2020年3月2日
 */
public class ApiResultDto implements Serializable {
	
	/** 序列化 */
	private static final long serialVersionUID = 1L;

	/** 返回编码 */
	private String code;
	
	/** 返回消息 */
	private String message;
	
	/** 返回数据 */
	private Object data;

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 * @return this;
	 */
	public ApiResultDto setCode(String code) {
		this.code = code;
		return this;
	}

	/**
	 * @return the data
	 */
	public Object getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 * @return this;
	 */
	public ApiResultDto setData(Object data) {
		this.data = data;
		return this;
	}

	/**
	 * json序列化
	 * @return json字符串;
	 */
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public ApiResultDto setMessage(String message) {
		this.message = message;
		return this;
	}

    /**  
     * Title: getSystemError
     * Description: 系统错误消息
     * @param data 附加返回数据
     */
    public ApiResultDto getSystemError(Object... data) {
        if (data != null && data.length >= 1) {
            this.setData(data[0]);
        }
        return this.setCode("1")
                .setMessage("系统错误");
    }

    /**
     * Title: getSuccess
     * Description: 成功消息
     * @param data 附加返回数据
     * @return
     */
    public ApiResultDto getSuccess(Object... data) {
        if (data != null && data.length >= 1) {
            this.setData(data[0]);
        }
        return this.setCode("0")
                .setMessage("操作成功");
    }
}
