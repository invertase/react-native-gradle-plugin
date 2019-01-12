<p align="center">
  <a href="https://invertase.io">
    <img src="https://static.invertase.io/assets/invertase-logo-small.png"><br/>
  </a>
  <h2 align="center">React Native Gradle Plugin</h2>
</p>

<p align="center">
  <a href="/LICENSE"><img src="https://img.shields.io/npm/l/@invertase/puppeteer-pool.svg?style=flat-square" alt="License"></a>
  <a href="https://discord.gg/C9aK28N"><img src="https://img.shields.io/discord/295953187817521152.svg?logo=discord&style=flat-square&colorA=7289da&label=discord" alt="Chat"></a>
  <a href="https://twitter.com/invertaseio"><img src="https://img.shields.io/twitter/follow/invertaseio.svg?style=social&label=Follow" alt="Follow on Twitter"></a>
</p>

----

A gradle plugin for React Native Android that simplifies build configurations and versioning for projects & native modules.

## Installation

TODO

## For Projects

TODO

## For React Native Modules


TODO


## Planned Features

Example usage below of WIP features. Example is of a React Native Module's build gradle file.

```groovy
plugins {
  id "io.invertase.gradle.react-native"
}

// this React Native module defines its defaults
// a users project consuming this module can override 
// the values below by creating the same format
project.ext {
  set('react-native', [
    versions: [
      // --->
      // built in scopes
      // --->      
      android           : [
        minSdk    : 16,
        targetSdk : 28,
        compileSdk: 28,
        
        // only need to set this if you need to use a
        // custom version or if your Gradle version is < 3.0
        // buildTools: "28.0.3"
      ],

      gradle            : [
        buildTools: "3.2.1"
      ],

      googlePlayServices: [
        base: "16.0.1",
        
        // e.g. custom ones:
        // auth: "16.0.1",
        // maps: "16.0.0",
      ],
      
      // --->
      // RN modules can add more scopes with versions here
      // --->
      firebase          : [
        core: "16.0.6",
        // etc
      ],
    ],
    options : [
      // specify a custom react native source directory
      // relative to <YOUR_PROJECT>/android
      // automatically detects location already
      // reactNativeAndroidDir: "/../../../node_modules/react-native/android",

      // set the version of this module/project to
      // be the version from your package.json file
      usePackageVersion: true,
    ],

    // toggle on/off plugin inspections/warning helpers
    // the warnings trigger either on build failure or
    // when a possible configuration issue has been detected in debug build
    warnings: [
      duplicateDex       : false,

      jcenterBeforeGoogle: false,

      // any others?
    ]
  ])
}

buildscript {
  repositories {
    google()
    jcenter()
  }
}

repositories {
  google()
  jcenter()
}

dependencies {
  implementation "com.google.firebase:firebase-core:${ReactNative.plugin.getVersionOrDefault("firebase", "core")}"
  implementation "com.google.android.gms:play-services-base:${ReactNative.plugin.getVersionOrDefault("googlePlayServices", "base")}"
}

// compile, target & build sdks, etc
ReactNative.module.applyAndroidVersions()

// custom maven { } to add RN sources to project
ReactNative.module.addReactNativeDependency()
```

 



