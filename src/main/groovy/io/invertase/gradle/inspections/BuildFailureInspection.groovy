package io.invertase.gradle.inspections

import org.gradle.api.Project

class BuildFailureInspection {
  private Project project
  private Project rootProject
  private String failureCause = ""
  static BuildFailureInspection sharedInstance = new BuildFailureInspection()

  private String DUPE_DEX_1 = "com.android.dex.DexException: Multiple dex files define Lcom/google/"
  private String DUPE_DEX_2 = "java.util.zip.ZipException: duplicate entry: com/google/android/gms/"

  /**
   *
   * @param project
   */
  static void applyToProject(Project project) {
    sharedInstance.setup(project)
  }

  /**
   *
   * @param project
   */
  void setup(Project project) {
    this.project = project
    this.rootProject = project.rootProject

    rootProject.gradle.buildFinished { buildResult ->
      if (buildResult.getFailure() != null) {
        try {
          this.failureCause = buildResult.getFailure().properties.get("reportableCauses").toString()
          trigger()
        } catch (Exception ignored) {
        }
      }
    }
  }

  /**
   *
   */
  private void trigger() {
    if (failureCause.contains(DUPE_DEX_1) || failureCause.contains(DUPE_DEX_2)) {
      printDuplicateDex()
      return
    }

    // TODO check for missing google services file error

    // TODO other checks
  }

  void printDuplicateDex() {
    logger.error("")
    logger.error(" -----------------------------------------------------------")
    logger.error("|                REACT NATIVE (Gradle Plugin)               |")
    logger.error(" ----------------------------------------------------------- ")
    logger.error("|                                                           |")
    logger.error("|  This is a common build error when using libraries that   |")
    logger.error("|  require google play services.                            |")
    logger.error("|                                                           |")
    logger.error("|  This error occurs because different versions of google   |")
    logger.error("|  play services or google play services modules are being  |")
    logger.error("|  required by different libraries.                         |")
    logger.error("|                                                           |")
    logger.error("|  A temporary fix would be to set:                         |")
    logger.error("|                                                           |")
    logger.error("|             android { multiDexEnabled true }              |")
    logger.error("|                                                           |")
    logger.error("|  inside your build gradle, however it is recommended for  |")
    logger.error("|  your prod build that you de-duplicate these to minimize  |")
    logger.error("|  bundle size.                                             |")
    logger.error("|                                                           |")
    logger.error("|  See http://invertase.link/dupe-dex for how to do this.   |")
    logger.error("|                                                           |")
    logger.error(" ----------------------------------------------------------- ")
    logger.error("")
  }
}
