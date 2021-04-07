import 'package:flutter/material.dart';

class DrawerDetailPage extends StatelessWidget {
  String title;

  DrawerDetailPage({this.title});

  @override
  Widget build(BuildContext context) => Scaffold(
        appBar: AppBar(
          title: Text(title),
        ),
        body: Center(
          child: Text("$title详情页"),
        ),
      );
}
