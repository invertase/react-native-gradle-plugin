package io.invertase.gradle

import org.gradle.api.Project

class ReactNativeModule implements WithExtensions {
  private Project project

  ReactNativeModule(Project project) {
    this.project = project
  }

  @Override
  LinkedHashMap getExtensions() {
    return [:]
  }

  @Override
  String getName() {
    return 'module'
  }
}
