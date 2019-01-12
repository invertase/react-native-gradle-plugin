package io.invertase.gradle

import org.gradle.api.Project

class ReactNativeModule implements WithExtensions {
  private Project project

  ReactNativeModule(Project project) {
    this.project = project
    if (!project.hasProperty('android')) {
      project.configure(project) {
        apply plugin: 'com.android.library'
      }
    }
  }

  void applyAndroidVersions() {
    PluginExtension ext = PluginExtension.getSharedInstance()
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
