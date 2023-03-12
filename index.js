import React from 'react';
import { AppRegistry, StyleSheet, Text, View } from 'react-native';

const Hello1 = (props) => {
  return (
    <View style={styles.container}>
      <Text style={styles.hello}>Hello1, {props.name}!</Text>
    </View>
  );
}
const Hello2 = (props) => {
  return (
    <View style={styles.container}>
      <Text style={styles.hello}>Hello2, {props.name}!</Text>
    </View>
  );
}
const MainPage = (props) => {
  switch (props.router) {
    case 'hello1': return <Hello1 name={props.name}></Hello1>;
    default: return <Hello2 name={props.name}></Hello2>;
  }
};
var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
  },
  hello: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
});

AppRegistry.registerComponent('rn', () => MainPage);