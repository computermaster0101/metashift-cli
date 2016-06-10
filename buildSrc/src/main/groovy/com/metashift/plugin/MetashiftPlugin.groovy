
package com.minds.metashift.plugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.BasePlugin
import org.springframework.boot.gradle.SpringBootPlugin

/**
 * HAcked from -- Gradle 'Spring Boot' {@link Plugin}.
 *
 * To provide the ability to launch the metashift from gradle and use System.in
 *
 * @author Phillip Webb
 * @author Dave Syer
 */
public class MetashiftPlugin implements Plugin<Project> {

	@Override
	void apply(Project project) {
		project.getPlugins().apply(BasePlugin)

		// HAxored -- to support running shell from gradle
		new MetashiftPluginFeatures().apply(project)

		// This must be applied after the Metashift shell plugin since it will apply some logic that is needed at runtime.
		// Mainly the findMainClass task that gets added by the bootRun task application
		project.getPlugins().apply(SpringBootPlugin)

		useUtf8Encoding(project)
	}

	private useUtf8Encoding(Project project) {
		project.tasks.withType(org.gradle.api.tasks.compile.JavaCompile).all {
			it.doFirst {
				if(!it.options.encoding) {
					it.options.encoding = 'UTF-8'
				}
			}
		}
	}
}
