package com.crazychenchat;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;
import android.content.SharedPreferences;

public class MyApplication extends Application{
	public static MyApplication mInstance;	
	public String myname = "无名氏";
	public String othername = "小昕";
	public boolean ischangemy;
	public boolean ischangeother;
	
	public static MyApplication getInstance() {
		return mInstance;
	}
	
	
	@Override
	public void onCreate() {
        ImageLoaderConfiguration configuration = ImageLoaderConfiguration
                .createDefault(this);
        mInstance = this;
       
        ImageLoader.getInstance().init(configuration);
        init();
	}	
	
	private void init(){
		//初始化设置
		SharedPreferences sp = this.getSharedPreferences("save",MODE_PRIVATE);
		myname = sp.getString("myname", "无名氏");
		othername = sp.getString("othername", "小昕");
		ischangemy = sp.getBoolean("ischangemy", false);
		ischangeother = sp.getBoolean("ischangeother", false);
		Config.index = sp.getInt("index", 0);
	}
	
	public String getMyname() {
		return myname;
	}
	
	public void setMyname(String myname) {
		this.myname = myname;
	}
	
	public String getOthername() {
		return othername;
	}
	
	public void setOthername(String othername) {
		this.othername = othername;
	}
}
