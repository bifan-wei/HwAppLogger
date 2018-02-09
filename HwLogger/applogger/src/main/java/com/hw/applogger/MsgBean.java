package com.hw.applogger;

public class MsgBean {
	public String ClassName;
	public String Level = LogLevel.Info;
	public String Tag;
	public String Time;
	public String Message;

	@Override
	public String toString() {
		return "ExceptionBean{" +
				"ClassName='" + ClassName + '\'' +
				", Level='" + Level + '\'' +
				", Tag='" + Tag + '\'' +
				", Time='" + Time + '\'' +
				", Message='" + Message + '\'' +
				'}';
	}
}
