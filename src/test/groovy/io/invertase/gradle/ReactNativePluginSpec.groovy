package io.invertase.gradle

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class ReactNativePluginSpec extends Specification {

    def "apply() should load the plugin"() {
        given:
        def project = ProjectBuilder.builder().build()

        when:
        project.with {
            apply plugin: 'io.invertase.gradle.react-native'
        }

        then:
        project.plugins.hasPlugin(ReactNativePlugin)
    }

}
