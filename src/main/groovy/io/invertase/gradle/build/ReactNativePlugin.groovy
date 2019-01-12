package io.invertase.gradle.build

import io.invertase.gradle.common.WithExtensions
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
      PluginExtension.getSharedInstance().setProject(project, true)
      this.reactNativeProject = new ReactNativeProject(project)
    } else {
      PluginExtension.getSharedInstance().setProject(project, false)
      this.reactNativeModule = new ReactNativeModule(project)
    }

    createExtensions()
  }

  private void createExtensions() {
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
        getName: this.&getName
      ]
    ]
  }

  @Override
  String getName() {
    return 'plugin'
  }
}
