package com.hw.applogger;

public class RequestLogBean {

	/**
	 * 请求发起时间
	 */
	public String Time;
	/**
	 * 请求地址
	 */
	public String Url;
	/**
	 * 请求参数
	 */
	public String Params;

	/**
	 * 返回信息
	 */
	public String Response;
	
	/**
	 * 请求状态码
	 */
	public int ResponseCode = -1;
	
	/**
	 * 请求是否成功
	 */
	public Boolean IsSuccess = false;

	/**
	 * 额外信息
	 */
	public String Tag;


	@Override
	public String toString() {
		return "RequestLogBean{" +
				"Time='" + Time + '\'' +
				", Url='" + Url + '\'' +
				", Params='" + Params + '\'' +
				", Response='" + Response + '\'' +
				", ResponseCode=" + ResponseCode +
				", IsSuccess=" + IsSuccess +
				", Tag='" + Tag + '\'' +
				'}';
	}
}
