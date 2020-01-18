package eu.prismacapacity.unixuid_maven_plugin;

import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Tiny plugin that contributes a property 'os.detected.userid' to a maven session on unix systems. 
 * @author uwe
 *
 */
@Mojo(name = "unixuid", threadSafe = true, defaultPhase = LifecyclePhase.INITIALIZE, executionStrategy = "always", requiresProject = true)
public class UnixUidMojo extends AbstractMojo {
	
	@Parameter(defaultValue = "${project.properties}", required = true)
	private Map<String, String> properties;

	@Parameter(defaultValue = "${project}", readonly = true)
	private MavenProject project;

	@SuppressWarnings("restriction")
	public void execute() throws MojoExecutionException {
		if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
			getLog().info("Skipping because windows was detected");
		} else {
			long uid = new com.sun.security.auth.module.UnixSystem().getUid();
			getLog().debug("Adding property: os.detected.userid=" + uid);

			project.getProperties().put("os.detected.userid", String.valueOf(uid));
		}

	}
}
