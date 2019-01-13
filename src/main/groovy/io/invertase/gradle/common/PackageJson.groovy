package io.invertase.gradle.common

import groovy.json.JsonSlurper
import org.gradle.api.Project

class PackageJson {
  static private Map projectPackageCache = new HashMap(5)

  static Object getForProject(Project project) {
    if (projectPackageCache.hasProperty(project.name)) {
      return projectPackageCache.get(project.name)
    }

    File packageJson = null
    File parentDir = project.projectDir

    for (int i = 0; i <= 3; i++) {
      parentDir = parentDir.parentFile

      packageJson = new File(
        parentDir,
        'package.json'
      )

      if (packageJson.exists()) break
    }


    if (packageJson != null) {
      println ":${project.name} package.json found at ${packageJson.toString()}"
      Object json = null

      try {
        json = new JsonSlurper().parseText(packageJson.text)
      } catch (Exception ignored) {
      }

      if (json == null) {
        project.logger.warn ":${project.name} failed to parse package.json found at ${packageJson.toString()}"
        return json
      }

      projectPackageCache.put(project.name, json)
      return json
    }


    println ":${project.name} unable to locate a package.json file relative to this project"
    return null
  }

}
