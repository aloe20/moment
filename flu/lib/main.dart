import 'package:flu/page/home_page.dart';
import 'package:flu/page/splash_page.dart';
import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => MyAppState();
}

class MyAppState extends State<MyApp> {
  bool _isHome = false;

  @override
  Widget build(BuildContext context) =>
      MaterialApp(
        debugShowCheckedModeBanner: false,
        theme: ThemeData(
          primarySwatch: Colors.blue,
        ),
        home: _isHome ? HomePage() : SplashPage(onPressed: () {
          setState(() {
            _isHome = true;
          });
        },),
      );
}
