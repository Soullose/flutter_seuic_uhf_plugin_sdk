import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:flutter_seuic_uhf_plugin_sdk/constents/channel.dart';
import 'package:flutter_seuic_uhf_plugin_sdk/constents/method.dart';

import 'flutter_seuic_uhf_plugin_sdk_platform_interface.dart';

/// An implementation of [FlutterSeuicUhfPluginSdkPlatform] that uses method channels.
class MethodChannelFlutterSeuicUhfPluginSdk extends FlutterSeuicUhfPluginSdkPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('flutter_seuic_uhf_plugin_sdk');

  @visibleForTesting
  final openChannel = MethodChannel(Channel.METHOD_CHANNEL_OPEN);

  @visibleForTesting
  final closeChannel = MethodChannel(Channel.METHOD_CHANNEL_CLOSE);

  @visibleForTesting
  final versionChannel = MethodChannel(Channel.METHOD_CHANNEL_VERSION);

  @visibleForTesting
  final tempChannel = MethodChannel(Channel.METHOD_CHANNEL_TEMP);

  @visibleForTesting
  final powerChannel = MethodChannel(Channel.METHOD_CHANNEL_POWER);

  @visibleForTesting
  final eventChannel = EventChannel(Channel.EVENT_CHANNEL_SCANNER_UHF);

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Stream<String> getUHF() {
    return eventChannel
        .receiveBroadcastStream()
        .map((dynamic event) => event.toString());
  }

  @override
  Future<String> getFirmwareVersion() async{
    final version = await versionChannel.invokeMethod(Method.GET_FIRMWARE_VERSION);
    return version;
  }

  @override
  Future<String> open() async{
    final open = await versionChannel.invokeMethod(Method.OPEN);
    return open;
  }


  @override
  Future<String> close() async{
    final close = await versionChannel.invokeMethod(Method.CLOSE);
    return close;
  }

  @override
  Future<bool> registerCallback() async{
    final reg = await versionChannel.invokeMethod(Method.REGISTER_CALLBACK);
    return reg;
  }

  @override
  Future<bool> unregisterCallback() async{
    final unReg = await versionChannel.invokeMethod(Method.UNREGISTER_CALLBACK);
    return unReg;
  }
}
