plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  id("maven-publish")
}

android {
  namespace = "com.aloe.web"
  compileSdk = libs.versions.sdk.compile.get().toInt()
  buildToolsVersion = libs.versions.sdk.tool.get()
  defaultConfig {
    minSdk = libs.versions.sdk.min.get().toInt()
    targetSdk = libs.versions.sdk.target.get().toInt()
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility(JavaVersion.VERSION_11)
    targetCompatibility(JavaVersion.VERSION_11)
  }
  kotlinOptions {
    jvmTarget = "11"
  }
}

val sourcesJar by tasks.registering(Jar::class){
  from(android.sourceSets["main"].java.srcDirs)
  archiveClassifier.set("sources")
}

publishing{
  repositories {
    mavenLocal()
  }
  publications{
    create<MavenPublication>("Main"){
      artifact(sourcesJar)
      afterEvaluate { artifact(tasks.getByName("bundleDebugAar")) }
      groupId = "com.aloe"
      artifactId = "web"
      version = "0.1"
    }
  }
}