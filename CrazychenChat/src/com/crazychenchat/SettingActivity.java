package com.crazychenchat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.crazychenchat.util.PhotoUtil;
import com.mediav.ads.sdk.adcore.Mvad;
import com.mediav.ads.sdk.interfaces.IMvInterstitialAd;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SettingActivity extends Activity implements OnClickListener{
	RelativeLayout layout_choose;
	RelativeLayout layout_photo;
	PopupWindow avatorPop;
	RelativeLayout layout_all;	
	RelativeLayout img_other;
	RelativeLayout img_my;
	
	EditText edit_other;
	EditText edit_my;
	
	ImageView otherimg;
	ImageView curimg = null;
	String curpath = "";
	ImageView myimg;
	public String filePath = "";
	protected int mScreenWidth;//屏幕宽度
	Button save;
	Button back;
	boolean isFirst = true;
	IMvInterstitialAd interstitialAd;
	final String adSpaceid = "ak56uHtjYC";		

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);	
		init();	
	}		
	
	private void init(){
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		
		Button save = (Button) findViewById(R.id.save);
		Button back = (Button) findViewById(R.id.back);
		
		layout_all = (RelativeLayout) findViewById(R.id.layout_all);

		edit_other = (EditText) findViewById(R.id.edit_other);
		edit_other.setText(MyApplication.getInstance().getOthername());
		
		img_my = (RelativeLayout) findViewById(R.id.img_my);
		myimg = (ImageView) findViewById(R.id.myimg);
		if(MyApplication.getInstance().ischangemy){
			myimg.setImageURI(Uri.fromFile(new File(Config.MyAvatarDir+"my.jpg")));
		}
		
		
		edit_my = (EditText) findViewById(R.id.edit_my);
		edit_my.setText(MyApplication.getInstance().getMyname());
		
		img_other = (RelativeLayout) findViewById(R.id.img_other);
		otherimg = (ImageView) findViewById(R.id.otherimg);
		if(MyApplication.getInstance().ischangeother){
			otherimg.setImageURI(Uri.fromFile(new File(Config.MyAvatarDir+"other.jpg")));	
		}
		
				
		img_other.setOnClickListener(this);
		img_my.setOnClickListener(this);
		
		save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String myname = edit_my.getText().toString();
				if("".equals(myname)){
					Toast.makeText(SettingActivity.this, "不要忘了写自己的名字哦", Toast.LENGTH_SHORT).show();
					return;
				}
				String othername = edit_other.getText().toString();
				if("".equals(othername)){
					Toast.makeText(SettingActivity.this, "不要忘了写别人的名字哦", Toast.LENGTH_SHORT).show();
					return;
				}
				
				SharedPreferences sp = getSharedPreferences("save", MODE_PRIVATE);
				Editor editor = sp.edit();
				editor.putString("myname", myname);
				editor.putString("othername", othername);
				editor.commit();
				MainActivity.mainActivity.title.setText(othername);
				MyApplication.getInstance().setMyname(myname);
				MyApplication.getInstance().setOthername(othername);
				MainActivity.mainActivity.chatListAdapter.notifyDataSetChanged();
				Toast.makeText(SettingActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
			}
		});
		
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void showAvatarPop() {
		View view = LayoutInflater.from(this).inflate(R.layout.pop_showavator, null);
		layout_choose = (RelativeLayout) view.findViewById(R.id.layout_choose);
		layout_photo = (RelativeLayout) view.findViewById(R.id.layout_photo);
		layout_photo.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				layout_choose.setBackgroundColor(getResources().getColor(
						R.color.base_color_text_white));
				layout_photo.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.pop_bg_press));
				File dir = new File(Config.MyAvatarDir);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				// 原图
				File file = new File(dir, new SimpleDateFormat("yyMMddHHmmss")
						.format(new Date()));
				filePath = file.getAbsolutePath();// 获取相片的保存路径
				Uri imageUri = Uri.fromFile(file);

				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent,
						Config.REQUESTCODE_UPLOADAVATAR_CAMERA);
			}
		});
		layout_choose.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub				
				layout_photo.setBackgroundColor(getResources().getColor(
						R.color.base_color_text_white));
				layout_choose.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.pop_bg_press));
				Intent intent = new Intent(Intent.ACTION_PICK, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent,
						Config.REQUESTCODE_UPLOADAVATAR_LOCATION);
			}
		});

		avatorPop = new PopupWindow(view, mScreenWidth, 600);
		avatorPop.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					avatorPop.dismiss();
					return true;
				}
				return false;
			}
		});

		avatorPop.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		avatorPop.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		avatorPop.setTouchable(true);
		avatorPop.setFocusable(true);
		avatorPop.setOutsideTouchable(true);
		avatorPop.setBackgroundDrawable(new BitmapDrawable());
		// 动画效果 从底部弹起
		avatorPop.setAnimationStyle(R.style.Animations_GrowFromBottom);

		avatorPop.showAtLocation(layout_all, Gravity.BOTTOM, 0, 0);
	}

	/**
	 * startImageAction
	 * 
	 * @Title: startImageAction
	 * @return void
	 * @throws
	 */
	private void startImageAction(Uri uri, int outputX, int outputY,
			int requestCode, boolean isCrop) {
		Intent intent = null;
		if (isCrop) {
			intent = new Intent("com.android.camera.action.CROP");
		} else {
			intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		}
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", true);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, requestCode);
	}

	Bitmap newBitmap;
	boolean isFromCamera = false;// 区分拍照旋转
	int degree = 0;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Config.REQUESTCODE_UPLOADAVATAR_CAMERA:// 拍照修改头像
			if (resultCode == RESULT_OK) {
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					//ShowToast("SD不可用");
					return;
				}
				isFromCamera = true;
				File file = new File(filePath);
				degree = PhotoUtil.readPictureDegree(file.getAbsolutePath());
				Log.i("life", "拍照后的角度：" + degree);
				startImageAction(Uri.fromFile(file), 200, 200,
						Config.REQUESTCODE_UPLOADAVATAR_CROP, true);
			}
			break;
		case Config.REQUESTCODE_UPLOADAVATAR_LOCATION:// 本地修改头像
			if (avatorPop != null) {
				avatorPop.dismiss();
			}
			Uri uri = null;
			if (data == null) {
				return;
			}
			if (resultCode == RESULT_OK) {
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					//ShowToast("SD不可用");
					return;
				}
				isFromCamera = false;
				uri = data.getData();
				startImageAction(uri, 200, 200,
						Config.REQUESTCODE_UPLOADAVATAR_CROP, true);
			} else {
				//ShowToast("照片获取失败");
			}

			break;
		case Config.REQUESTCODE_UPLOADAVATAR_CROP:// 裁剪头像返回
			// TODO sent to crop
			if (avatorPop != null) {
				avatorPop.dismiss();
			}
			if (data == null) {
				// Toast.makeText(this, "取消选择", Toast.LENGTH_SHORT).show();
				return;
			} else {
				saveCropAvator(data);
			}
			// 初始化文件路径			
			break;
		default:
			break;

		}
	}
	
	private void updateUserAvatar(String url){	
		ImageLoader.getInstance().displayImage(url,curimg, ImageLoadOptions.getOptions());
	}
	String path;

	/**
	 * 保存裁剪的头像
	 * @param data
	 */
	private void saveCropAvator(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap bitmap = extras.getParcelable("data");
			Log.i("life", "avatar - bitmap = " + bitmap);
			if (bitmap != null) {
				bitmap = PhotoUtil.toRoundCorner(bitmap, 10);
				if (isFromCamera && degree != 0) {
					bitmap = PhotoUtil.rotaingImageView(degree, bitmap);
				}
				curimg.setImageBitmap(bitmap);
				
				path = Config.MyAvatarDir + curpath;
				SharedPreferences sp = getSharedPreferences("save",MODE_PRIVATE);
				Editor editor = sp.edit();
				if("other.jpg".equals(curpath)){
					editor.putBoolean("ischangeother", true);
					MyApplication.getInstance().ischangeother = true;
				}else{
					editor.putBoolean("ischangemy", true);
					MyApplication.getInstance().ischangemy = true;				
				}
				editor.commit();
				PhotoUtil.saveBitmap(Config.MyAvatarDir, curpath,
						bitmap, true);
				MainActivity.mainActivity.chatListAdapter.notifyDataSetChanged();
				// 上传头像
				if (bitmap != null && bitmap.isRecycled()) {
					bitmap.recycle();
				}
			}
		}
	}
	
	@Override
	public void onClick(View v) {	
		interstitialAd = Mvad.showInterstitial(this, adSpaceid, false);		
		interstitialAd.showAds(this);
		switch (v.getId()) {
		case R.id.img_other:
			curimg = otherimg;
			curpath = "other.jpg";
			showAvatarPop();
			break;
		case R.id.img_my:
			curimg = myimg;
			curpath = "my.jpg";
			showAvatarPop();
			break;
		}
	}

}
