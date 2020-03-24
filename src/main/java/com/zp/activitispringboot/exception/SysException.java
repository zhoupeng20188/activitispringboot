package com.zp.activitispringboot.exception;

/**
 * 可预料的系统异常处理
 *
 * @author JIMO
 */
public class SysException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3649133470079093085L;

	/**
     * 错误码
     */
    private String code;

    /**
     * 错误描述
     */
    private String msg;

    /**
     * 构造方法
     *
     * @param code 错误码
     */
    public SysException(String code) {
        this.code = code;
    }

    /**
     * 构造方法
     *
     * @param code   错误码
     * @param msg 错误描述
     */
    public SysException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
