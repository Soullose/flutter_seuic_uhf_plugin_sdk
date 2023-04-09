import 'flutter_seuic_uhf_plugin_sdk_platform_interface.dart';

class FlutterSeuicUhfPluginSdk {
  Future<String?> getPlatformVersion() {
    return FlutterSeuicUhfPluginSdkPlatform.instance.getPlatformVersion();
  }

  Stream<String?> getUHF() {
    return FlutterSeuicUhfPluginSdkPlatform.instance.getUHF();
  }

  Future<String?> getFirmwareVersion() {
    return FlutterSeuicUhfPluginSdkPlatform.instance.getFirmwareVersion();
  }

  Future<String?> open() {
    return FlutterSeuicUhfPluginSdkPlatform.instance.open();
  }

  Future<String?> close() {
    return FlutterSeuicUhfPluginSdkPlatform.instance.close();
  }


  Future<bool?> registerCallback() {
    return FlutterSeuicUhfPluginSdkPlatform.instance.registerCallback();
  }

  Future<bool?> unregisterCallback() {
    return FlutterSeuicUhfPluginSdkPlatform.instance.unregisterCallback();
  }

}
