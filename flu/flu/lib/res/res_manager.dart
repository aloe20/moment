/*
 * Copyright 2023 The Android Open Source Project
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
import 'dart:ui';

import 'package:flutter/cupertino.dart';
import 'package:flutter/services.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class ResManager extends PlatformInterface {
  static final Object _token = Object();
  final _resChannel = const MethodChannel('res');
  static ResManager _instance = ResManager();

  ResManager() : super(token: _token);

  static ResManager get instance => _instance;

  static set instance(ResManager instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<Color> getColor(String colorName) async {
    final color = await _resChannel.invokeMethod<int>('getColor', {'name': colorName});
    return Color(color??0);
  }
}
