
import 'data_platform_interface.dart';

class Data {
  Future<String?> getPlatformVersion() {
    return DataPlatform.instance.getPlatformVersion();
  }
}
