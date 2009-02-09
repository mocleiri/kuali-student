/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 */
package com.totsp.mavenplugin.gwt.scripting;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.StringUtils;

import com.totsp.mavenplugin.gwt.AbstractGWTMojo;
import com.totsp.mavenplugin.gwt.DebugMojo;
import com.totsp.mavenplugin.gwt.util.BuildClasspathUtil;
import com.totsp.mavenplugin.gwt.util.DependencyScope;

/**
 * Handler for writing cmd scripts for the windows platform.
 * 
 * @author ccollins
 * @author rcooper
 */
public class ScriptWriterWindows implements ScriptWriter {
    private static final String CLASSWORLDS_CONF_FILENAME = "classworlds.conf";

    public ScriptWriterWindows() {
    }

    /**
     * Write run script.
     */
    public File writeRunScript(AbstractGWTMojo mojo) throws MojoExecutionException {
        
        File classworldsConf = null;
        try {
            classworldsConf = writeClassworldsConf(mojo, DependencyScope.RUNTIME);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DependencyResolutionRequiredException e) {
            e.printStackTrace();
        }
        
        String filename = (mojo instanceof DebugMojo) ? "debug.cmd" : "run.cmd";
        File file = new File(mojo.getBuildDir(), filename);
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
       
        writeVariables(writer, mojo, classworldsConf);
        writer.print("set BOOT_OPTS=%BOOT_OPTS% \"-Dcatalina.base="); writer.print(mojo.getTomcat().getAbsolutePath()); writer.println("\"");
        writer.print("set ARGS= -gen \"");
        
        writer.print(mojo.getGen().getAbsolutePath());
        writer.print("\" -logLevel ");
        writer.print(mojo.getLogLevel());
        writer.print(" -style ");
        writer.print(mojo.getStyle());
        //writer.print(" -out ");
        writer.print(" -war ");
        writer.print("\"" + mojo.getOutput().getAbsolutePath() + "\"");
        writer.print(" -port ");
        writer.print(Integer.toString(mojo.getPort()));

        writer.print(" -startupUrl ");
        writer.print(mojo.getStartupUrl());
        
        if (mojo.isNoServer()) {
            writer.print(" -noserver ");
        }

        writer.print(" " + mojo.getRunTarget());
        writer.println();
        
        writer.println("\"%JAVA%\" %JAVA_OPTS% -classpath \"%CLASSPATH%\" %BOOT_OPTS% org.codehaus.classworlds.Launcher %ARGS%");
        
        writer.flush();
        writer.close();

        return file;
    }

    /**
     * Write compile script.
     */
    public File writeCompileScript(AbstractGWTMojo mojo) throws MojoExecutionException {
        
        File classworldsConf = null;
        try {
            classworldsConf = writeClassworldsConf(mojo, DependencyScope.COMPILE);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DependencyResolutionRequiredException e) {
            e.printStackTrace();
        }
        
        File file = new File(mojo.getBuildDir(), "compile.cmd");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        writeVariables(writer, mojo, classworldsConf);       

        for (String target : mojo.getCompileTarget()) {            
            writer.print("set ARGS=-gen \"");
            writer.print(mojo.getGen().getAbsolutePath());
            writer.print("\" -logLevel ");
            writer.print(mojo.getLogLevel());
            writer.print(" -style ");
            writer.print(mojo.getStyle());

            //writer.print(" -out ");
            writer.print(" -war ");
            writer.print("\"" + mojo.getOutput().getAbsolutePath() + "\"");
            writer.print(" ");

            if (mojo.isEnableAssertions()) {
                writer.print(" -ea ");
            }

            writer.print(target);
            writer.println();
            writer.println("\"%JAVA%\" %JAVA_OPTS% -classpath \"%CLASSPATH%\" %BOOT_OPTS% org.codehaus.classworlds.Launcher %ARGS%");
        }

        writer.flush();
        writer.close();

        return file;
    }    

