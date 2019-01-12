package io.invertase.gradle.build

import io.invertase.gradle.common.WithExtensions
import io.invertase.gradle.inspections.BuildFailureInspection
import org.gradle.api.Project

class ReactNativeProject implements WithExtensions {
  private Project project

  ReactNativeProject(Project project) {
    this.project = project
    BuildFailureInspection.applyToProject(project)
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
