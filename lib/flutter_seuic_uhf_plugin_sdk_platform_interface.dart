import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'flutter_seuic_uhf_plugin_sdk_method_channel.dart';

abstract class FlutterSeuicUhfPluginSdkPlatform extends PlatformInterface {
  /// Constructs a FlutterSeuicUhfPluginSdkPlatform.
  FlutterSeuicUhfPluginSdkPlatform() : super(token: _token);

  static final Object _token = Object();

  static FlutterSeuicUhfPluginSdkPlatform _instance =
      MethodChannelFlutterSeuicUhfPluginSdk();

  /// The default instance of [FlutterSeuicUhfPluginSdkPlatform] to use.
  ///
  /// Defaults to [MethodChannelFlutterSeuicUhfPluginSdk].
  static FlutterSeuicUhfPluginSdkPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FlutterSeuicUhfPluginSdkPlatform] when
  /// they register themselves.
  static set instance(FlutterSeuicUhfPluginSdkPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Stream<String?> getUHF() {
    throw UnimplementedError('getUHF() has not been implemented.');
  }

  Future<String?> open() {
    throw UnimplementedError('open() has not been implemented.');
  }

  Future<String?> close() {
    throw UnimplementedError('close() has not been implemented.');
  }

  Future<bool?> unregisterCallback() {
    throw UnimplementedError('unregisterCallback() has not been implemented.');
  }

  Future<bool?> registerCallback() {
    throw UnimplementedError('registerCallback() has not been implemented.');
  }

  Future<String?> getFirmwareVersion() {
    throw UnimplementedError('getFirmwareVersion() has not been implemented.');
  }
}
