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
      compileSdkVersion ext.getVersion('android', 'compileSdk')

      // Android plugin for Gradle 3.0.0 or higher automatically uses a default version of
      // the build tools that the plugin specifies.
      // @url https://developer.android.com/studio/releases/build-tools
      if (Utilities.isGradleVersionLT('3.0')) {
        buildToolsVersion ext.getVersion('android', 'buildTools')
      }

      defaultConfig {
        targetSdkVersion ext.getVersion('android', 'targetSdk')
        minSdkVersion ext.getVersion('android', 'minSdk')
      }
    }
  }

  /**
   *
   * @param type
   */
  void applyReactNativeDependency(String type) {
    Boolean found = false
    File defaultDir = null
    String androidSourcesName = "${project.name} -> React Native sources"
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
      project.repositories.add(project.repositories.maven {
        url defaultDir.toString()
        name androidSourcesName
      })

      println ":default:${project.name}:${ProjectExtension.OPTION_RN_ANDROID_DIR} ${defaultDir.canonicalPath}"
      found = true
    } else {
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
          project.repositories.add(project.repositories.maven {
            url androidPrebuiltBinaryDir.toString()
            name androidSourcesName
          })

          println ":1:${project.name}:${ProjectExtension.OPTION_RN_ANDROID_DIR} ${androidPrebuiltBinaryDir.canonicalPath}"
          found = true
          break
        }

        if (androidSourcesDir.exists()) {
          project.repositories.add(project.repositories.maven {
            url androidSourcesDir.toString()
            name androidSourcesName
          })

          println ":3:${project.name}:${ProjectExtension.OPTION_RN_ANDROID_DIR} ${androidSourcesDir.canonicalPath}"
          found = true
          break
        }
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
        project.dependencies.add("implementation", "com.facebook.react:react-native:+")
        break
      default:
        project.dependencies.add("api", "com.facebook.react:react-native:+")
    }
  }


  @Override
  LinkedHashMap getExtensions() {
    ReactNativeModule target = this
    return [
      applyAndroidVersions      : target.&applyAndroidVersions,
      applyReactNativeDependency: target.&applyReactNativeDependency,
    ]
  }

  @Override
  String getName() {
    return 'module'
  }
}
