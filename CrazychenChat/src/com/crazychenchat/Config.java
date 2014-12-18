package com.crazychenchat;

import java.io.File;

import android.net.Uri;
import android.os.Environment;


public class Config {
	public static int index = 0;
	public static final String CHARSET = "utf-8";
	public static final String APP_ID = "crazy_chen";
	public static final String SERVER_URL = "http://www.tuling123.com/openapi/api";
	public static final String KEY = "key";
	public static final String INFO = "info";
	public static final String[] API_KEY = {
		"636c1179613c8dbe85a3a8122a6f238e",
		"082da3b56fa7423bd3a375cf15d39d56",
		"2e9aa003732538f5196ad71ed2a16e27",
		"f09ab27814dcb335e4d97cf1e2bc98a2",
		"ebfca905aa95891ae53e6ce36e563f2b"
		};
	public static final String CODE = "code";
	public static final String SUCCESS = "100000";
	public static final String FULL = "40004";
	public static final String TEXT = "text";
	public static final long DURING = 1000 * 30;
	public static final String NAME = "С�";
	
	/**
	 * �ҵ�ͷ�񱣴�Ŀ¼
	 */
	public static String MyAvatarDir = Environment.getExternalStorageDirectory().getPath()+"/chat/avatar/";
	/**
	 * ���ջص�
	 */
	public static final int REQUESTCODE_UPLOADAVATAR_CAMERA = 1;//�����޸�ͷ��
	public static final int REQUESTCODE_UPLOADAVATAR_LOCATION = 2;//��������޸�ͷ��
	public static final int REQUESTCODE_UPLOADAVATAR_CROP = 3;//ϵͳ�ü�ͷ��
	
}