    /**
     * Write i18n script.
     */
    public File writeI18nScript(AbstractGWTMojo mojo) throws MojoExecutionException {
        File file = new File(mojo.getBuildDir(), "i18n.cmd");
        PrintWriter writer = this.getPrintWriterWithClasspath(mojo, file, DependencyScope.COMPILE);

        // constants
        if (mojo.getI18nConstantsNames() != null) {
            for (String target : mojo.getI18nConstantsNames()) {
                String extra = (mojo.getExtraJvmArgs() != null) ? mojo.getExtraJvmArgs() : "";

                writer.print("\"" + AbstractGWTMojo.JAVA_COMMAND + "\" " + extra + " -cp %CLASSPATH%");
                writer.print(" com.google.gwt.i18n.tools.I18NSync");
                //writer.print(" -out ");
                writer.print(" -war ");
                writer.print("\"" + mojo.getI18nOutputDir() + "\"");
                writer.print(" ");
                writer.print(target);
                writer.println();
            }
        }

        // messages
        if (mojo.getI18nMessagesNames() != null) {
            for (String target : mojo.getI18nMessagesNames()) {
                String extra = (mojo.getExtraJvmArgs() != null) ? mojo.getExtraJvmArgs() : "";

                writer.print("\"" + AbstractGWTMojo.JAVA_COMMAND + "\" " + extra + " -cp %CLASSPATH%");
                writer.print(" com.google.gwt.i18n.tools.I18NSync");
                writer.print(" -createMessages ");
                //writer.print(" -out ");
                writer.print(" -war ");
                writer.print("\"" + mojo.getI18nOutputDir() + "\"");
                writer.print(" ");
                writer.print(target);
                writer.println();
            }
        }

        writer.flush();
        writer.close();

        return file;
    }
    
    /**
     * Write test scripts.
     */
    public void writeTestScripts(AbstractGWTMojo mojo) throws MojoExecutionException {
        
        // get extras
        String extra = (mojo.getExtraJvmArgs() != null) ? mojo.getExtraJvmArgs() : "";        
        String testExtra = mojo.getExtraTestArgs() != null ? mojo.getExtraTestArgs() : ""; 
        
        // make sure output dir is present
        File outputDir = new File(mojo.getBuildDir(), "gwtTest");
        outputDir.mkdirs();
        outputDir.mkdir();

        // for each test compile source root, build a test script
        List<String> testCompileRoots = mojo.getProject().getTestCompileSourceRoots();
        for (String currRoot : testCompileRoots) {

            Collection<File> coll = FileUtils.listFiles(new File(currRoot),
                    new WildcardFileFilter(mojo.getTestFilter()), HiddenFileFilter.VISIBLE);

            for (File currFile : coll) {
                
                String testName = currFile.toString();                
                mojo.getLog().debug(("gwtTest test match found (after filter applied) - " + testName));

                // parse off the extension
                if (testName.lastIndexOf('.') > testName.lastIndexOf(File.separatorChar)) {
                    testName = testName.substring(0, testName.lastIndexOf('.'));
                }
                if (testName.startsWith(currRoot)) {
                    testName = testName.substring(currRoot.length());
                }
                if (testName.startsWith(File.separator)) {
                    testName = testName.substring(1);
                }
                testName = StringUtils.replace(testName, File.separatorChar, '.');
                mojo.getLog().debug("testName after parsing - " + testName);

                // start script inside gwtTest output dir, and name it with test class name
                File file = new File(mojo.getBuildDir() + File.separator + "gwtTest", "gwtTest-" + testName + ".cmd");
                PrintWriter writer = this.getPrintWriterWithClasspath(mojo, file, DependencyScope.TEST);

                // build Java command
                writer.print("\"" + AbstractGWTMojo.JAVA_COMMAND + "\" ");
                if (extra.length() > 0) {
                    writer.print(" " + extra + " ");
                }
                if (testExtra.length() > 0) {
                    writer.print(" " + testExtra + " ");
                }
                writer.print(" -cp %CLASSPATH% ");
                writer.print("junit.textui.TestRunner ");
                writer.print(testName);

                // write script out                
                writer.flush();
                writer.close();
            }
        }      
    }

