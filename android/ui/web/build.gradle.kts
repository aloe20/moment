plugins {
  id("moment.android.library")
  id("maven-publish")
}

android {
  namespace = "com.aloe.web"

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  publishing{
    singleVariant("debug") {
      withSourcesJar()
      withJavadocJar()
    }
  }
}

afterEvaluate {
  publishing{
    repositories {
      mavenLocal()
    }
    publications{
      register<MavenPublication>("Main"){
        groupId = "com.aloe"
        artifactId = "web"
        version = "0.1"
        afterEvaluate { from(components["debug"]) }
      }
    }
  }
}
