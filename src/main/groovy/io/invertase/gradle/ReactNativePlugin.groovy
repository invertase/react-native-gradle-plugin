package io.invertase.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class ReactNativePlugin implements Plugin<Project>, WithExtensions {
  private Project project
  private ReactNativeProject reactNativeProject
  private ReactNativeModule reactNativeModule

  @Override
  void apply(Project project) {
    this.project = project

    if (project == project.rootProject) {
      this.reactNativeProject = new ReactNativeProject(project)
    } else {
      this.reactNativeModule = new ReactNativeModule(project)
    }

    applyExtensions()
  }

  private void applyExtensions() {
    LinkedHashMap<String, LinkedHashMap<String, Closure>> extensions = getExtensions()

    if (reactNativeProject) {
      extensions.put(reactNativeProject.getName(), reactNativeProject.getExtensions())
    }

    if (reactNativeModule) {
      extensions.put(reactNativeModule.getName(), reactNativeModule.getExtensions())
    }

    project.extensions.extraProperties.set('ReactNative', extensions)
  }

  @Override
  LinkedHashMap getExtensions() {
    return [
      (getName()): [
        configure                    : this.&configure,
        customVersionOrDefault       : this.&customVersionOrDefault,
        addLocalReactNativeDependency: this.&addLocalReactNativeDependency,
        applyAndroidVersions         : this.&applyAndroidVersions
      ]
    ]
  }

  @Override
  String getName() {
    return 'plugin'
  }
}
