import 'package:flutter_seuic_uhf_plugin_sdk/flutter_seuic_uhf_plugin_sdk_method_channel.dart';
import 'package:flutter_seuic_uhf_plugin_sdk/flutter_seuic_uhf_plugin_sdk_platform_interface.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockFlutterSeuicUhfPluginSdkPlatform
    with MockPlatformInterfaceMixin
    implements FlutterSeuicUhfPluginSdkPlatform {
  @override
  Future<String?> close() {
    // TODO: implement close
    throw UnimplementedError();
  }

  @override
  Future<String?> getFirmwareVersion() {
    // TODO: implement getFirmwareVersion
    throw UnimplementedError();
  }

  @override
  Future<String?> getPlatformVersion() {
    // TODO: implement getPlatformVersion
    throw UnimplementedError();
  }

  @override
  Future<int?> getPower() {
    // TODO: implement getPower
    throw UnimplementedError();
  }

  @override
  Stream<String?> getUHF() {
    // TODO: implement getUHF
    throw UnimplementedError();
  }

  @override
  Future<String?> open() {
    // TODO: implement open
    throw UnimplementedError();
  }

  @override
  Future<bool?> registerCallback() {
    // TODO: implement registerCallback
    throw UnimplementedError();
  }

  @override
  Future<void> setPower(int power) {
    // TODO: implement setPower
    throw UnimplementedError();
  }

  @override
  Future<bool?> unregisterCallback() {
    // TODO: implement unregisterCallback
    throw UnimplementedError();
  }
}

void main() {
  final FlutterSeuicUhfPluginSdkPlatform initialPlatform =
      FlutterSeuicUhfPluginSdkPlatform.instance;

  test('$MethodChannelFlutterSeuicUhfPluginSdk is the default instance', () {
    expect(
        initialPlatform, isInstanceOf<MethodChannelFlutterSeuicUhfPluginSdk>());
  });
}
