package com.turbo.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Jusven
 * @Date 2021/6/12 21:34
 * 公共返回对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBean {
    private long code;
    private String message;
    private Object obj;

    /**
     * 成功返回结果
     * @return
     */
    public static ResponseBean success(){
        return new ResponseBean(ResponseBeanEnum.SUCCESS.getCode(),ResponseBeanEnum.SUCCESS.getMessage(),null);
    }

    public static ResponseBean success(Object obj){
        return new ResponseBean(ResponseBeanEnum.SUCCESS.getCode(),ResponseBeanEnum.SUCCESS.getMessage(),obj);
    }

    /**
     * 失败返回结果
     * @return
     */
    public static ResponseBean error(ResponseBeanEnum responseBeanEnum){
        return new ResponseBean(responseBeanEnum.getCode(),responseBeanEnum.getMessage(),null);
    }

    public static ResponseBean error(ResponseBeanEnum responseBeanEnum, Object obj){
        return new ResponseBean(responseBeanEnum.getCode(),responseBeanEnum.getMessage(),obj);
    }
}
