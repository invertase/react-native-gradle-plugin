package io.invertase.gradle.common

import org.gradle.api.Project
import org.gradle.util.GradleVersion

class Utilities {
  static GradleVersion getGradleVersion() {
    return GradleVersion.current()
  }

  static Boolean isGradleVersionGT(String version) {
    int result = getGradleVersion() <=> GradleVersion.version(version)
    return result > 0
  }

  static Boolean isGradleVersionGTE(String version) {
    int result = getGradleVersion() <=> GradleVersion.version(version)
    return result >= 0
  }

  static Boolean isGradleVersionLTE(String version) {
    int result = getGradleVersion() <=> GradleVersion.version(version)
    return result <= 0
  }

  static Boolean isGradleVersionLT(String version) {
    int result = getGradleVersion() <=> GradleVersion.version(version)
    return result < 0
  }

  static Boolean isExpo(Project project) {
    return project.findProject(":expo-core") != null
  }

  static Boolean isFlutter(Project project) {
    File rootProjectDir = project.rootProject.projectDir
    return !isExpo(project) && new File(rootProjectDir.parentFile, 'pubspec.yaml').exists()
  }
}
