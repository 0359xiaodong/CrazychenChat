package com.crazychenchat.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.crazychenchat.Config;
import com.crazychenchat.Message;
import com.crazychenchat.MyApplication;
import com.crazychenchat.net.NetConnection.FailCallback;
import com.crazychenchat.net.NetConnection.SuccessCallback;

public class GetMessage {
	public GetMessage(String content,
			final SuccessCallBack successCallBack,
			final FailCallBack failCallBack) throws UnsupportedEncodingException {
		new NetConnection(Config.SERVER_URL, HttpMethod.GET, 
			new SuccessCallback() {				
				@Override
				public void onSuccess(String result) {
					try {
						JSONObject jsonObj = new JSONObject(result);
						switch(jsonObj.getString(Config.CODE)){
						case Config.SUCCESS:							
							String text = jsonObj.getString(Config.TEXT).replaceAll(Config.NAME, MyApplication.getInstance().othername);
							Message msg = new Message(text,1);
							successCallBack.onSuccess(msg);
							break;
						case Config.FULL:
							Config.index++;
							if(Config.index >= Config.API_KEY.length) Config.index = 0;
							String text2 = "主人你说什么，再说一遍号码n(*≧▽≦*)n";
							Message msg2 = new Message(text2,1);
							successCallBack.onSuccess(msg2);
							break;
						default:
							System.out.println("失败");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}, 
			new FailCallback() {				
				@Override
				public void onFail() {
					failCallBack.onFail(new Message("服务器异常啊，主人等会再试吧", 1));
				}
			}, Config.KEY,Config.API_KEY[Config.index],Config.INFO,URLEncoder.encode(content, "UTF-8"));
	}		
	
	public static interface SuccessCallBack{
		void onSuccess(Message msg);
	}
	
	public static interface FailCallBack{
		void onFail(Message msg);
	}
}
