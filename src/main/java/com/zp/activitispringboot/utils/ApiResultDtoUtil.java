package com.zp.activitispringboot.utils;

import com.zp.activitispringboot.bean.ApiResultDto;

/**
 * 消息返回工具类
 *
 * @author lvzichang
 * @date 2020/2/20 11:58
 */
public class ApiResultDtoUtil {

    /**
     * 获取正常的返回结果
     *
     * @return ApiResultDto
     */
    public static ApiResultDto success() {
        return new ApiResultDto().getSuccess();
    }

    /**
     * 获取正常的返回结果
     *
     * @param data 数据
     * @return ApiResultDto
     */
    public static ApiResultDto success(Object data) {
        return new ApiResultDto().getSuccess(data);
    }

    /**
     * 返回错误的信息
     *
     * @param code    code
     * @param message 消息
     * @return ApiResultDto
     */
    public static ApiResultDto error(String code, String message) {
        return new ApiResultDto().setCode(code).setMessage(message);
    }

    /**
     * 自定义返回消息
     *
     * @param msg  消息
     * @param data 数据
     * @return resultCode
     */
    public static ApiResultDto success(String msg, Object data) {
        return new ApiResultDto().getSuccess(data).setMessage(msg);
    }
}
