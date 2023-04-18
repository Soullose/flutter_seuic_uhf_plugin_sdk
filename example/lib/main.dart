import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_seuic_uhf_plugin_sdk/flutter_seuic_uhf_plugin_sdk.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _epcId = "";
  String _version = "";
  int _power = 0;

  String _platformVersion = 'Unknown';
  final _flutterSeuicUhfPluginSdkPlugin = FlutterSeuicUhfPluginSdk();

  @override
  void initState() {
    super.initState();
    initPlatformState();
    initScanner();
  }

  void initScanner() {
    String barcode;
    _flutterSeuicUhfPluginSdkPlugin.getUHF().listen((dynamic event) {
      barcode = event ?? '无';
      setState(() {
        _epcId = barcode;
      });
    });
  }

  void open() async {
    final result;
    result = await _flutterSeuicUhfPluginSdkPlugin.open();
    print('open:$result');
  }

  void close() async {
    final result;
    result = await _flutterSeuicUhfPluginSdkPlugin.close();
    print('close:$result');
  }

  void registerCallback() async {
    final result;
    result = await _flutterSeuicUhfPluginSdkPlugin.registerCallback();
    print('registerCallback:$result');
  }

  void unregisterCallback() async {
    final result;
    result = await _flutterSeuicUhfPluginSdkPlugin.unregisterCallback();
    print('unregisterCallback:$result');
  }

  void getFirmwareVersion() async {
    final result;
    result = await _flutterSeuicUhfPluginSdkPlugin.getFirmwareVersion();
    setState(() {
      _version = result;
    });
  }

  void getPower() async {
    final result;
    result = await _flutterSeuicUhfPluginSdkPlugin.getPower();
    setState(() {
      _power = result;
    });
  }

  void setPower(int power) async{
    await _flutterSeuicUhfPluginSdkPlugin.setPower(power);
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion =
          await _flutterSeuicUhfPluginSdkPlugin.getPlatformVersion() ??
              'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  TextEditingController textEditingController = new TextEditingController();

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: SingleChildScrollView(
          child: Center(
            child: Column(
              children: [
                Text('Running on: $_platformVersion\n'),
                Text('epcId on: $_epcId\n'),
                Text('固件版本:$_version'),
                Text('功率:$_power'),
                ElevatedButton(
                    onPressed: () {
                      open();
                    },
                    child: Text('开启')),
                ElevatedButton(
                    onPressed: () {
                      close();
                    },
                    child: Text('关闭')),
                ElevatedButton(
                    onPressed: () {
                      registerCallback();
                    },
                    child: Text('注册')),
                ElevatedButton(
                    onPressed: () {
                      unregisterCallback();
                    },
                    child: Text('注销')),
                ElevatedButton(
                    onPressed: () {
                      getFirmwareVersion();
                    },
                    child: Text('获取固件版本')),
                ElevatedButton(
                    onPressed: () {
                      getPower();
                    },
                    child: Text('获取功率')),
                  TextField(
                    controller: textEditingController,
                    decoration: InputDecoration(
                      hintText: '请输入功率',
                      suffixIcon: IconButton(
                        icon: Icon(Icons.send),
                        onPressed: () {
                          setPower(int.parse(textEditingController.text));
                        },
                      ),
                    ),
                  ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
