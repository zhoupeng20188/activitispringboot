package com.zp.activitispringboot.exception;

import com.zp.activitispringboot.bean.ApiResultDto;
import com.zp.activitispringboot.utils.ApiResultDtoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.ServletException;
import java.util.List;
import java.util.Set;

/**
 * 捕获全局异常返回规定消息给前端
 *
 * @author sqdai
 * @date 2019/7/8 14:16
 */
@RestControllerAdvice
//public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {
public class ExceptionHandlerAdvice {

    /**
     * logger
     */
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 系统异常处理
     *
     * @param e 系统异常
     * @return Result
     */
//    @ExceptionHandler(Exception.class)
//    public ApiResultDto handler(Exception e) {
//    	// 网络异常，请稍后重试
////    	logger.error(ResponseCode.SystemError.getCode(), e);
//    	return new ApiResultDto().getSystemError(e);
//    }

    /**
     * 业务异常处理
     *
     * @param e 业务异常
     * @return Result
     */
    @ExceptionHandler(BizException.class)
    public ApiResultDto handleBizException(BizException e) {
        // 获取异常消息
        return ApiResultDtoUtil.error(e.getCode(), e.getMessage());
    }

    /**
     * 可预料的系统异常处理
     *
     * @param e 可预料的系统异常
     * @return Result
     */
//    @ExceptionHandler(SysException.class)
//    public ApiResultDto handleBizException(SysException e) {
//        // 获取异常消息
//        return ApiResultDtoUtil.error(e.getCode(), e.getMessage());
//    }

