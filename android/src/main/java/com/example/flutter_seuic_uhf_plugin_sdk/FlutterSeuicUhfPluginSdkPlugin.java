package com.example.flutter_seuic_uhf_plugin_sdk;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.flutter_seuic_uhf_plugin_sdk.constant.Channel;
import com.example.flutter_seuic_uhf_plugin_sdk.constant.Method;
import com.seuic.scankey.IKeyEventCallback;
import com.seuic.scankey.ScanKeyService;
import com.seuic.uhf.EPC;
import com.seuic.uhf.UHFService;

//import java.lang.reflect.Method;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * FlutterSeuicUhfPluginSdkPlugin
 */
public class FlutterSeuicUhfPluginSdkPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;
    private MethodChannel openChannel;
    private MethodChannel closeChannel;
    private MethodChannel versionChannel;
    private MethodChannel tempChannel;
    private MethodChannel powerChannel;

    private static final String EVENT_CHANNEL_NAME = "event_channel";
    private EventChannel eventChannel;
    private Context applicationContext;

    private UHFService uhfService;
    private Activity activity;

    private ScanKeyService mScanKeyService;
    private IKeyEventCallback mCallback;


    private String TAG = "zy";


    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        applicationContext = flutterPluginBinding.getApplicationContext();

        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_seuic_uhf_plugin_sdk");
        channel.setMethodCallHandler(this);

        openChannel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), Channel.METHOD_CHANNEL_OPEN);
        openChannel.setMethodCallHandler(this);

        closeChannel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), Channel.METHOD_CHANNEL_CLOSE);
        closeChannel.setMethodCallHandler(this);

        versionChannel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), Channel.METHOD_CHANNEL_VERSION);
        versionChannel.setMethodCallHandler(this);

        tempChannel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), Channel.METHOD_CHANNEL_TEMP);
        tempChannel.setMethodCallHandler(this);

        powerChannel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), Channel.METHOD_CHANNEL_POWER);
        powerChannel.setMethodCallHandler(this);

        eventChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), Channel.EVENT_CHANNEL_SCANNER_UHF);
        eventChannel.setStreamHandler(new ScannerUHFService(flutterPluginBinding.getApplicationContext()));

    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {


        if (call.method.equals("getPlatformVersion")) {

//      uhfService = UHFService.getInstance();
//            boolean open = uhfService.open();
//            String version = uhfService.getFirmwareVersion();
//            String temperature = uhfService.getTemperature();
//            int power = uhfService.getPower();
//            String region = uhfService.getRegion();
//            System.out.println("是否打开:" + open);
//            System.out.println("固件版本:" + version);
//            System.out.println("温度:" + temperature);
//            System.out.println("功率:" + power);
//            System.out.println("区域:" + region);
//
//            System.out.println(uhfService.getTagIDs());

//            mScanKeyService.registerCallback(mCallback, "100,101,102,249,249,250");
            result.success("Android " + Build.VERSION.RELEASE);
        } else if (call.method.equals(Method.OPEN)){
            boolean open = uhfService.open();
            result.success("开启");
        }
        else if (call.method.equals(Method.CLOSE)){
            uhfService.close();
            result.success("关闭");
        }
        else if (call.method.equals(Method.GET_FIRMWARE_VERSION)){
            String version = uhfService.getFirmwareVersion();
            result.success(version);
        }
        else if (call.method.equals(Method.GET_TEMPERATURE)){
            String temperature = uhfService.getTemperature();
            result.success(temperature);
        }
        else if (call.method.equals(Method.GET_POWER)){
            int power = uhfService.getPower();
            result.success(power);
        }
        else if (call.method.equals(Method.REGISTER_CALLBACK)){
            mScanKeyService.registerCallback(mCallback, "100,101,102,249,249,250");
            result.success(true);
        }
        else if (call.method.equals(Method.UNREGISTER_CALLBACK)){
            mScanKeyService.unregisterCallback(mCallback);
            result.success(true);
        }
        else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        byte[] btPassword = new byte[16];
        BaseUtil.getHexByteArray("00000000", btPassword, btPassword.length);
        activity = binding.getActivity();
//    stopApps(activity, "com.seuic.uhftool");
        EPC epc = new EPC();
        uhfService = UHFService.getInstance();
        mScanKeyService = ScanKeyService.getInstance();
        mCallback = new IKeyEventCallback.Stub() {
            @Override
            public void onKeyDown(int keyCode) throws RemoteException {
                Log.d(TAG, "onKeyDown: keyCode=" + keyCode);
                System.out.println("99999999999999999999999999999999999");
                boolean inventoryOnce = uhfService.inventoryOnce(epc, 100);
                if (inventoryOnce) {
                    String epcId = epc.getId();
                    System.out.println(epcId);
                    if (epcId!=null){
                        byte[] buffer = new byte[128];
                        // bank 0：密码区  1：EPC区  2：TI区  3：用户区
                        boolean readTagData = uhfService.readTagData(BaseUtil.getHexByteArray(epcId), btPassword, 3, 0, 6, buffer);
                        System.out.println("xxxxxxx" + readTagData);
                        if (readTagData) {
                            String data = BaseUtil.getHexString(buffer, 6, " ");
                            System.out.println("readTagData：" + data);
                        }
                        Intent intent = new Intent(Channel.EVENT_CHANNEL_SCANNER_UHF);
                        Bundle bundle = new Bundle();
                        bundle.putString("BAR_CODE", epcId);
                        intent.putExtras(bundle);
                        applicationContext.sendBroadcast(intent);
                    }

                }
            }

            @Override
            public void onKeyUp(int keyCode) throws RemoteException {
                Log.d(TAG, "onKeyUp: keyCode=" + keyCode);
            }
        };
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
//    mScanKeyService.registerCallback(mCallback, "100,101,102,249,249,250");

    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
//    mScanKeyService.registerCallback(mCallback, "100,101,102,249,249,250");

    }

    @Override
    public void onDetachedFromActivity() {
//    mScanKeyService.registerCallback(mCallback, "100,101,102,249,249,250");

    }


//    public static void stopApps(Context context, String packageName) {
//        try {
//            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            Method forceStopPackage = am.getClass().getDeclaredMethod("forceStopPackage", String.class);
//            forceStopPackage.setAccessible(true);
//            forceStopPackage.invoke(am, packageName);
//            System.out.println("TimerV force stop package " + packageName + " successful");
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            System.err.println("TimerV force stop package " + packageName + " error!");
//        }
//    }
}
