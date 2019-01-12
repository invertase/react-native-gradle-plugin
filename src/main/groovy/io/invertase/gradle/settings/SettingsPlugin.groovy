package io.invertase.gradle.settings

import io.invertase.gradle.common.WithExtensions
import org.gradle.api.Plugin
import org.gradle.api.initialization.ProjectDescriptor
import org.gradle.api.initialization.Settings
import org.gradle.initialization.DefaultSettings

class SettingsPlugin implements Plugin<DefaultSettings>, WithExtensions {
  private Settings settings
  private ProjectDescriptor rootProject
  private DefaultSettings defaultSettings

  @Override
  void apply(DefaultSettings defaultSettings) {
    this.defaultSettings = defaultSettings
    this.settings = defaultSettings.settings
    this.rootProject = defaultSettings.rootProject

    addReactNativeModules()
  }

  void addReactNativeModules() {
    // TODO was testing adding in modules automatically into settings.gradle, modules could be read from package.json etc
    // TODO it's possible to add native modules automatically and import automatically
    // TODO in MainApplication.java via reflection... do we want that though... :P
    // defaultSettings.include(':jet')
    // defaultSettings.project(':jet').projectDir = new File(rootProject.projectDir, './../../node_modules/jet/android')
    // defaultSettings.include(':@react-native-firebase/analytics')
    // defaultSettings.project(':@react-native-firebase/analytics').projectDir = new File(rootProject.projectDir, './../../packages/analytics/android')
  }

  @Override
  LinkedHashMap getExtensions() {
    return [
      (getName()): [
        getName: this.&getName
      ]
    ]
  }

  @Override
  String getName() {
    return 'settings'
  }
}
