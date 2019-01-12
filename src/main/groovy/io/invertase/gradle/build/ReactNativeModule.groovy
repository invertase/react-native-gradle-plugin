package io.invertase.gradle.build

import io.invertase.gradle.common.Utilities
import io.invertase.gradle.common.WithExtensions
import org.gradle.api.GradleException
import org.gradle.api.Project

class ReactNativeModule implements WithExtensions {
  private Project project
  private Project rootProject
  ReactNativeModule(Project project) {
    this.project = project
    this.rootProject = project.rootProject
    if (!project.hasProperty('android')) {
      project.configure(project) {
        apply plugin: 'com.android.library'
      }
    }
  }

  /**
   *
   */
  void applyAndroidVersions() {
    ProjectExtension ext = ProjectExtension.getSharedInstance()
    project.android {
      compileSdkVersion ext.getCustomVersionOrDefault('android', 'compileSdk')

      // Android plugin for Gradle 3.0.0 or higher automatically uses a default version of
      // the build tools that the plugin specifies.
      // @url https://developer.android.com/studio/releases/build-tools
      if (Utilities.isGradleVersionLT('3.0')) {
        buildToolsVersion ext.getCustomVersionOrDefault('android', 'buildTools')
      }

      defaultConfig {
        targetSdkVersion ext.getCustomVersionOrDefault('android', 'targetSdk')
        minSdkVersion ext.getCustomVersionOrDefault('android', 'minSdk')
      }
    }
  }

  /**
   *
   * @param type
   */
  void addReactNativeDependency(type) {
    Boolean found = false
    File defaultDir = null
    String androidSourcesName = "React Native sources"
    String customDir = ProjectExtension.getSharedInstance().getOption(ProjectExtension.OPTION_RN_ANDROID_DIR, true)

    if (customDir != null) {
      defaultDir = new File(
        project.projectDir,
        customDir
      )

      if (!defaultDir.exists()) {
        throw new GradleException(
          "Could not resolve custom directory specified in option '${ProjectExtension.OPTION_RN_ANDROID_DIR}': ${defaultDir.toString()}"
        )
      }
    } else {
      defaultDir = new File(
        project.projectDir,
        '/../../../node_modules/react-native/android'
      )
    }


    if (defaultDir.exists()) {
      project.repositories {
        maven {
          url defaultDir.toString()
          name androidSourcesName
        }
      }

      println ":${project.name}:${ProjectExtension.OPTION_RN_ANDROID_DIR} ${defaultDir.canonicalPath}"

      return
    }


    File parentDir = rootProject.projectDir

    for (int i = 0; i < 5; i++) {
      parentDir = parentDir.parentFile

      File androidSourcesDir = new File(
        parentDir,
        'node_modules/react-native'
      )

      File androidPrebuiltBinaryDir = new File(
        parentDir,
        'node_modules/react-native/android'
      )

      if (androidPrebuiltBinaryDir.exists()) {
        project.repositories {
          maven {
            url androidPrebuiltBinaryDir.toString()
            name androidSourcesName
          }
        }

        println ":${project.name}:${ProjectExtension.OPTION_RN_ANDROID_DIR} ${androidPrebuiltBinaryDir.canonicalPath}"
        found = true
        break
      }

      if (androidSourcesDir.exists()) {
        project.repositories {
          maven {
            url androidSourcesDir.toString()
            name androidSourcesName
          }
        }

        println ":${project.name}:${ProjectExtension.OPTION_RN_ANDROID_DIR} ${androidSourcesDir.canonicalPath}"
        found = true
        break
      }
    }


    if (!found) {
      throw new GradleException(
        ":${project.name}: unable to locate React Native android sources. " +
          "Ensure you have you installed React Native as a dependency in your project and try again."
      )
    }


    switch (type) {
      case "implementation":
        project.dependencies {
          implementation "com.facebook.react:react-native:+"
        }
        break
      default:
        project.dependencies {
          api "com.facebook.react:react-native:+"
        }
    }
  }


  @Override
  LinkedHashMap getExtensions() {
    return [
      applyAndroidVersions: this.&applyAndroidVersions
    ]
  }

  @Override
  String getName() {
    return 'module'
  }
}
