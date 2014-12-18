package com.crazychenchat.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;

import com.crazychenchat.Config;

public class NetConnection {
	
	public NetConnection(
			final String url,
			final HttpMethod method,
			final SuccessCallback successCallback,
			final FailCallback failCallback,
			final String ... kvs) {
		
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				StringBuffer paramsStr = new StringBuffer();
				for(int i=0; i<kvs.length; i+=2){
					paramsStr.append(kvs[i]).append("=").append(kvs[i+1]).append("&");
				}
				paramsStr.deleteCharAt(paramsStr.length()-1);
				
				try {
					URLConnection uc = null;
					switch(method){
					case POST:
						uc = new URL(url).openConnection();
						uc.setReadTimeout(5 * 1000);
						uc.setConnectTimeout(5 * 1000);
						uc.setDoOutput(true);
						BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(uc.getOutputStream(), Config.CHARSET));
						bw.write(paramsStr.toString());
						bw.flush();						
						break;
					default:	
						uc = new URL(url+"?"+paramsStr.toString()).openConnection();
						break;
					}
					
					BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream(),Config.CHARSET));
					String line = null;
					StringBuffer result = new StringBuffer();
					while((line=br.readLine())!=null){
						result.append(line);
					}
					
					return result.toString();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(String result) {
				if(result != null){		
					if(successCallback != null){
						successCallback.onSuccess(result);
					}
				}else{
					if(failCallback != null){
						failCallback.onFail();
					}
				}
				super.onPostExecute(result);				
			}
		}.execute();
		
	}
	
	
	//成功
	public static interface SuccessCallback{
		void onSuccess(String result);		
	}
	
	//失败
	public static interface FailCallback{
		void onFail();
	}
}
