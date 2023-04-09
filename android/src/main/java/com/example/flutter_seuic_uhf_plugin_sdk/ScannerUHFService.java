package com.example.flutter_seuic_uhf_plugin_sdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.example.flutter_seuic_uhf_plugin_sdk.constant.Channel;
import com.seuic.uhf.UHFService;

import io.flutter.plugin.common.EventChannel;

public class ScannerUHFService implements EventChannel.StreamHandler {
    UHFService uhfService;


    private BroadcastReceiver chargingStateChangeReceiver;


    private final Context applicationContext;


    public ScannerUHFService(Context context) {
        this.applicationContext = context;
    }

    @Override
    public void onListen(Object arguments, EventChannel.EventSink events) {
        chargingStateChangeReceiver = scanUHFReceiver(events);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Channel.EVENT_CHANNEL_SCANNER_UHF);
        intentFilter.setPriority(Integer.MAX_VALUE);
        applicationContext.registerReceiver(chargingStateChangeReceiver, intentFilter);
    }

    @Override
    public void onCancel(Object arguments) {
        applicationContext.unregisterReceiver(chargingStateChangeReceiver);
        chargingStateChangeReceiver = null;
    }


    private BroadcastReceiver scanUHFReceiver(final EventChannel.EventSink events) {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                System.out.println(extras.getString("BAR_CODE"));
                events.success(extras.getString("BAR_CODE"));
            }
        };
    }
}