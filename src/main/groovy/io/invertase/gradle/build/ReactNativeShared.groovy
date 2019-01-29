package io.invertase.gradle.build

import io.invertase.gradle.common.PackageJson
import io.invertase.gradle.common.WithExtensions
import org.gradle.api.Project

class ReactNativeShared implements WithExtensions {
  private Project project
  private Boolean isRoot

  ReactNativeShared(Project project, Boolean isRoot) {
    this.isRoot = isRoot
    this.project = project
    if (!project.hasProperty('android')) {
      project.configure(project) {
        apply plugin: 'com.android.library'
      }
    }
  }

  /**
   *
   */
  void applyDefaultExcludes() {
    project.android {
      packagingOptions {
        exclude 'META-INF/-no-jdk.kotlin_module'
        exclude 'META-INF/DEPENDENCIES'
        exclude 'META-INF/NOTICE'
        exclude 'META-INF/LICENSE'
        exclude 'META-INF/LICENSE.txt'
        exclude 'META-INF/NOTICE.txt'
      }
    }
  }

  void applyPackageVersion() {
    Object packageJson = PackageJson.getForProject(project)

    if (packageJson != null) {
      String version = packageJson.getAt("version")
      String[] tokens = version.tokenize('.')

      int major = Integer.parseInt(tokens[0])
      int minor = Integer.parseInt(tokens[1])

      // additionally strips pre-releases off patch
      int patch = Integer.parseInt(tokens[2].tokenize('-')[0])

      int code = major * 1000000 + minor * 1000 + patch

      project.android {
        defaultConfig {
          versionCode code
          versionName version
        }
      }

      println ":${project.name}:version set from package.json: ${version} (${major},${minor},${patch} - ${code})"
    } else {
      project.android {
        defaultConfig {
          versionCode 1
          versionName "1.0"
        }
      }
    }
  }

  @Override
  LinkedHashMap getExtensions() {
    ReactNativeShared target = this
    return [
      applyDefaultExcludes: target.&applyDefaultExcludes,
      applyPackageVersion : target.&applyPackageVersion
    ]
  }

  @Override
  String getName() {
    return 'shared'
  }
}
