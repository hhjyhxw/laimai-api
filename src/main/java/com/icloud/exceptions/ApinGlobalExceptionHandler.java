package com.icloud.exceptions;

import com.alibaba.fastjson.JSONObject;
import com.icloud.basecommon.model.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * API全局异常处理
 */
@ControllerAdvice(basePackages="com.icloud.api")
public class ApinGlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApinGlobalExceptionHandler.class);


	@ExceptionHandler(ApiException.class)
	@ResponseBody
	public ApiResponse HandleServiceException(ApiException e){
        ApiResponse response = new ApiResponse();
        response.setCode(10005);
        response.setMessage(e.getMessage());
       response.setData(false);
        String result = JSONObject.toJSONString(response);
        logger.info("[用户请求] ApiException, response=" + JSONObject.toJSONString(result));
		return response;
	}

    /**
     * 默认异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiResponse HandleApiException(Exception e){
        ApiResponse response = new ApiResponse();
        response.setCode(500);
        response.setMessage(e.getMessage());
        response.setData(false);
        String result = JSONObject.toJSONString(response);
        logger.info("[用户请求] Exception,response=" + JSONObject.toJSONString(result));
        e.printStackTrace();
        return response;
    }


}
