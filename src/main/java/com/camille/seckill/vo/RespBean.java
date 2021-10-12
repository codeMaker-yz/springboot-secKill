package com.camille.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {

	private long code;
	private String message;
	private Object obj;


	public static RespBean success(){
		return new RespBean(RespBeanEnum.SUCCESS.getCode(),RespBeanEnum.SUCCESS.getMessage(),null);
	}


	public static RespBean success(Object obj){
		return new RespBean(RespBeanEnum.SUCCESS.getCode(),RespBean.success().getMessage(),obj);
	}



	public static RespBean error(RespBeanEnum respBeanEnum){
		return new RespBean(respBeanEnum.getCode(),respBeanEnum.getMessage(),null);
	}



	public static RespBean error(RespBeanEnum respBeanEnum,Object obj){
		return new RespBean(respBeanEnum.getCode(),respBeanEnum.getMessage(),obj);
	}

}