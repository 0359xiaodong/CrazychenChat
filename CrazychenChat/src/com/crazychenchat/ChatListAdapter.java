package com.crazychenchat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatListAdapter extends BaseAdapter{
	Context context;	
	ArrayList<Message> messages;
	
	static class ListCell{
		TextView textView;
		TextView currentTime;
		ImageView img;
		TextView name;
		
		public ListCell(TextView textView,TextView currentTime,ImageView img,TextView name) {
			this.textView = textView;
			this.currentTime = currentTime;
			this.img = img;
			this.name = name;
		}
		
		public ImageView getImg() {
			return img;
		}
		
		public void setImg(ImageView img) {
			this.img = img;
		}
		
		public void setTextView(TextView textView) {
			this.textView = textView;
		}
		
		public TextView getTextView() {
			return textView;
		}
		
		public void setCurrentTime(TextView currentTime) {
			this.currentTime = currentTime;
		}
		
		public TextView getCurrentTime() {
			return currentTime;
		}
		
		public TextView getName() {
			return name;
		}
		
		public void setName(TextView name) {
			this.name = name;
		}
	}
	
	
	public ChatListAdapter(Context context,ArrayList<Message> messages) {
		this.context = context;
		this.messages = messages;
	}
	
	public synchronized void add(Message msg){
		messages.add(msg);
	}
	
	
	@Override
	public int getCount() {
		return messages.size();
	}

	@Override
	public Object getItem(int position) {
		return messages.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {		
		
		Message msg = messages.get(position);		
		if(msg.getType() == 0){
			convertView = LayoutInflater.from(context).inflate(R.layout.main_chat_send_msg, null);
			convertView.setTag(new ListCell(
					(TextView)convertView.findViewById(R.id.chat_send_content),
					(TextView)convertView.findViewById(R.id.chat_send_createDate),
					(ImageView)convertView.findViewById(R.id.chat_send_icon),
					(TextView)convertView.findViewById(R.id.chat_send_name)));
		}else if(msg.getType() ==1){
			convertView = LayoutInflater.from(context).inflate(R.layout.main_chat_from_msg, null);
			convertView.setTag(new ListCell(
					(TextView)convertView.findViewById(R.id.chat_from_content),
					(TextView)convertView.findViewById(R.id.chat_from_createDate),
					(ImageView)convertView.findViewById(R.id.chat_from_icon),
					(TextView)convertView.findViewById(R.id.chat_from_name)));
		}
	
		ListCell lc = (ListCell) convertView.getTag();
		
		lc.getTextView().setText(msg.getMsg());	
		if(msg.getType() == 0){		
			lc.getName().setText(MyApplication.getInstance().getMyname());
			if(MyApplication.getInstance().ischangemy){
				lc.getImg().setImageURI(Uri.fromFile(new File(Config.MyAvatarDir+"my.jpg")));
			}
		}else if(msg.getType() ==1){
			lc.getName().setText(MyApplication.getInstance().getOthername());
			if(MyApplication.getInstance().ischangeother){
				lc.getImg().setImageURI(Uri.fromFile(new File(Config.MyAvatarDir+"other.jpg")));		
			}
		}
		
		if(msg.getChattime() - msg.getLastChatTime() >= Config.DURING){
			lc.getCurrentTime().setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(msg.getChattime())));
		}else{
			lc.getCurrentTime().setVisibility(View.GONE);
		}
		Message.lastTime = System.currentTimeMillis();
	
		return convertView;
	}

}
