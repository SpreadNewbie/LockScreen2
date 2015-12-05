package com.example.lockscreen;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class LockScreenService extends Service {
	private Intent mFxLockIntent = null;
	private KeyguardManager mKeyguardManager = null;
	private KeyguardManager.KeyguardLock mKeyguardLock = null;
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("111", "LockScreemService.onCreate");
		mFxLockIntent = new Intent(LockScreenService.this, MainActivity.class);
		mFxLockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		registerComponent();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("111", "LockScreemService.onDestroy");
		unregisterComponent();
		// 被销毁时启动自身，保持自身在后台存活
		startService(new Intent(LockScreenService.this, LockScreenService.class));
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void registerComponent() {
		IntentFilter mScreenOnOrOffFilter = new IntentFilter();
		mScreenOnOrOffFilter.addAction(Intent.ACTION_SCREEN_OFF);//当屏幕锁屏的时候触发  
		mScreenOnOrOffFilter.addAction(Intent.ACTION_SCREEN_ON);//当屏幕解锁的时候触发  
		LockScreenService.this.registerReceiver(mScreenOnOrOffReceiver,
				mScreenOnOrOffFilter);
	}

	// 解除广播监听
	public void unregisterComponent() {
		if (mScreenOnOrOffReceiver != null) {
			LockScreenService.this.unregisterReceiver(mScreenOnOrOffReceiver);
		}
	}

	// 监听来自用户按Power键点亮点暗屏幕的广播
	private BroadcastReceiver mScreenOnOrOffReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i("111", "LockScreemService.mScreenOnOrOffReceiver");
			if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)
					|| intent.getAction().equals(
							Intent.ACTION_SCREEN_ON)) {
				mKeyguardManager = (KeyguardManager) context
						.getSystemService(Context.KEYGUARD_SERVICE);
				mKeyguardLock = mKeyguardManager.newKeyguardLock("FxLock");
				// 屏蔽手机内置的锁屏
				mKeyguardLock.disableKeyguard();
				// 启动该第三方锁屏
				startActivity(mFxLockIntent);
			}
		}
	};
}