    /**
     * Util to get a PrintWriter with Windows preamble.
     * 
     * @param mojo
     * @param file
     * @return
     * @throws MojoExecutionException
     */
    private PrintWriter getPrintWriterWithClasspath(final AbstractGWTMojo mojo, File file, final DependencyScope scope)
            throws MojoExecutionException {

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(file));
            writer.println("@echo off");
        } catch (IOException e) {
            throw new MojoExecutionException("Error creating script - " + file, e);
        }

        try {
            Collection<File> classpath = BuildClasspathUtil.buildClasspathList(mojo, scope);
            writer.print("set CLASSPATH=");

            StringBuffer cpString = new StringBuffer();

            for (File f : classpath) {
                cpString.append("\"" + f.getAbsolutePath() + "\";");

                // break the line at 4000 characters to try to avoid max size
                if (cpString.length() > 4000) {
                    writer.println(cpString);
                    cpString = new StringBuffer();
                    writer.print("set CLASSPATH=%CLASSPATH%;");
                }
            }
            writer.println(cpString);
            writer.println();
        } catch (DependencyResolutionRequiredException e) {
            throw new MojoExecutionException("Error creating script - " + file, e);
        }

        writer.println();
        return writer;
    }
    
    /**
     * Write a configuration file for classworlds. This is useful to work around the maximum
     * command line length problem in Windows.
     * 
     * @param mojo
     * @throws IOException
     * @throws DependencyResolutionRequiredException
     * @throws MojoExecutionException 
     */
    private File writeClassworldsConf(AbstractGWTMojo mojo, final DependencyScope scope) throws IOException, DependencyResolutionRequiredException, MojoExecutionException {
        String filename;
        String main;
        if (scope.compareTo(DependencyScope.COMPILE) == 0) {
            filename = "compile-" + CLASSWORLDS_CONF_FILENAME;
            //main = "com.google.gwt.dev.GWTCompiler";
            main = "com.google.gwt.dev.Compiler";
        } else {
            filename = "run-" + CLASSWORLDS_CONF_FILENAME;
            //main = "com.google.gwt.dev.GWTShell";
            main = "com.google.gwt.dev.HostedMode";
        }
        File file = new File(mojo.getBuildDir(), filename);
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        try {
            Collection<File> classpath = BuildClasspathUtil.buildClasspathList(mojo, scope);
            
            
            writer.print("main is "); writer.print(main); writer.println(" from gwt");
            writer.println();
            writer.println("[gwt]");
            // first listed must be the special GWT patch for classworlds support.
            // this overrides a GWT-supplied class that fixes a problem in the way
            // the embedded Tomcat server sets up classloading.
           // writer.println("load " + mojo.getGWTClassworldsPatchArtifact().getFile().getAbsolutePath());
            for (File f : classpath) {
                // classworlds throws exceptions if any of these paths do not exist. 
                if (f.exists()) {
                    writer.println("load " + f.getAbsolutePath() + (f.isDirectory() ? "/" : ""));
                }
            }
        } finally {
            writer.close();
        }
        return file;
    }
    
    private void writeVariables(PrintWriter writer, AbstractGWTMojo mojo, File classworldsConf) throws MojoExecutionException {
        writer.print("set JAVA="); writer.println(AbstractGWTMojo.JAVA_COMMAND);
        if (mojo.getExtraJvmArgs() != null) {
            writer.print("set JAVA_OPTS="); writer.println(mojo.getExtraJvmArgs());
        }
        if (mojo instanceof DebugMojo) {
            writer.print("set JAVA_OPTS=-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,address=");
            writer.print(mojo.getDebugPort());
            writer.println(",suspend=y %JAVA_OPTS%");
        }
        writer.print("set CLASSPATH="); writer.println(mojo.getClassworldsArtifact().getFile().getAbsolutePath());
        writer.print("set CLASSWORLDS_CONF="); writer.println(classworldsConf.getAbsolutePath());
        writer.println("set BOOT_OPTS=\"-Dclassworlds.conf=%CLASSWORLDS_CONF%\"");
    }
    
}