    /**
     * 可预料的系统异常处理
     *
     * @param e 可预料的系统异常
     * @return Result
     */
//    @ExceptionHandler(ServletException.class)
//    public ApiResultDto handleServletException(ServletException e) {
//        // 获取异常消息
//        return ApiResultDtoUtil.error("1", e.getMessage());
//    }

//    /**
//     * Customize the response for HttpRequestMethodNotSupportedException.
//     * <p>This method logs a warning, sets the "Allow" header, and delegates to
//     * {@link #handleExceptionInternal}.
//     * @param ex the exception
//     * @param headers the headers to be written to the response
//     * @param status the selected response status
//     * @param request the current request
//     * @return a {@code ResponseEntity} instance
//     */
//    @Override
//    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
//            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//        pageNotFoundLogger.warn(ex.getMessage());
//
//        Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
//        if (!CollectionUtils.isEmpty(supportedMethods)) {
//            headers.setAllow(supportedMethods);
//        }
//        return handleExceptionInternal(ex, ApiResultDtoUtil.error(status.toString(), ex.getMessage()), headers, status, request);
//    }
//
//    /**
//     * Customize the response for HttpMediaTypeNotSupportedException.
//     * <p>This method sets the "Accept" header and delegates to
//     * {@link #handleExceptionInternal}.
//     * @param ex the exception
//     * @param headers the headers to be written to the response
//     * @param status the selected response status
//     * @param request the current request
//     * @return a {@code ResponseEntity} instance
//     */
//    @Override
//    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
//            HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//        List<MediaType> mediaTypes = ex.getSupportedMediaTypes();
//        if (!CollectionUtils.isEmpty(mediaTypes)) {
//            headers.setAccept(mediaTypes);
//        }
//
//        return handleExceptionInternal(ex, ApiResultDtoUtil.error(status.toString(), ex.getMessage()), headers, status, request);
//    }
//
//    /**
//     * Customize the response for HttpMediaTypeNotAcceptableException.
//     * <p>This method delegates to {@link #handleExceptionInternal}.
//     * @param ex the exception
//     * @param headers the headers to be written to the response
//     * @param status the selected response status
//     * @param request the current request
//     * @return a {@code ResponseEntity} instance
//     */
//    @Override
//    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
//            HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//        return handleExceptionInternal(ex, ApiResultDtoUtil.error(status.toString(), ex.getMessage()), headers, status, request);
//    }
//
//    /**
//     * Customize the response for MissingPathVariableException.
//     * <p>This method delegates to {@link #handleExceptionInternal}.
//     * @param ex the exception
//     * @param headers the headers to be written to the response
//     * @param status the selected response status
//     * @param request the current request
//     * @return a {@code ResponseEntity} instance
//     * @since 4.2
//     */
//    @Override
//    protected ResponseEntity<Object> handleMissingPathVariable(
//            MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//        return handleExceptionInternal(ex, ApiResultDtoUtil.error(status.toString(), ex.getMessage()), headers, status, request);
//    }
//
//    /**
//     * Customize the response for MissingServletRequestParameterException.
//     * <p>This method delegates to {@link #handleExceptionInternal}.
//     * @param ex the exception
//     * @param headers the headers to be written to the response
//     * @param status the selected response status
//     * @param request the current request
//     * @return a {@code ResponseEntity} instance
//     */
//    @Override
//    protected ResponseEntity<Object> handleMissingServletRequestParameter(
//            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//        return handleExceptionInternal(ex, ApiResultDtoUtil.error(status.toString(), ex.getMessage()), headers, status, request);
//    }
//
//    /**
//     * Customize the response for ServletRequestBindingException.
//     * <p>This method delegates to {@link #handleExceptionInternal}.
//     * @param ex the exception
//     * @param headers the headers to be written to the response
//     * @param status the selected response status
//     * @param request the current request
//     * @return a {@code ResponseEntity} instance
//     */
//    @Override
//    protected ResponseEntity<Object> handleServletRequestBindingException(
//            ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//        return handleExceptionInternal(ex, ApiResultDtoUtil.error(status.toString(), ex.getMessage()), headers, status, request);
//    }
//
//    /**
//     * Customize the response for ConversionNotSupportedException.
//     * <p>This method delegates to {@link #handleExceptionInternal}.
//     * @param ex the exception
//     * @param headers the headers to be written to the response
//     * @param status the selected response status
//     * @param request the current request
//     * @return a {@code ResponseEntity} instance
//     */
//    @Override
//    protected ResponseEntity<Object> handleConversionNotSupported(
//            ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//        return handleExceptionInternal(ex, ApiResultDtoUtil.error(status.toString(), ex.getMessage()), headers, status, request);
//    }
//
//    /**
//     * Customize the response for TypeMismatchException.
//     * <p>This method delegates to {@link #handleExceptionInternal}.
//     * @param ex the exception
//     * @param headers the headers to be written to the response
//     * @param status the selected response status
//     * @param request the current request
//     * @return a {@code ResponseEntity} instance
//     */
//    @Override
//    protected ResponseEntity<Object> handleTypeMismatch(
//            TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//        return handleExceptionInternal(ex, ApiResultDtoUtil.error(status.toString(), ex.getMessage()), headers, status, request);
//    }
//
//    /**
//     * Customize the response for HttpMessageNotReadableException.
//     * <p>This method delegates to {@link #handleExceptionInternal}.
//     * @param ex the exception
//     * @param headers the headers to be written to the response
//     * @param status the selected response status
//     * @param request the current request
//     * @return a {@code ResponseEntity} instance
//     */
//    @Override
//    protected ResponseEntity<Object> handleHttpMessageNotReadable(
//            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//        return handleExceptionInternal(ex, ApiResultDtoUtil.error(status.toString(), ex.getMessage()), headers, status, request);
//    }
//
//    /**
//     * Customize the response for HttpMessageNotWritableException.
//     * <p>This method delegates to {@link #handleExceptionInternal}.
//     * @param ex the exception
//     * @param headers the headers to be written to the response
//     * @param status the selected response status
//     * @param request the current request
//     * @return a {@code ResponseEntity} instance
//     */
//    @Override
//    protected ResponseEntity<Object> handleHttpMessageNotWritable(
//            HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//        return handleExceptionInternal(ex, ApiResultDtoUtil.error(status.toString(), ex.getMessage()), headers, status, request);
//    }
//
//    /**
//     * Customize the response for MethodArgumentNotValidException.
//     * <p>This method delegates to {@link #handleExceptionInternal}.
//     * @param ex the exception
//     * @param headers the headers to be written to the response
//     * @param status the selected response status
//     * @param request the current request
//     * @return a {@code ResponseEntity} instance
//     */
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(
//            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//        return handleExceptionInternal(ex, ApiResultDtoUtil.error(status.toString(), ex.getMessage()), headers, status, request);
//    }
//
//    /**
//     * Customize the response for MissingServletRequestPartException.
//     * <p>This method delegates to {@link #handleExceptionInternal}.
//     * @param ex the exception
//     * @param headers the headers to be written to the response
//     * @param status the selected response status
//     * @param request the current request
//     * @return a {@code ResponseEntity} instance
//     */
//    @Override
//    protected ResponseEntity<Object> handleMissingServletRequestPart(
//            MissingServletRequestPartException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//        return handleExceptionInternal(ex, ApiResultDtoUtil.error(status.toString(), ex.getMessage()), headers, status, request);
//    }
//
//    /**
//     * Customize the response for BindException.
//     * <p>This method delegates to {@link #handleExceptionInternal}.
//     * @param ex the exception
//     * @param headers the headers to be written to the response
//     * @param status the selected response status
//     * @param request the current request
//     * @return a {@code ResponseEntity} instance
//     */
//    @Override
//    protected ResponseEntity<Object> handleBindException(
//            BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//        return handleExceptionInternal(ex, ApiResultDtoUtil.error(status.toString(), ex.getMessage()), headers, status, request);
//    }
//
//    /**
//     * Customize the response for NoHandlerFoundException.
//     * <p>This method delegates to {@link #handleExceptionInternal}.
//     * @param ex the exception
//     * @param headers the headers to be written to the response
//     * @param status the selected response status
//     * @param request the current request
//     * @return a {@code ResponseEntity} instance
//     * @since 4.0
//     */
//    @Override
//    protected ResponseEntity<Object> handleNoHandlerFoundException(
//            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//
//        return handleExceptionInternal(ex, ApiResultDtoUtil.error(status.toString(), ex.getMessage()), headers, status, request);
//    }
}
