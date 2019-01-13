package io.invertase.gradle.build

import io.invertase.gradle.common.Utilities
import io.invertase.gradle.common.WithExtensions
import org.gradle.api.Plugin
import org.gradle.api.Project

class ReactNativePlugin implements Plugin<Project>, WithExtensions {
  private Project project
  private ReactNativeProject reactNativeProject
  private ReactNativeModule reactNativeModule
  private ReactNativeShared reactNativeShared

  @Override
  void apply(Project project) {
    this.project = project

    if (project == project.rootProject) {
      ProjectExtension.getSharedInstance().setProject(project, true)
      this.reactNativeProject = new ReactNativeProject(project)
      this.reactNativeShared = new ReactNativeShared(project, true)
    } else {
      ProjectExtension.getSharedInstance().setProject(project, false)
      this.reactNativeModule = new ReactNativeModule(project)
      this.reactNativeShared = new ReactNativeShared(project, false)
    }

    createExtensions()
  }

  private void createExtensions() {
    LinkedHashMap<String, LinkedHashMap<String, Closure>> extensions = getExtensions()

    if (reactNativeProject) {
      extensions.put(reactNativeProject.getName(), reactNativeProject.&getExtensions())
    }

    if (reactNativeModule) {
      extensions.put(reactNativeModule.getName(), reactNativeModule.&getExtensions())
    }

    extensions.put(reactNativeShared.getName(), reactNativeShared.&getExtensions())

    project.extensions.extraProperties.set('ReactNative', extensions)
  }

  @Override
  LinkedHashMap getExtensions() {
    return [
      ext : ProjectExtension.&getSharedInstance(),
      util: Utilities
    ]
  }

  @Override
  String getName() {
    return 'plugin'
  }
}
