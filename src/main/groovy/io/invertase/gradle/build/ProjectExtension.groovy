package io.invertase.gradle.build

import org.gradle.api.GradleException
import org.gradle.api.Project

class ProjectExtension {
  private Project project
  private Boolean isRoot
  private static sharedInstance = new ProjectExtension()

  public static String OPTION_RN_ANDROID_DIR = "reactNativeAndroidDir"
  public static String OPTION_USE_PACKAGE_VERSION = "usePackageVersion"

  private LinkedHashMap OPTION_DEFAULTS = [
    (OPTION_RN_ANDROID_DIR)     : null,
    (OPTION_USE_PACKAGE_VERSION): false,
  ]

  private String KEY_REACT_NATIVE = "react-native"

  /**
   *
   * @return
   */
  static ProjectExtension getSharedInstance() {
    return sharedInstance
  }

  /**
   *
   * @param project
   * @param isRoot
   */
  void setProject(Project project, Boolean isRoot) {
    this.project = project
    this.isRoot = isRoot
  }

  /**
   *
   * @param option
   * @param rootProject
   * @return
   */
  Object getOption(String option, Boolean rootProject = false) {
    LinkedHashMap reactNativeRoot = getReactNativeRoot(rootProject)

    if (reactNativeRoot.options != null && reactNativeRoot.options[option] != null) {
      return reactNativeRoot.options[option]
    }

    return OPTION_DEFAULTS[option]
  }


  /**
   *
   * @return
   */
  LinkedHashMap getReactNativeRoot(Boolean rootProject = false) {
    Project target = rootProject ? project.rootProject : project

    if (target.ext.has(KEY_REACT_NATIVE)) {
      return target.ext.get(KEY_REACT_NATIVE)
    }

    return [:]
  }

  /**
   *
   * @param child
   * @param rootProject
   * @return
   */
  LinkedHashMap getReactNativeRootChild(String child, Boolean rootProject = false) {
    LinkedHashMap reactNativeRoot = getReactNativeRoot(rootProject)
    return (reactNativeRoot[child] != null ? reactNativeRoot[child] : [:]) as LinkedHashMap
  }


  /**
   *
   * @param rootProject
   * @return
   */
  LinkedHashMap getVersionsRoot(Boolean rootProject = false) {
    return getReactNativeRootChild("versions", rootProject)
  }

  /**
   *
   * @param scope
   * @param name
   * @return
   */
  Object getCustomVersionOrDefault(String scope, String name) {
    LinkedHashMap projectVersions = getVersionsRoot(false)
    LinkedHashMap rootProjectVersions = getVersionsRoot(true)

    if (!projectVersions[scope]) {
      throw new GradleException("${project.name} has not defined project.ext.'react-native'.versions.${scope} in its build.gradle file.")
    }

    def defaultValue = projectVersions[scope][name]

    if (defaultValue == null) {
      throw new GradleException("${project.name} has not defined project.ext.'react-native'.versions.${scope}.${name} in its build.gradle file.")
    }

    def defaulted = true
    def value = defaultValue

    if (rootProjectVersions[scope] != null && rootProjectVersions[scope][name] != null) {
      value = rootProjectVersions[scope][name]
      defaulted = false
    } else if (scope == "android" && project.rootProject.ext.has(name + "Version")) {
      // support legacy behavior
      value = project.rootProject.ext.get(name + "Version")
      defaulted = false
    }

    if (defaulted) {
      println "${project.name}:${scope}.${name} using default value: ${defaultValue}"
    } else {
      println "${project.name}:${scope}.${name} using custom value: ${defaultValue}"
    }

    return value
  }
}
