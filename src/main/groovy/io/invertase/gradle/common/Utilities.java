package io.invertase.gradle.common;

import org.gradle.util.GradleVersion;

public class Utilities {
  static GradleVersion getGradleVersion() {
    return GradleVersion.current();
  }

  static Boolean isGradleVersionGT(String version) {
    int result = getGradleVersion().compareTo(GradleVersion.version(version));
    return result > 0;
  }

  static Boolean isGradleVersionGTE(String version) {
    int result = getGradleVersion().compareTo(GradleVersion.version(version));
    return result >= 0;
  }

  static Boolean isGradleVersionLTE(String version) {
    int result = getGradleVersion().compareTo(GradleVersion.version(version));
    return result <= 0;
  }

  static Boolean isGradleVersionLT(String version) {
    int result = getGradleVersion().compareTo(GradleVersion.version(version));
    return result < 0;
  }
}
