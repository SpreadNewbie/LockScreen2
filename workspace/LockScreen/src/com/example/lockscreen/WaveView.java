package com.example.lockscreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * ˮ�������ؼ�
 * 
 * 
 */
public class WaveView extends View {

	private int mViewWidth;
	private int mViewHeight;

	/**
	 * ˮλ��
	 */
	private float mLevelLine;

	/**
	 * �����������
	 */
	private float mWaveHeight = 80;
	/**
	 * ����
	 */
	private float mWaveWidth = 200;
	/**
	 * �����ص�����ߵĲ���
	 */
	private float mLeftSide;

	private float mMoveLen;
	
	private int currentBattery;
	
	private float currentMultiple;
	/**
	 * ˮ��ƽ���ٶ�
	 */
	public static final float SPEED = 1.7f;

	private List<Point> mPointsList;
	private Paint mPaint;
	private Paint mTextPaint;
	private Path mWavePath;
	private boolean isMeasured = false;

	private Timer timer;
	private MyTimerTask mTask;

	public void setParamater(int battery,float multiple) {
		this.currentBattery = battery;
		this.currentMultiple=multiple;
	}

	@SuppressLint("HandlerLeak")
	Handler updateHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			Log.i("111", "w.handlemessage" + mLevelLine);
			// ��¼ƽ����λ��
			mMoveLen += SPEED;
			Log.i("333", "w.currentBattery" + currentBattery);
			Log.i("333", "w.mViewHeight" + mViewHeight);
			mLevelLine=((float)(100-currentBattery)*mViewHeight)/100;//�������⿨һ��   ���ˣ�
			
			Log.i("222", "w.handlemessage20.0.0 =====" + mLevelLine);
			mLeftSide += SPEED;
			// ����ƽ��
			for (int i = 0; i < mPointsList.size(); i++) {
				mPointsList.get(i).setX(mPointsList.get(i).getX() + SPEED);
				switch (i % 4) {
				case 0:
				case 2:
					mPointsList.get(i).setY(mLevelLine);
					break;
				case 1:
					mPointsList.get(i).setY(mLevelLine + mWaveHeight);
					break;
				case 3:
					mPointsList.get(i).setY(mLevelLine - mWaveHeight);
					break;
				}
			}
			if (mMoveLen >= mWaveWidth) {
				// ����ƽ�Ƴ���һ���������κ�λ
				mMoveLen = 0;
				resetPoints();
			}
			invalidate();
		}

	};

	/**
	 * ���е��x���궼��ԭ����ʼ״̬��Ҳ����һ������ǰ��״̬
	 */
	private void resetPoints() {
		mLeftSide = -mWaveWidth;
		for (int i = 0; i < mPointsList.size(); i++) {
			mPointsList.get(i).setX(i * mWaveWidth / 4 - mWaveWidth);
		}
	}

	public WaveView(Context context) {
		super(context);
		Log.i("111", "w.WaveView1");
		init();
	}

	public WaveView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public WaveView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mPointsList = new ArrayList<Point>();
		timer = new Timer();

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.FILL);
		mPaint.setColor(Color.parseColor("#1be400"));

		mTextPaint = new Paint();
		mTextPaint.setColor(Color.WHITE);
		mTextPaint.setTextAlign(Align.CENTER);
		mTextPaint.setTextSize(30);

		mWavePath = new Path();
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		// ��ʼ����
		start();
	}

	private void start() {
		if (mTask != null) {
			mTask.cancel();
			mTask = null;
		}
		mTask = new MyTimerTask(updateHandler);
		timer.schedule(mTask, 0, 30);
		Log.i("111", "w.start");
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Log.i("111", "w.onMeasure");
		if (!isMeasured) {
			isMeasured = true;
			mViewHeight = getMeasuredHeight();
			mViewWidth = getMeasuredWidth();
			Log.i("222", "w.mViewHeight" + mViewHeight+"   w.mViewWidth"+mViewWidth);
			Log.i("222", "w.top" + getTop()+"   w.bottom"+getBottom()+"   w.left" + getLeft()+"   w.right"+getRight());
			//mLevelLine=(1-(currentBattery/100))*mViewHeight;
			//mLevelLine=(currentBattery/100)*mViewHeight;
			// ����View��ȼ��㲨�η�ֵ
			mWaveHeight = mViewWidth / currentMultiple;
			// ���������ı�View���Ҳ����View��ֻ�ܿ����ķ�֮һ�����Σ���������ʹ���������
			mWaveWidth = mViewWidth * 4;
			// ������صľ���Ԥ��һ������
			mLeftSide = -mWaveWidth;
			// ��������ڿɼ���View����������ɼ������Σ�ע��n��ȡ��
			int n = (int) Math.round(mViewWidth / mWaveWidth + 0.5);
			// n��������Ҫ4n+1���㣬��������ҪԤ��һ�������������������������Ҫ4n+5����
			for (int i = 0; i < (4 * n + 5); i++) {
				// ��P0��ʼ��ʼ����P4n+4���ܹ�4n+5����
				float x = i * mWaveWidth / 4 - mWaveWidth;
				float y = 0;
				switch (i % 4) {
				case 0:
				case 2:
					// ���λ��ˮλ����
					y = mLevelLine;
					break;
				case 1:
					// ���²����Ŀ��Ƶ�
					y = mLevelLine + mWaveHeight;
					break;
				case 3:
					// ���ϲ����Ŀ��Ƶ�
					y = mLevelLine - mWaveHeight;
					break;
				}
				mPointsList.add(new Point(x, y));
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.i("111", "w.onDraw");

		mWavePath.reset();
		int i = 0;
		mWavePath.moveTo(mPointsList.get(0).getX(), mPointsList.get(0).getY());
		for (; i < mPointsList.size() - 2; i = i + 2) {
			mWavePath.quadTo(mPointsList.get(i + 1).getX(),
					mPointsList.get(i + 1).getY(), mPointsList.get(i + 2)
							.getX(), mPointsList.get(i + 2).getY());
		}
		mWavePath.lineTo(mPointsList.get(i).getX(), mViewHeight);
		mWavePath.lineTo(mLeftSide, mViewHeight);
		mWavePath.close();

		// mPaint��Style��FILL�����������Path����
		canvas.drawPath(mWavePath, mPaint);
		// ���ưٷֱ�
		canvas.drawText(currentBattery + "%", mViewWidth / 2, mViewHeight / 2,
				mTextPaint);
	}

	class MyTimerTask extends TimerTask {
		Handler handler;

		public MyTimerTask(Handler handler) {
			this.handler = handler;
		}

		@Override
		public void run() {
			handler.sendMessage(handler.obtainMessage());
		}

	}

	class Point {
		private float x;
		private float y;

		public float getX() {
			return x;
		}

		public void setX(float x) {
			this.x = x;
		}

		public float getY() {
			return y;
		}

		public void setY(float y) {
			this.y = y;
		}

		public Point(float x, float y) {
			this.x = x;
			this.y = y;
		}

	}

}
