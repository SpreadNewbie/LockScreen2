package com.example.lockscreen;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	private String showRemainTime;
	private ImageView battery, iv_line2, iv_lighting, iv_fire;
	private Button unlock;
	private Button phone;
	private Button msg;
	private TextView time_remain1, time_remain2, time_remain3, time_remain4,
			remain_battery;
	private RelativeLayout layout_normal;
	private FrameLayout layout_charging, layout_fast_charging;
	private ImageView iv_icon;
	private Animation myAnimation_Translate, myAnimation_Translate2,
			myAnimation_Alpha;
	// 定义AnimationDrawable动画
	private AnimationDrawable frameAnimation = null;
	// 定义一个Drawable对象
	Drawable mBitAnimation = null;
	Bitmap mBitmap=null;
	public static final int NORMALCHARGING = 1;
	public static final int FASTCHARGING = 2;
	public static final int DISCHARGING = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		/*
		 * this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		 * getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		 * WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 */
		startService(new Intent(MainActivity.this, LockScreenService.class));
		Log.i("111", "MainActivity.onCreate");
		final Vibrator vib = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
		final long[] myp = { 0, 300 };
		// 按钮的监听事件
		DisplayMetrics dm = getResources().getDisplayMetrics();
		final int screenWidth = dm.widthPixels;
		final int screenHeight = dm.heightPixels - 50;
		unlock.setOnTouchListener(new OnTouchListener() {
			int lastX, lastY, downX, downY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int ea = event.getAction();
				int dx = 0, dy = 0;// 记录坐标变化值
				switch (ea) {
				case MotionEvent.ACTION_DOWN:
					// 获取按下去的坐标
					downX = (int) event.getRawX();
					downY = (int) event.getRawY();
					lastX = downX;
					lastY = downY;
					break;
				case MotionEvent.ACTION_MOVE:
					dx = (int) event.getRawX() - lastX;
					dy = (int) event.getRawY() - lastY;

					int l = v.getLeft() + dx;
					int b = v.getBottom() + dy;
					int r = v.getRight() + dx;
					int t = v.getTop() + dy;
					if (l < 0) {
						l = 0;
						r = l + v.getWidth();
					}

					if (t < 0) {
						t = 0;
						b = t + v.getHeight();
					}

					if (r > screenWidth) {
						r = screenWidth;
						l = r - v.getWidth();
					}

					if (b > screenHeight) {
						b = screenHeight;
						t = b - v.getHeight();
					}
					v.layout(l, t, r, b);
					lastX = (int) event.getRawX();
					lastY = (int) event.getRawY();
					Log.e("test", "t" + lastX);
					Log.e("test", "t" + lastY);
					v.postInvalidate();
					break;
				case MotionEvent.ACTION_UP:
					// 通过当按下去以后的坐标和最后的坐标的差绝对值判断是否移动
					if (Math.abs(downX - lastX) > 1
							&& Math.abs(downY - lastY) > 1) {
						vib.vibrate(myp, -1);
						finish();
					}
					break;
				}
				return false;
			}
		});
		phone.setOnTouchListener(new OnTouchListener() {
			int lastX, lastY, downX, downY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int ea = event.getAction();
				int dx = 0, dy = 0;// 记录坐标变化值
				switch (ea) {
				case MotionEvent.ACTION_DOWN:
					// 获取按下去的坐标
					downX = (int) event.getRawX();
					downY = (int) event.getRawY();
					lastX = downX;
					lastY = downY;
					break;
				case MotionEvent.ACTION_MOVE:
					dx = (int) event.getRawX() - lastX;
					dy = (int) event.getRawY() - lastY;

					int l = v.getLeft() + dx;
					int b = v.getBottom() + dy;
					int r = v.getRight() + dx;
					int t = v.getTop() + dy;
					if (l < 0) {
						l = 0;
						r = l + v.getWidth();
					}

					if (t < 0) {
						t = 0;
						b = t + v.getHeight();
					}

					if (r > screenWidth) {
						r = screenWidth;
						l = r - v.getWidth();
					}

					if (b > screenHeight) {
						b = screenHeight;
						t = b - v.getHeight();
					}
					v.layout(l, t, r, b);
					lastX = (int) event.getRawX();
					lastY = (int) event.getRawY();
					Log.e("test", "t" + lastX);
					Log.e("test", "t" + lastY);
					v.postInvalidate();
					break;
				case MotionEvent.ACTION_UP:
					// 通过当按下去以后的坐标和最后的坐标的差绝对值判断是否移动
					if (Math.abs(downX - lastX) > 1
							&& Math.abs(downY - lastY) > 1) {
						vib.vibrate(myp, -1);
						launchPhone();
						finish();
					}
					break;
				}
				return false;
			}
		});
		msg.setOnTouchListener(new OnTouchListener() {
			int lastX, lastY, downX, downY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int ea = event.getAction();
				int dx = 0, dy = 0;// 记录坐标变化值
				switch (ea) {
				case MotionEvent.ACTION_DOWN:
					// 获取按下去的坐标
					downX = (int) event.getRawX();
					downY = (int) event.getRawY();
					lastX = downX;
					lastY = downY;
					break;
				case MotionEvent.ACTION_MOVE:
					dx = (int) event.getRawX() - lastX;
					dy = (int) event.getRawY() - lastY;

					int l = v.getLeft() + dx;
					int b = v.getBottom() + dy;
					int r = v.getRight() + dx;
					int t = v.getTop() + dy;
					if (l < 0) {
						l = 0;
						r = l + v.getWidth();
					}

					if (t < 0) {
						t = 0;
						b = t + v.getHeight();
					}

					if (r > screenWidth) {
						r = screenWidth;
						l = r - v.getWidth();
					}

					if (b > screenHeight) {
						b = screenHeight;
						t = b - v.getHeight();
					}
					v.layout(l, t, r, b);
					lastX = (int) event.getRawX();
					lastY = (int) event.getRawY();
					Log.e("test", "t" + lastX);
					Log.e("test", "t" + lastY);
					v.postInvalidate();
					break;
				case MotionEvent.ACTION_UP:
					// 通过当按下去以后的坐标和最后的坐标的差绝对值判断是否移动
					if (Math.abs(downX - lastX) > 1
							&& Math.abs(downY - lastY) > 1) {
						vib.vibrate(myp, -1);
						launchSms();
						finish();
					}
					break;
				}
				return false;
			}
		});
	}

	private void init() {
		battery = (ImageView) findViewById(R.id.battery);
		time_remain1 = (TextView) findViewById(R.id.time_remain1);
		time_remain2 = (TextView) findViewById(R.id.time_remain2);
		time_remain3 =  (TextView) findViewById(R.id.time_remain3);
		time_remain4 = (TextView) findViewById(R.id.time_remain4);
		remain_battery = (TextView) findViewById(R.id.remain_battery);
		unlock = (Button) findViewById(R.id.ic_lock_24dp);
		phone = (Button) findViewById(R.id.ic_phone_24dp);
		msg = (Button) findViewById(R.id.ic_msg_24dp);
		iv_icon = (ImageView) findViewById(R.id.iv_icon);

		layout_normal = (RelativeLayout) findViewById(R.id.layout_normal);
		layout_charging = (FrameLayout) findViewById(R.id.layout_charging);
		layout_fast_charging = (FrameLayout) findViewById(R.id.layout_fast_charging);

		iv_line2 = (ImageView) findViewById(R.id.iv_line2);
		iv_lighting = (ImageView) findViewById(R.id.iv_lighting);
		iv_fire = (ImageView) findViewById(R.id.iv_fire);

		// 实例化AnimationDrawable对象
		frameAnimation = new AnimationDrawable();

		/* 装载资源 */
		// 这里用一个循环装载所有名字类似的资源
		// 如"a1...........24.png"的图片
//		for (int i = 0; i <= 24; i++) {
//			int id = getResources().getIdentifier("a" + i, "drawable",
//					this.getPackageName());
//			// 此方法返回一个可绘制的对象与特定的资源ID相关联
//			mBitAnimation = getResources().getDrawable(id);
//			/* 为动画添加一帧 */
//			// 参数mBitAnimation是该帧的图片
//			// 参数100是该帧显示的时间，按毫秒计算
//			frameAnimation.addFrame(mBitAnimation, 100);
//		}
		
	
		
//		mBitmap=Utils.readBitMap(MainActivity.this, R.drawable.a0);
//		mBitAnimation=new BitmapDrawable(mBitmap);
//		frameAnimation.addFrame(mBitAnimation, 250);
//		mBitmap=Utils.readBitMap(MainActivity.this, R.drawable.a1);
//		mBitAnimation=new BitmapDrawable(mBitmap);
//		frameAnimation.addFrame(mBitAnimation, 250);
//		mBitmap=Utils.readBitMap(MainActivity.this, R.drawable.a2);
//		mBitAnimation=new BitmapDrawable(mBitmap);
//		frameAnimation.addFrame(mBitAnimation, 250);
//		mBitmap=Utils.readBitMap(MainActivity.this, R.drawable.a3);
//		mBitAnimation=new BitmapDrawable(mBitmap);
//		frameAnimation.addFrame(mBitAnimation, 250);
//		mBitmap=Utils.readBitMap(MainActivity.this, R.drawable.a4);
//		mBitAnimation=new BitmapDrawable(mBitmap);
//		frameAnimation.addFrame(mBitAnimation, 250);
//		mBitmap=Utils.readBitMap(MainActivity.this, R.drawable.a5);
//		mBitAnimation=new BitmapDrawable(mBitmap);
//		frameAnimation.addFrame(mBitAnimation, 250);
//		mBitmap=Utils.readBitMap(MainActivity.this, R.drawable.a6);
//		mBitAnimation=new BitmapDrawable(mBitmap);
//		frameAnimation.addFrame(mBitAnimation, 250);
//		mBitmap=Utils.readBitMap(MainActivity.this, R.drawable.a7);
//		mBitAnimation=new BitmapDrawable(mBitmap);
//		frameAnimation.addFrame(mBitAnimation, 250);
//		mBitmap=Utils.readBitMap(MainActivity.this, R.drawable.a8);
//		mBitAnimation=new BitmapDrawable(mBitmap);
//		frameAnimation.addFrame(mBitAnimation, 250);
//		mBitmap=Utils.readBitMap(MainActivity.this, R.drawable.a9);
//		mBitAnimation=new BitmapDrawable(mBitmap);
//		frameAnimation.addFrame(mBitAnimation, 250);
//		mBitmap=Utils.readBitMap(MainActivity.this, R.drawable.a10);
//		mBitAnimation=new BitmapDrawable(mBitmap);
//		frameAnimation.addFrame(mBitAnimation, 250);
//		mBitmap=Utils.readBitMap(MainActivity.this, R.drawable.a11);
//		mBitAnimation=new BitmapDrawable(mBitmap);
//		frameAnimation.addFrame(mBitAnimation, 250);
//		mBitmap=Utils.readBitMap(MainActivity.this, R.drawable.a12);
//		mBitAnimation=new BitmapDrawable(mBitmap);
//		frameAnimation.addFrame(mBitAnimation, 250);
//		mBitmap=Utils.readBitMap(MainActivity.this, R.drawable.a13);
//		mBitAnimation=new BitmapDrawable(mBitmap);
//		frameAnimation.addFrame(mBitAnimation, 250);
//		mBitmap=Utils.readBitMap(MainActivity.this, R.drawable.a14);
//		mBitAnimation=new BitmapDrawable(mBitmap);
//		frameAnimation.addFrame(mBitAnimation, 250);
//		mBitmap=Utils.readBitMap(MainActivity.this, R.drawable.a15);
//		mBitAnimation=new BitmapDrawable(mBitmap);
//		frameAnimation.addFrame(mBitAnimation, 250);
//		mBitmap=Utils.readBitMap(MainActivity.this, R.drawable.a16);
//		mBitAnimation=new BitmapDrawable(mBitmap);
//		frameAnimation.addFrame(mBitAnimation, 250);
//		mBitmap=Utils.readBitMap(MainActivity.this, R.drawable.a17);
//		mBitAnimation=new BitmapDrawable(mBitmap);
//		frameAnimation.addFrame(mBitAnimation, 250);
//		mBitmap=Utils.readBitMap(MainActivity.this, R.drawable.a18);
//		mBitAnimation=new BitmapDrawable(mBitmap);
//		frameAnimation.addFrame(mBitAnimation, 250);
//		mBitmap=Utils.readBitMap(MainActivity.this, R.drawable.a19);
//		mBitAnimation=new BitmapDrawable(mBitmap);
//		frameAnimation.addFrame(mBitAnimation, 250);
//		mBitmap=Utils.readBitMap(MainActivity.this, R.drawable.a20);
//		mBitAnimation=new BitmapDrawable(mBitmap);
//		frameAnimation.addFrame(mBitAnimation, 250);
//		mBitmap=Utils.readBitMap(MainActivity.this, R.drawable.a21);
//		mBitAnimation=new BitmapDrawable(mBitmap);
//		frameAnimation.addFrame(mBitAnimation, 250);
//		mBitmap=Utils.readBitMap(MainActivity.this, R.drawable.a22);
//		mBitAnimation=new BitmapDrawable(mBitmap);
//		frameAnimation.addFrame(mBitAnimation, 250);
//		mBitmap=Utils.readBitMap(MainActivity.this, R.drawable.a23);
//		mBitAnimation=new BitmapDrawable(mBitmap);
//		frameAnimation.addFrame(mBitAnimation, 250);
//		mBitmap=Utils.readBitMap(MainActivity.this, R.drawable.a24);
//		mBitAnimation=new BitmapDrawable(mBitmap);
//		frameAnimation.addFrame(mBitAnimation, 250);
		
		
//		frameAnimation.addFrame(getResources().getDrawable(R.drawable.a0), 250);
//		frameAnimation.addFrame(getResources().getDrawable(R.drawable.a1), 250);
//		frameAnimation.addFrame(getResources().getDrawable(R.drawable.a2), 250);
//		frameAnimation.addFrame(getResources().getDrawable(R.drawable.a3), 250);
//		frameAnimation.addFrame(getResources().getDrawable(R.drawable.a4), 250);
//		frameAnimation.addFrame(getResources().getDrawable(R.drawable.a5), 250);
//		frameAnimation.addFrame(getResources().getDrawable(R.drawable.a6), 250);
//		frameAnimation.addFrame(getResources().getDrawable(R.drawable.a7), 250);
//		frameAnimation.addFrame(getResources().getDrawable(R.drawable.a8), 250);
//		frameAnimation.addFrame(getResources().getDrawable(R.drawable.a9), 250);
//		frameAnimation.addFrame(getResources().getDrawable(R.drawable.a10), 250);
//		frameAnimation.addFrame(getResources().getDrawable(R.drawable.a11), 250);
//		frameAnimation.addFrame(getResources().getDrawable(R.drawable.a12), 250);
//		frameAnimation.addFrame(getResources().getDrawable(R.drawable.a13), 250);
//		frameAnimation.addFrame(getResources().getDrawable(R.drawable.a14), 250);
//		frameAnimation.addFrame(getResources().getDrawable(R.drawable.a15), 250);
//		frameAnimation.addFrame(getResources().getDrawable(R.drawable.a16), 250);
//		frameAnimation.addFrame(getResources().getDrawable(R.drawable.a17), 250);
//		frameAnimation.addFrame(getResources().getDrawable(R.drawable.a18), 50);
//		frameAnimation.addFrame(getResources().getDrawable(R.drawable.a19), 250);
//		frameAnimation.addFrame(getResources().getDrawable(R.drawable.a20), 250);
//		frameAnimation.addFrame(getResources().getDrawable(R.drawable.a21), 250);
//		frameAnimation.addFrame(getResources().getDrawable(R.drawable.a22), 250);
//		frameAnimation.addFrame(getResources().getDrawable(R.drawable.a23), 250);
//		frameAnimation.addFrame(getResources().getDrawable(R.drawable.a24), 250);
		/*
		 * 上边用到了Resources的getIdentifier方法 方法返回一个资源的唯一标识符，如果没有这个资源就返回0
		 * 0不是有效的标识符，在说说这个方法几个参数的含义 第一个 就是我们的资源名称了。 第二个 就是我们要去哪里找我们的资源
		 * 我们的图片在drawable 下 所以为drawable 第三个 我们用了Context的getPackageName返回应用程序的包名
		 */

//		// 设置播放模式是否循环播放，false表示循环，true表示不循环
//		frameAnimation.setOneShot(false);
//
//		// 设置本类将要显示的这个动画
//		iv_fire.setBackgroundDrawable(frameAnimation);
		// 动态注册广播
		IntentFilter mBatteryFilter = new IntentFilter();
		mBatteryFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(mBatteryReceiver, mBatteryFilter);
	}

	private void setResource(int battery_remain) {
		if (battery_remain <= 0) {
			battery.setImageResource(R.drawable.battery0);
		} else if (battery_remain <= 20 && battery_remain > 0) {
			battery.setImageResource(R.drawable.battery20);
		} else if (battery_remain <= 40 && battery_remain > 20) {
			battery.setImageResource(R.drawable.battery40);
		} else if (battery_remain <= 60 && battery_remain > 40) {
			battery.setImageResource(R.drawable.battery60);
		} else if (battery_remain <= 80 && battery_remain > 60) {
			battery.setImageResource(R.drawable.battery80);
		} else if (battery_remain <= 100 && battery_remain > 80) {
			battery.setImageResource(R.drawable.battery100);
		}
	}

	private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("111", "MainActivity.mBatteryReceiver");
			if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
				// 获取当前电量
				int level = intent.getIntExtra("level", 0);
				WaveView waveView1 = (WaveView) findViewById(R.id.WaveView1);
				WaveView waveView2 = (WaveView) findViewById(R.id.WaveView2);
				waveView1.setParamater(level, 5f);
				waveView2.setParamater(level,2.5f);
				// 电量的总刻度
				int scale = intent.getIntExtra("scale", 100);
				// 电池是否充电状态
				int status = intent.getIntExtra("status",
						BatteryManager.BATTERY_STATUS_UNKNOWN);
				int battery_remain = level * 100 / scale;
				if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
					layout_normal.setVisibility(View.INVISIBLE);
					remain_battery.setVisibility(View.INVISIBLE);
					time_remain3.setVisibility(View.VISIBLE);
					time_remain4.setVisibility(View.VISIBLE);
					int chargePlug = intent.getIntExtra(
							BatteryManager.EXTRA_PLUGGED, -1);
					// boolean usbCharge = chargePlug ==
					// BatteryManager.BATTERY_PLUGGED_USB;
					// boolean acCharge = chargePlug ==
					// BatteryManager.BATTERY_PLUGGED_AC;

					if (chargePlug == BatteryManager.BATTERY_PLUGGED_USB) {
						showRemainTime = caclTime(NORMALCHARGING,
								battery_remain);
						layout_charging.setVisibility(View.VISIBLE);
						layout_fast_charging.setVisibility(View.INVISIBLE);
					} else {
						showRemainTime = caclTime(FASTCHARGING, battery_remain);
						layout_fast_charging.setVisibility(View.VISIBLE);
						layout_charging.setVisibility(View.INVISIBLE);
						myAnimation_Translate2 = AnimationUtils.loadAnimation(
								MainActivity.this, R.anim.my_translate_action);
						myAnimation_Alpha = AnimationUtils.loadAnimation(
								MainActivity.this, R.anim.my_alpha_action);
						myAnimation_Translate2
								.setAnimationListener(new AnimationListener() {

									@Override
									public void onAnimationStart(
											Animation animation) {

									}

									@Override
									public void onAnimationRepeat(
											Animation animation) {

									}

									@Override
									public void onAnimationEnd(
											Animation animation) {
										iv_lighting.setVisibility(View.VISIBLE);
										iv_lighting
												.startAnimation(myAnimation_Alpha);
										iv_fire.setVisibility(View.VISIBLE);
//										frameAnimation.start();
										
										iv_fire.setBackgroundResource(R.anim.anim_resource); 
										frameAnimation = (AnimationDrawable) iv_fire.getBackground(); 
										frameAnimation.start();
									}
								});
						iv_line2.startAnimation(myAnimation_Translate2);
					}
					time_remain3.setText(showRemainTime);
					time_remain4.setText("to fully-Charge");

					myAnimation_Translate = AnimationUtils.loadAnimation(
							MainActivity.this, R.anim.my_translate_action);
					myAnimation_Translate
							.setAnimationListener(new AnimationListener() {

								@Override
								public void onAnimationStart(Animation animation) {

								}

								@Override
								public void onAnimationRepeat(
										Animation animation) {

								}

								@Override
								public void onAnimationEnd(Animation animation) {
									iv_icon.startAnimation(myAnimation_Translate);
								}
							});
					iv_icon.startAnimation(myAnimation_Translate);
				} else {
					layout_charging.setVisibility(View.INVISIBLE);
					layout_fast_charging.setVisibility(View.INVISIBLE);
					layout_normal.setVisibility(View.VISIBLE);
					remain_battery.setVisibility(View.VISIBLE);
					time_remain3.setVisibility(View.INVISIBLE);
					time_remain4.setVisibility(View.INVISIBLE);
					setResource(battery_remain);
					showRemainTime = caclTime(DISCHARGING, battery_remain);
					// 把它转成百分比
					time_remain1.setText(showRemainTime);
					time_remain2.setText("Remaining");
					time_remain2.setTextColor(Color.GRAY);
					remain_battery.setText(battery_remain + "%");
					remain_battery.setVisibility(View.VISIBLE);
				}
			}
		}
	};

	private String caclTime(int chargingOrDischarging, int battery_remain) {
		String string = null;
		int total_hours = 0;
		int total_minutes = 0;
		int hours = 0;
		int minutes = 0;
		int days = 0;
		switch (chargingOrDischarging) {
		case NORMALCHARGING:
			total_minutes = (int) (((int) (3600 - (battery_remain * 3600) / 100) * 60) / 500);// 普通充电电流500mA
			hours = total_minutes / 60;
			minutes = total_minutes % 60;
			string = hours + "-hour   " + minutes + "-minute";
			break;
		case FASTCHARGING:
			total_minutes = (int) (((int) (3600 - (battery_remain * 3600) / 100) * 60) / 1000);// 快速充电电流1000mA
			hours = total_minutes / 60;
			minutes = total_minutes % 60;
			string = hours + "-hour   " + minutes + "-minute";
			break;
		case DISCHARGING:
			total_hours = (int) ((int) (battery_remain * 3600) / 100 / 9.2);// 电池容量3600mAh
																			// 待机电流9。2mA
			hours = total_hours % 24;
			days = total_hours / 24;
			string = days + " -day   " + hours + "-hour";
			break;
		}

		return string;
	}

	// 启动短信应用
	private void launchSms() {
		Intent intent = new Intent();
		ComponentName comp = new ComponentName("com.android.mms",
				"com.android.mms.ui.ConversationList");
		intent.setComponent(comp);
		intent.setAction("android.intent.action.VIEW");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		startActivity(intent);
	}

	// 启动拨号应用
	private void launchPhone() {
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		startActivity(intent);
	}

	// 使back键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return disableKeycode(keyCode, event);
	}

	private boolean disableKeycode(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("111", "MainActivity.onDestroy");
		unregisterReceiver(mBatteryReceiver);
	}
}
