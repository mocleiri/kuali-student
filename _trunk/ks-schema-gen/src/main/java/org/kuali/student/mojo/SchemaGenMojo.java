package org.kuali.student.mojo;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.kuali.student.schemagen.SchemaGen;
/**
 * Generates schema from annotations
 *
 * @goal schemaGen
 * @requiresDependencyResolution 
 */
public class SchemaGenMojo extends AbstractMojo {
    /**
     * Persistence File Names.
     *
     * @parameter
     */
    private String[] persistenceFileNames;

    /**
     * Output path.
     *
     * @parameter
     */
    private String outputPath;
	
    
    /** @parameter default-value="${project}" */
    private MavenProject mavenProject;

    
    public SchemaGenMojo() {
		super();
	}
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		SchemaGen schemaGen = new SchemaGen(outputPath, persistenceFileNames, getClassLoader());
		try {
			schemaGen.generateAllDbTypes();
		} catch (Exception e) {
			throw new RuntimeException("Error generating schema.",e);
		}
	}
	
    /**
     * Returns the an isolated classloader.
     *
     * @return ClassLoader
     * @noinspection unchecked
     */
    private ClassLoader getClassLoader()
    {
        try
        {
            List classpathElements = mavenProject.getCompileClasspathElements();
            classpathElements.add( mavenProject.getBuild().getOutputDirectory() );
            classpathElements.add( mavenProject.getBuild().getTestOutputDirectory() );
            for(Object resource:mavenProject.getBuild().getResources()){
            	if(resource instanceof Resource){
            		classpathElements.add(((Resource) resource).getDirectory());
            	}
            }
            URL urls[] = new URL[classpathElements.size()];
            for ( int i = 0; i < classpathElements.size(); ++i )
            {
                urls[i] = new File( (String) classpathElements.get( i ) ).toURL();
            }
            return new URLClassLoader( urls, this.getClass().getClassLoader() );
        }
        catch ( Exception e )
        {
            getLog().debug( "Couldn't get the classloader." );
            return this.getClass().getClassLoader();
        }
    }

	public String[] getPersistenceFileNames() {
		return persistenceFileNames;
	}

	public void setPersistenceFileNames(String[] persistenceFileNames) {
		this.persistenceFileNames = persistenceFileNames;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

}
