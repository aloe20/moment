import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'data_method_channel.dart';

abstract class DataPlatform extends PlatformInterface {
  /// Constructs a DataPlatform.
  DataPlatform() : super(token: _token);

  static final Object _token = Object();

  static DataPlatform _instance = MethodChannelData();

  /// The default instance of [DataPlatform] to use.
  ///
  /// Defaults to [MethodChannelData].
  static DataPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [DataPlatform] when
  /// they register themselves.
  static set instance(DataPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
