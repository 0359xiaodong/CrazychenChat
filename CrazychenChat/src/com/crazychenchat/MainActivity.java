package com.crazychenchat;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.crazychenchat.net.GetMessage;
import com.mediav.ads.sdk.adcore.Mvad;
import com.mediav.ads.sdk.interfaces.IMvInterstitialAd;

public class MainActivity extends Activity{
	ListView chatList;
	ChatListAdapter chatListAdapter;
	ArrayList<Message> messages;
	Button send_btn;
	EditText chat_msg;
	Button setting;
	Button about;
	private long exitTime = 0;
	public static MainActivity mainActivity;
	TextView title;
	boolean isFirst = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
		mainActivity = this;						
		init();
		final String adSpaceid = "ak56uHtjYC";
		IMvInterstitialAd interstitialAd = Mvad.showInterstitial(this, adSpaceid, false);
		interstitialAd.showAds(this);
	}
	
	private void init(){	
		title = (TextView) findViewById(R.id.title);
		title.setText(MyApplication.getInstance().getOthername());
		
		chatList  = (ListView) findViewById(R.id.chatlist);
		send_btn = (Button) findViewById(R.id.send_btn);
		chat_msg = (EditText) findViewById(R.id.chat_msg);
		messages = new ArrayList<Message>();
		chatListAdapter = new ChatListAdapter(this,messages);
		chatList.setAdapter(chatListAdapter);
		
		Message msg = new Message("#^_^#主人你好,我是"+MyApplication.getInstance().getOthername()+"。输入“问：你好答：你也好”,"+MyApplication.getInstance().getOthername()+"就会记住哦。",1);
		chatListAdapter.add(msg);	
		
		setting  = (Button) findViewById(R.id.setting);
		setting.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, SettingActivity.class));
			}
		});
		
		
		about = (Button) findViewById(R.id.about);
		about.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(MainActivity.this, "开发者：crazychen\n联系邮箱:835127729@qq.com\nHow can I refuse a girl sweet like you.", Toast.LENGTH_LONG).show();
			}
		});
		
		// 关闭软键盘
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(chat_msg.getWindowToken(), 0);
			
	}
		
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {				
		if(keyCode == event.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){			
			 if((System.currentTimeMillis()-exitTime) > 2000){  
		            Toast.makeText(getApplicationContext(), "再按一次跟"+MyApplication.getInstance().getOthername()+"说再见", Toast.LENGTH_SHORT).show();                                
		            exitTime = System.currentTimeMillis();  
		        } else {
		        	SharedPreferences sp = getSharedPreferences("save", MODE_PRIVATE);
		        	Editor ed = sp.edit();
		        	ed.putInt("index", Config.index);
		        	ed.commit();
		            finish();
		            System.exit(0);
		        }
		        return true;   
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
    @Override  
    public boolean dispatchTouchEvent(MotionEvent ev) {  
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {          	
            View v = getCurrentFocus();  
            if (isShouldHideInput(v, ev)) {  
      
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
                if (imm != null) {  
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
                }  
            }  
            return super.dispatchTouchEvent(ev);  
        }  
        // 必不可少，否则所有的组件都不会有TouchEvent了  
        if (getWindow().superDispatchTouchEvent(ev)) {  
            return true;  
        }  
        return onTouchEvent(ev);  
    }  
	
    public  boolean isShouldHideInput(View v, MotionEvent event) {  
        if (v != null && (v instanceof EditText)) { 
            int[] leftTop = { 0, 0 };  
            //获取输入框当前的location位置  
            ((View) v.getParent()).getLocationInWindow(leftTop);  
            int left = leftTop[0];  
            int top = leftTop[1];  
            int bottom = top + ((View) v.getParent()).getHeight();  
            int right = left + ((View) v.getParent()).getWidth();  
            if (event.getX() > left && event.getX() < right  
                    && event.getY() > top && event.getY() < bottom) {  
                // 点击的是输入框区域，保留点击EditText的事件  
                return false;  
            } else {  
                return true;  
            }  
        }  
        return false;  
    }  
    
	public void sendMsg(View view){		
		String content = chat_msg.getText().toString();
		chat_msg.setText("");
		if("".equals(content) || content == null){
			Toast.makeText(this, "说点什么吧,主人", Toast.LENGTH_SHORT).show();
			return;
		}
		
		chatListAdapter.add(new Message(content, 0));
		chatListAdapter.notifyDataSetChanged();
		chatList.setSelection(messages.size() - 1);
		
		try {
			new GetMessage(content, new GetMessage.SuccessCallBack() {
				
				@Override
				public void onSuccess(Message msg) {
					chatListAdapter.add(msg);
					chatListAdapter.notifyDataSetChanged();
					chatList.setSelection(messages.size() - 1);
				}
			}, new GetMessage.FailCallBack() {
				
				@Override
				public void onFail(Message msg) {
					chatListAdapter.add(msg);			
					chatListAdapter.notifyDataSetChanged();
					chatList.setSelection(messages.size() - 1);
				}
			});
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
	}
	
}
