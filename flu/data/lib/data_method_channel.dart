import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'data_platform_interface.dart';

/// An implementation of [DataPlatform] that uses method channels.
class MethodChannelData extends DataPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('data');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
}
