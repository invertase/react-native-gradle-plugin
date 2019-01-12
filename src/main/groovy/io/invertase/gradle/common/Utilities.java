package io.invertase.gradle.common;

import org.gradle.util.GradleVersion;

public class Utilities {
  public static GradleVersion getGradleVersion() {
    return GradleVersion.current();
  }

  public static Boolean isGradleVersionGT(String version) {
    int result = getGradleVersion().compareTo(GradleVersion.version(version));
    return result > 0;
  }

  public static Boolean isGradleVersionGTE(String version) {
    int result = getGradleVersion().compareTo(GradleVersion.version(version));
    return result >= 0;
  }

  public static Boolean isGradleVersionLTE(String version) {
    int result = getGradleVersion().compareTo(GradleVersion.version(version));
    return result <= 0;
  }

  public static Boolean isGradleVersionLT(String version) {
    int result = getGradleVersion().compareTo(GradleVersion.version(version));
    return result < 0;
  }
}
