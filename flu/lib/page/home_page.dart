import 'package:flu/page/drawer_detail_page.dart';
import 'package:flu/page/project_page.dart';
import 'package:flu/page/recommend_page.dart';
import 'package:flutter/material.dart';

class HomePage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  int _selectIndex = 0;
  List<String> _title = ['Home', 'Business', 'School', 'Cake'];
  List<Widget> _bodyWidget = [
    RecommendPage(),
    ProjectPage(),
    Text('School'),
    Text('Cake'),
  ];

  @override
  Widget build(BuildContext context) => Scaffold(
        appBar: AppBar(
          title: Text(_title[_selectIndex]),
        ),
        drawer: _getDrawer(),
        body: _bodyWidget[_selectIndex],
        bottomNavigationBar: BottomNavigationBar(
          items: [
            BottomNavigationBarItem(icon: Icon(Icons.home), label: _title[0]),
            BottomNavigationBarItem(
                icon: Icon(Icons.business), label: _title[1]),
            BottomNavigationBarItem(icon: Icon(Icons.school), label: _title[2]),
            BottomNavigationBarItem(icon: Icon(Icons.cake), label: _title[3]),
          ],
          currentIndex: _selectIndex,
          selectedItemColor: Colors.amber[800],
          unselectedItemColor: Colors.grey,
          showUnselectedLabels: true,
          onTap: (index) => {
            setState(() {
              _selectIndex = index;
            })
          },
        ),
      );

  Drawer _getDrawer() => Drawer(
        child: ListView(
          padding: EdgeInsets.zero,
          children: [
            DrawerHeader(
                decoration: BoxDecoration(color: Colors.blue),
                child: Text('Drawer Header',
                    style: TextStyle(color: Colors.white, fontSize: 24))),
            ListTile(
              leading: Icon(Icons.message),
              title: Text('Message'),
              onTap: () {
                _jumpDetailPage('Message');
              },
            ),
            ListTile(
              leading: Icon(Icons.account_circle),
              title: Text('Profile'),
              onTap: () {
                _jumpDetailPage('Profile');
              },
            ),
            ListTile(
              leading: Icon(Icons.settings),
              title: Text('Settings'),
              onTap: () {
                _jumpDetailPage('Settings');
              },
            )
          ],
        ),
      );

  void _jumpDetailPage(String title) {
    Navigator.of(context).push(MaterialPageRoute(
        builder: (context) => DrawerDetailPage(
              title: title,
            )));
  }
}
