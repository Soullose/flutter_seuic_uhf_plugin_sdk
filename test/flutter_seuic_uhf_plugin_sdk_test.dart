import 'package:flutter_test/flutter_test.dart';
import 'package:flutter_seuic_uhf_plugin_sdk/flutter_seuic_uhf_plugin_sdk.dart';
import 'package:flutter_seuic_uhf_plugin_sdk/flutter_seuic_uhf_plugin_sdk_platform_interface.dart';
import 'package:flutter_seuic_uhf_plugin_sdk/flutter_seuic_uhf_plugin_sdk_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockFlutterSeuicUhfPluginSdkPlatform
    with MockPlatformInterfaceMixin
    implements FlutterSeuicUhfPluginSdkPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final FlutterSeuicUhfPluginSdkPlatform initialPlatform = FlutterSeuicUhfPluginSdkPlatform.instance;

  test('$MethodChannelFlutterSeuicUhfPluginSdk is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelFlutterSeuicUhfPluginSdk>());
  });

  test('getPlatformVersion', () async {
    FlutterSeuicUhfPluginSdk flutterSeuicUhfPluginSdkPlugin = FlutterSeuicUhfPluginSdk();
    MockFlutterSeuicUhfPluginSdkPlatform fakePlatform = MockFlutterSeuicUhfPluginSdkPlatform();
    FlutterSeuicUhfPluginSdkPlatform.instance = fakePlatform;

    expect(await flutterSeuicUhfPluginSdkPlugin.getPlatformVersion(), '42');
  });
}
