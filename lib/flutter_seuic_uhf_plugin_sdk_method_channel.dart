import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:flutter_seuic_uhf_plugin_sdk/constents/channel.dart';
import 'package:flutter_seuic_uhf_plugin_sdk/constents/method.dart';

import 'flutter_seuic_uhf_plugin_sdk_platform_interface.dart';

/// An implementation of [FlutterSeuicUhfPluginSdkPlatform] that uses method channels.
class MethodChannelFlutterSeuicUhfPluginSdk
    extends FlutterSeuicUhfPluginSdkPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('flutter_seuic_uhf_plugin_sdk');

  @visibleForTesting
  final openChannel = MethodChannel(Channel.methodChannelOpen);

  @visibleForTesting
  final closeChannel = MethodChannel(Channel.methodChannelClose);

  @visibleForTesting
  final versionChannel = MethodChannel(Channel.methodChannelVersion);

  @visibleForTesting
  final tempChannel = MethodChannel(Channel.methodChannelTemp);

  @visibleForTesting
  final powerChannel = MethodChannel(Channel.methodChannelPower);

  @visibleForTesting
  final eventChannel = EventChannel(Channel.eventChannelScannerUhf);

  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Stream<String> getUHF() {
    return eventChannel
        .receiveBroadcastStream()
        .map((dynamic event) => event.toString());
  }

  @override
  Future<String> getFirmwareVersion() async {
    final version =
        await versionChannel.invokeMethod(Method.getFirmwareVersion);
    return version;
  }

  @override
  Future<String> open() async {
    final open = await openChannel.invokeMethod(Method.open);
    return open;
  }

  @override
  Future<String> close() async {
    final close = await closeChannel.invokeMethod(Method.close);
    return close;
  }

  @override
  Future<bool> registerCallback() async {
    final reg = await methodChannel.invokeMethod(Method.registerCallback);
    return reg;
  }

  @override
  Future<bool> unregisterCallback() async {
    final unReg = await methodChannel.invokeMethod(Method.unregisterCallback);
    return unReg;
  }

  @override
  Future<int> getPower() async {
    final power = await powerChannel.invokeMethod(Method.getPower);
    return power;
  }

  @override
  Future<void> setPower(int power) async {
    await powerChannel.invokeMethod(Method.setPower, power);
  }
}
