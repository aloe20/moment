import 'package:flutter_test/flutter_test.dart';
import 'package:data/data.dart';
import 'package:data/data_platform_interface.dart';
import 'package:data/data_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockDataPlatform
    with MockPlatformInterfaceMixin
    implements DataPlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final DataPlatform initialPlatform = DataPlatform.instance;

  test('$MethodChannelData is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelData>());
  });

  test('getPlatformVersion', () async {
    Data dataPlugin = Data();
    MockDataPlatform fakePlatform = MockDataPlatform();
    DataPlatform.instance = fakePlatform;

    expect(await dataPlugin.getPlatformVersion(), '42');
  });
}
