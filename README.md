# flutter_seuic_uhf_plugin_sdk

A Flutter plugin based on the seuic(东集) UTouch mobile phone uhf scanner SDK

## Getting Started


in pubspec.yml dependencies add

```yaml

flutter_seuic_uhf_plugin_sdk:
  git:
    url: https://github.com/Soullose/flutter_seuic_uhf_plugin_sdk.git

```

## Get UHF Scanner

```dart
final _flutterSeuicUhfPluginSdkPlugin = FlutterSeuicUhfPluginSdk();



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


```