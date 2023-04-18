package com.example.flutter_seuic_uhf_plugin_sdk;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import com.example.flutter_seuic_uhf_plugin_sdk.constant.Channel;
import com.example.flutter_seuic_uhf_plugin_sdk.constant.Method;
import com.seuic.scankey.IKeyEventCallback;
import com.seuic.scankey.ScanKeyService;
import com.seuic.uhf.EPC;
import com.seuic.uhf.UHFService;

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

    private EventChannel eventChannel;
    private Context applicationContext;

    private UHFService uhfService = UHFService.getInstance();
    private Activity activity;


    public static final int MAX_LEN = 64;


    private ScanKeyService scanKeyService = ScanKeyService.getInstance();

    private IKeyEventCallback iKeyEventCallback;

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
            result.success("Android " + Build.VERSION.RELEASE);
        } else if (call.method.equals(Method.OPEN)) {
//            boolean open = uhfService.open();
            open();
            uhfService.open();
//            uhfService.setPower(20);
            result.success("开启:" );
        } else if (call.method.equals(Method.CLOSE)) {
//            uhfService.close();
            close();
            result.success("关闭");
        } else if (call.method.equals(Method.GET_FIRMWARE_VERSION)) {
            String version = uhfService.getFirmwareVersion();
            result.success(version);
        } else if (call.method.equals(Method.GET_TEMPERATURE)) {
            String temperature = uhfService.getTemperature();
            result.success(temperature);
        } else if (call.method.equals(Method.GET_POWER)) {
            int power = uhfService.getPower();
            result.success(power);
        } else if (call.method.equals(Method.REGISTER_CALLBACK)) {
//            scanKeyService.registerCallback(iKeyEventCallback, "100,101,102,249,249,250");
            registerCallback();
            result.success(true);
        } else if (call.method.equals(Method.UNREGISTER_CALLBACK)) {
//            scanKeyService.unregisterCallback(iKeyEventCallback);
            unregisterCallback();
            result.success(true);
        } else if (call.method.equals(Method.SET_POWER)) {
            setPower((int)call.arguments);
            result.success(call.arguments);
        }else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        resetHandlers();
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        scanKeyService = ScanKeyService.getInstance();
        iKeyEventCallback = iKeyEventCallback();
        uhfService = UHFService.getInstance();
        activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity();
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        onAttachedToActivity(binding);
    }

    @Override
    public void onDetachedFromActivity() {
        resetHandlers();
    }

    public IKeyEventCallback iKeyEventCallback() {
        return new IKeyEventCallback.Stub() {
            @Override
            public void onKeyDown(int i) throws RemoteException {
                if (i == 250) {
                    readUHFOne();
                }
            }

            @Override
            public void onKeyUp(int i) throws RemoteException {

            }
        };
    }

    public void readUHFOne() {
        new Thread(() -> {
        EPC epc = new EPC();
        boolean inventoryOnce = uhfService.inventoryOnce(epc, 100);
        if (inventoryOnce) {
            String epcId = epc.getId();
            System.out.println(epcId);
            if (epcId != null) {
                Intent intent = new Intent(Channel.EVENT_CHANNEL_SCANNER_UHF);
                Bundle bundle = new Bundle();
                try {
                    String regex = "[0-9]+";
                    String substring = epcId.substring(3, 6);
                    if (substring.matches(regex)) {
                        int i = Integer.parseInt(substring);
                        System.out.println(String.valueOf(i));
//                        Intent intent = new Intent(Channel.EVENT_CHANNEL_SCANNER_UHF);
//                        Bundle bundle = new Bundle();
                        bundle.putString("BAR_CODE", String.valueOf(i));
                        intent.putExtras(bundle);
                        applicationContext.sendBroadcast(intent);
                    }else {
                        bundle.putString("BAR_CODE", String.valueOf(epcId));
                        intent.putExtras(bundle);
                        applicationContext.sendBroadcast(intent);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        }).start();
    }

    public void registerCallback() {
        new Thread(() -> scanKeyService.registerCallback(iKeyEventCallback, "100,101,102,249,249,250")).start();
    }

    public void unregisterCallback() {
        new Thread(() -> scanKeyService.unregisterCallback(iKeyEventCallback)).start();
    }

    public void open() {
        new Thread(() -> {
            uhfService.open();
            uhfService.setPower(20);
        }).start();
    }

    public void close() {
        new Thread(()->uhfService.close()).start();
    }

    public void setPower(int power) {
        new Thread(()->uhfService.setPower(power)).start();
    }

    private void resetHandlers() {
        if (channel != null) {
            channel.setMethodCallHandler(null);
        }
        if (openChannel != null) {
            openChannel.setMethodCallHandler(null);
        }
        if (closeChannel != null) {
            closeChannel.setMethodCallHandler(null);
        }
        if (versionChannel != null) {
            versionChannel.setMethodCallHandler(null);
        }
        if (tempChannel != null) {
            tempChannel.setMethodCallHandler(null);
        }
        if (powerChannel != null) {
            powerChannel.setMethodCallHandler(null);
        }
        if (eventChannel != null) {
            eventChannel.setStreamHandler(null);
        }
    }

    public void read(String epcId) {
        int bank = Integer.parseInt("1");
        int address = Integer.parseInt("0");
        int length = Integer.parseInt("2");

        String str_password = "00000000";

        byte[] btPassword = new byte[16];
        BaseUtil.getHexByteArray(str_password, btPassword, btPassword.length);
        byte[] buffer = new byte[MAX_LEN];
        if (length > MAX_LEN) {
            buffer = new byte[length];
        }


        if (!uhfService.readTagData(getHexByteArray(epcId), getHexByteArray(str_password), bank, address, length, buffer)) {
            System.out.println("read fail");
        } else {
            String data = BaseUtil.getHexString(buffer, length, " ");
            System.out.println("data:" + data);
        }


    }


    public static byte[] getHexByteArray(String hexString) {
        byte[] buffer = new byte[hexString.length() / 2];
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            buffer[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return buffer;
    }

    static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

}
