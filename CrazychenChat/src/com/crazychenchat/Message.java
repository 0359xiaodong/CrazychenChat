package com.crazychenchat;

public class Message {
	private String msg;
	private int type;
	public static long lastTime = System.currentTimeMillis()-Config.DURING*2;
	private final long chattime = System.currentTimeMillis();
	private final long lastChatTime= lastTime;
	
	public Message(String msg,int type) {
		this.msg = msg;
		this.type = type;
	}	
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public long getChattime() {
		return chattime;
	}		
		
	public long getLastChatTime() {
		return lastChatTime;
	}
}
