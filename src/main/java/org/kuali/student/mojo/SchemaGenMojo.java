package org.kuali.student.mojo;

import java.io.File;
import java.lang.reflect.Method;
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
		addDependenciesToClassLoader();
		SchemaGen schemaGen = new SchemaGen(outputPath, persistenceFileNames);
		try {
			schemaGen.generateAllDbTypes();
		} catch (Exception e) {
			throw new RuntimeException("Error generating schema.",e);
		}
	}
	

    private void addDependenciesToClassLoader()
    {
        try
        {
            URLClassLoader systemClassLoader = (URLClassLoader) this.getClass().getClassLoader(); 
            Class<URLClassLoader> classLoaderClass = URLClassLoader.class; 
            Method method = classLoaderClass.getDeclaredMethod("addURL", new Class[]{URL.class}); 
            method.setAccessible(true); 
            
        	List classpathElements = mavenProject.getCompileClasspathElements();
            classpathElements.add( mavenProject.getBuild().getOutputDirectory() );
            classpathElements.add( mavenProject.getBuild().getTestOutputDirectory() );
            for(Object resource:mavenProject.getBuild().getResources()){
            	if(resource instanceof Resource){
            		classpathElements.add(((Resource) resource).getDirectory());
            	}
            }
            for ( int i = 0; i < classpathElements.size(); ++i )
            {
            	URL url = new File( (String) classpathElements.get( i ) ).toURL();
                method.invoke(systemClassLoader, new Object[]{url}); 
            }
            
        }
        catch ( Exception e )
        {
            getLog().debug( "Couldn't add to the classloader.",e);
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
