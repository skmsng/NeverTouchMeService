package com.example.nevertouchmeservice;

import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.Vibrator;

public class NeverTouchService extends Service
								implements SensorEventListener {
	
	private SensorManager mSensorManager;	// センサーマネージャ
	private Sensor mProximity;	//接近センサー
	private  float mPreValue=1;	//接近センサー（近い:0.0, 遠い:1.0）
	
	//サービス起動時1回だけ呼び出される
	@Override
	public void onCreate() {
		super.onCreate();
//        // センサーマネージャのインスタンスを取得
//        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
//        // センサーリストから接近センサーの取得
//        List<Sensor> list = mSensorManager.getSensorList(Sensor.TYPE_PROXIMITY);
//        if (list.size()>0) {
//        	mProximity = list.get(0);
//        }
//        //センサーのイベントリスナー登録
//    	mSensorManager.registerListener(this, mProximity,SensorManager.SENSOR_DELAY_UI);
    	//ノーティフィケーション表示
    	showNotification();
    	voice();
    	vibrate();
	}
	
	//サービス起動するたびに呼び出される
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		//showNotification();
	}

	//サービス終了時1回だけ呼び出される
	@Override
	public void onDestroy() {
		super.onDestroy();
		//センサーのイベントリスナー登録解除
		mSensorManager.unregisterListener(this);
        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nManager.cancel(R.string.app_name);	//ノーティフィケーション非表示
	}
	
	//クライアント（アクティビティ）側からサービス側に処理をさせる場合
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO 自動生成されたメソッド・スタブ

	}
	
	//センサーが反応したとき
	public void onSensorChanged(SensorEvent event) {
		// 接近センサー以外
		if (event.sensor.getType()!= Sensor.TYPE_PROXIMITY) {
			return;
		}
		//接近センサーの値が変化したとき
		if (event.values[0] < mPreValue) {
			vibrate();
			voice();
		}
		//センサー状態
		mPreValue = event.values[0];

	}
	//バイブレータ
	private void vibrate () {
		//インスタンス生成
    	Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
    	//振動パターン(OFF,ON,OFF,ON...の時間)
    	long[] pattern = {1000, 1000, 1000, 1000};
    	//振動開始(パターン,繰り返し開始位置)
    	vibrator.vibrate(pattern, -1);	//繰り返しなし
	}
	//音声
	private void voice () {
		//インスタンス生成
    	VoicePlayer vplayer = new VoicePlayer();
    	vplayer.play();
	}
	
	//ノーティフィケーション（ステータスバー、通知領域）に表示
	private void showNotification() {
		//マネージャの取得
        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //インスタンス生成(ステータスバーアイコン,ステータスバーメッセージ,通知領域に表示される発生時間)
        Notification notification;
        notification = new Notification(R.drawable.icon, "NeverTouchMeサービスを起動します",
                System.currentTimeMillis());
        //通知領域に追加
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        // 通知領域をタップしたときに呼ばれるActivity
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this,NeverTouchActivity.class),Intent.FLAG_ACTIVITY_NEW_TASK);
        //通知領域のタイトル,メッセージ
        notification.setLatestEventInfo(this, getString(R.string.app_name),
                "NeverTouchMeサービスを開始しました", contentIntent);
        //ノーティフィケーション表示
        nManager.notify(R.string.app_name, notification);
	}
}
