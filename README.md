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

A gradle plugin for React Native Android that simplifies and standardises build configurations (including versioning) for both Projects & React Native modules.

#### Current Features

 - Android app / React Native module auto-versioning using the version defined in your `package.json`
 - Standardise overriding android/gradle/tools sdk versions of React Native Modules
 - Standardise overriding dependency versions of React Native Modules, e.g. Firebase Android sdks
 - Built-in inspections (warning logs & suggested solutions) for common build mistakes/errors, e.g. `duplicate dex`

## Installation

Add the plugin to your `build.gradle`;

**Groovy**:
```groovy
plugins {
  id "io.invertase.gradle.build" version "1.3"
}
```

**Kotlin**:
```kotlin
plugins {
  id("io.invertase.gradle.build") version "1.3"
}
```

This must be added after your `buildscript` block.

## Usage

### For Projects & React Native Modules

#### Versioning Overrides

Add a versions configuration to your app build gradle, for example; 

```groovy
project.ext {
  set('react-native', [
    versions: [
      // Android version section can be automatically applied by calling 
      // ReactNative.module.applyAndroidVersions()
      //    or
      // ReactNative.project.applyAndroidVersions()
      // android version section supports legacy behavior e.g.:
      //    project.ext.compileSdkVersion
      android           : [
        minSdk    : 16,
        targetSdk : 28,
        compileSdk: 28,
        // optional as gradle.buildTools comes with one by default
        // buildTools: "28.0.3"
      ],

      googlePlayServices: [
        base: "16.0.1",
        // additional version sub sections as defined by native module authors
        // e.g maps:
        maps: "16.0.1",
      ],

      // additional version sections can be defined by
      // native module authors, e.g. React Native Firebase:
      firebase          : [
        functions: "16.1.3"
      ],
    ],
  ])
}
```

These additionally form the default versions when used in a react native module. 

**Example: Using a version**

```groovy
dependencies {
  api project(':@react-native-firebase/app')
  // getVersion() first looks for the overridden version defined in the consumers project
  // if non is found; it falls back to the default version defined in the React Native modules' build.gradle, as above
  implementation "com.google.firebase:firebase-functions:${ReactNative.ext.getVersion("firebase", "functions")}"
  implementation "com.google.android.gms:play-services-base:${ReactNative.ext.getVersion("googlePlayServices", "base")}"
}
```

#### Android build versioning using `package.json` version

Android builds can specify a build code and version name, e.g.:

```groovy
android {
  defaultConfig {
    versionCode 1
    versionName "1.0"
  }
}
```

Updating this every time you need to publish your app can be avoided by having it default to using the 
version defined in your projects `package.json` file, the process is explained [here](https://medium.com/@andr3wjack/versioning-react-native-apps-407469707661) 
in-depth by @AndrewJack.

This can be automated with this gradle plugin by adding the following to your app (or RN module) `build.gradle`:

```groovy
ReactNative.shared.applyPackageVersion()
```


#### Excluding common META-INF files

`META-INF` files can cause `'duplicate file'` build failures if they're not excluded, this plugin allows excluding the 
most common types of these files by adding  the following to your app (or RN module) `build.gradle`:  


```groovy
ReactNative.shared.applyDefaultExcludes()
```

For reference this will inject the following config:

```groovy
android {
  packagingOptions {
    exclude 'META-INF/-no-jdk.kotlin_module'
    exclude 'META-INF/DEPENDENCIES'
    exclude 'META-INF/NOTICE'
    exclude 'META-INF/LICENSE'
    exclude 'META-INF/LICENSE.txt'
    exclude 'META-INF/NOTICE.txt'
  }
}
```


### For React Native Modules


TODO docs:

```groovy
ReactNative.module.applyAndroidVersions()
ReactNative.module.applyReactNativeDependency("api")
```


## Planned Features

 - Support for injecting Java Constants constants (`buildConfigField`) into app from a JS script, `package.json` or `app.json`
 - Support for injecting Android Resources (resValue) into app from a JS script, `package.json` or `app.json`


## License

- See [LICENSE](/LICENSE)

----

Built and maintained with 💛 by [Invertase](https://invertase.io). 

- [💼 Hire Us](https://invertase.io/hire-us)
- [☕️ Sponsor Us](https://opencollective.com/react-native-firebase)
- [👩‍💻 Work With Us](https://invertase.io/jobs)
