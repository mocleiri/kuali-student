package org.kuali.student.mojo;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Says "Hi" to the user.
 * @goal kscontractdoc
 */
public class KSContractDocMojo extends AbstractMojo
{
    public void execute() throws MojoExecutionException
    {
        getLog().info("Hello, world.");
    }
}
