import 'package:flu/page/home_page.dart';
import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

class SplashPage extends StatelessWidget {
  VoidCallback onPressed;

  SplashPage({this.onPressed});

  @override
  Widget build(BuildContext context) =>
      Scaffold(
        body: Stack(
          children: [
            Container(
              constraints: BoxConstraints(
                  minWidth: double.infinity, minHeight: double.infinity),
              child: Image(
                image: NetworkImage('https://picsum.photos/id/1/1080/1920'),
                fit: BoxFit.cover,
              ),
            ),
            Container(
              padding: EdgeInsets.symmetric(vertical: 8.0, horizontal: 16.0),
              alignment: Alignment.bottomRight,
              child: TextButton(
                  onPressed: onPressed,
                  /*onPressed: () => {
                        Navigator.of(context).push(
                            MaterialPageRoute(builder: (context) => MainPage()))
                      },*/
                  child: Text(
                    "去首页",
                    style: TextStyle(color: Colors.white),
                  )),
            )
          ],
        ),
      );
}
