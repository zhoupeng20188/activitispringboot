package com.zp.activitispringboot.exception;

/**
 * 业务异常
 *
 * @author huangyong
 * @date 2019/7/23 10:04
 */
public class BizException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3649133470079093085L;

	/**
     * 错误码
     */
    private String code;

    /**
     * 参数集合
     */
    private String[] params;

    /**
     * 无参数构造
     *
     * @param code 错误码
     */
    public BizException(String code) {
        this.code = code;
    }

    /**
     * 可变参数构造
     *
     * @param code   错误码
     * @param params 参数集合
     */
    public BizException(String code, String... params) {
        this.code = code;
        this.params = params;
    }

    public String getCode() {
        return code;
    }

    public String[] getParams() {
        return params;
    }
}
