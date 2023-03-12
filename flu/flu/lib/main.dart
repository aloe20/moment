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

import 'package:data/data.dart';
import 'package:flu/res/res_manager.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() => runApp(const App());
class App extends StatefulWidget {
  const App({Key? key}) : super(key: key);

  @override
  State<App> createState() => _AppState();
}

class _AppState extends State<App> {
  ThemeData _lightTheme = ThemeData.light(useMaterial3: true);
  ThemeData _darkTheme = ThemeData.dark(useMaterial3: true);
  @override
  void initState() {
    super.initState();
    _loadColor();
  }
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      debugShowCheckedModeBanner: false,
      themeMode: ThemeMode.system,
      theme: _lightTheme,
      darkTheme: _darkTheme,
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }

  _loadColor() async {
    var lightScheme = ColorScheme.light(
      primary: await ResManager.instance.getColor("primary"),
      onPrimary: await ResManager.instance.getColor("onPrimary"),
      primaryContainer: await ResManager.instance.getColor("primaryContainer"),
      onPrimaryContainer: await ResManager.instance.getColor("onPrimaryContainer"),
      secondary: await ResManager.instance.getColor("secondary"),
      onSecondary: await ResManager.instance.getColor("onSecondary"),
      secondaryContainer: await ResManager.instance.getColor("secondaryContainer"),
      onSecondaryContainer: await ResManager.instance.getColor("onSecondaryContainer"),
      tertiary: await ResManager.instance.getColor("tertiary"),
      onTertiary: await ResManager.instance.getColor("onTertiary"),
      tertiaryContainer: await ResManager.instance.getColor("tertiaryContainer"),
      onTertiaryContainer: await ResManager.instance.getColor("onTertiaryContainer"),
      error: await ResManager.instance.getColor("error"),
      onError: await ResManager.instance.getColor("onError"),
      errorContainer: await ResManager.instance.getColor("errorContainer"),
      onErrorContainer: await ResManager.instance.getColor("onErrorContainer"),
      background: await ResManager.instance.getColor("background"),
      onBackground: await ResManager.instance.getColor("onBackground"),
      surface: await ResManager.instance.getColor("surface"),
      onSurface: await ResManager.instance.getColor("onSurface"),
      surfaceVariant: await ResManager.instance.getColor("surfaceVariant"),
      onSurfaceVariant: await ResManager.instance.getColor("onSurfaceVariant"),
      outline: await ResManager.instance.getColor("outline"),
      outlineVariant: await ResManager.instance.getColor("outlineVariant"),
      scrim: await ResManager.instance.getColor("scrim"),
      inverseSurface: await ResManager.instance.getColor("inverseSurface"),
      inversePrimary: await ResManager.instance.getColor("inversePrimary"),
      surfaceTint: await ResManager.instance.getColor("surfaceTint"),
    );
    setState(() {
      _lightTheme = ThemeData.from(colorScheme: lightScheme, useMaterial3: true);
      _darkTheme = ThemeData.from(colorScheme: lightScheme.copyWith(brightness: Brightness.dark), useMaterial3: true);
    });
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;
  String _platformVersion = 'Unknown';
  final _dataPlugin = Data();
  Color? _color = Colors.white;

  @override
  void initState() {
    super.initState();
    initPlatformState();
    Future(() async {
      var color = await ResManager.instance.getColor("purple_500");
      setState(() {
        _color = color;
      });
    });
  }

  Future<void> initPlatformState() async {
    String platformVersion;
    try {
      platformVersion =
          await _dataPlugin.getPlatformVersion() ?? 'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  void _incrementCounter() {
    setState(() {
      _counter++;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text('Running on: $_platformVersion\n'),
            Text(
              'You have pushed the button this many times:',
              style: TextStyle(color: _color),
            ),
            Text(
              '$_counter',
              style: Theme
                  .of(context)
                  .textTheme
                  .headlineMedium,
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: const Icon(Icons.add),
      ),
    );
  }
}
