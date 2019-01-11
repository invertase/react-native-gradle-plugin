package io.invertase.gradle

import org.gradle.api.Project

class ReactNativeProject implements WithExtensions {
  private Project project

  ReactNativeProject(Project project) {
    this.project = project
  }

  @Override
  LinkedHashMap getExtensions() {
    return [:]
  }

  @Override
  String getName() {
    return 'project'
  }
}
