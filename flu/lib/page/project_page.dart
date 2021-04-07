import 'package:flutter/material.dart';

class ProjectPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) => DefaultTabController(
        initialIndex: 0,
        length: 3,
        child: Scaffold(
          appBar: TabBar(tabs: [
            Tab(
              icon: Icon(Icons.cloud_outlined, color: Colors.blue,),
            ),
            Tab(
              icon: Icon(Icons.beach_access_outlined, color: Colors.blue),
            ),
            Tab(
              icon: Icon(Icons.brightness_5_outlined, color: Colors.blue),
            )
          ]),
          body: TabBarView(children: [
            Center(
              child: Text('cloudy'),
            ),
            Center(
              child: Text('rainy'),
            ),
            Center(
              child: Text('sunny'),
            )
          ]),
        ),
      );
}
