package org.kuali.student.datadictionary.mojo;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.kuali.student.contract.model.ServiceContractModel;
import org.kuali.student.contract.model.XmlType;
import org.kuali.student.contract.model.impl.ServiceContractModelCache;
import org.kuali.student.contract.model.impl.ServiceContractModelQDoxLoader;
import org.kuali.student.datadictionary.util.KradDictionaryCreator;
import org.kuali.student.contract.model.validation.ServiceContractModelValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The plugin entrypoint which is used to generate dictionary files based on the contract
 * @phase generate-sources
 * @goal ksdictionarycreator
 */
public class KSDictionaryCreatorMojo extends AbstractMojo {

	private static final Logger log = LoggerFactory.getLogger(KSDictionaryCreatorMojo.class);
	
    /**
     * @parameter expression=true
     **/
    private boolean throwExceptionIfNotAllFilesProcessed;
    /**
     * @parameter
     **/
    private List<String> sourceDirs;
    /**
     * @parameter expression="${outputDirectory}" default-value="${project.build.directory}/generated-sources/datadictionary"
     */
    private File outputDirectory;
    /**
     * @parameter expression=false
     */
    private boolean writeManual;
    /**
     * @parameter expression=true
     */
    private boolean writeGenerated;
    
    public boolean isThrowExceptionIfNotAllFilesProcessed() {
        return throwExceptionIfNotAllFilesProcessed;
    }

    public void setThrowExceptionIfNotAllFilesProcessed(boolean throwExceptionIfNotAllFilesProcessed) {
        this.throwExceptionIfNotAllFilesProcessed = throwExceptionIfNotAllFilesProcessed;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public List<String> getSourceDirs() {
        return sourceDirs;
    }

    public boolean isWriteManual() {
        return writeManual;
    }

    public boolean isWriteGenerated() {
        return writeGenerated;
    }

    public void setWriteManual(boolean writeManual) {
        this.writeManual = writeManual;
    }

    public void setWriteGenerated(boolean writeGenerated) {
        this.writeGenerated = writeGenerated;
    }

    public void setOutputDirectory(File htmlDirectory) {
        this.outputDirectory = htmlDirectory;
    }

    public void setSourceDirs(List<String> sourceDirs) {
        this.sourceDirs = sourceDirs;
    }

    private ServiceContractModel getModel() {
        ServiceContractModel instance = new ServiceContractModelQDoxLoader(
                sourceDirs);
        return new ServiceContractModelCache(instance);
    }

    private boolean validate(ServiceContractModel model) {
        Collection<String> errors = new ServiceContractModelValidator(model).validate();
        if (errors.size() > 0) {
            StringBuilder buf = new StringBuilder();
            buf.append(errors.size()).append(" errors found while validating the data.");
            return false;
        }
        return true;
    }

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("generating ks-XXX-dictionary.xml files=" + this.writeManual);
        getLog().info("generating ks-XXX-dictionary-generated.xml files=" + this.writeGenerated);
        ServiceContractModel model = this.getModel();
        this.validate(model);

        // build the list of expected files types to generate the dictionary files for.
        Set<String> lowerClasses = new HashSet<String>();
        
        for (XmlType type : model.getXmlTypes()) {
			lowerClasses.add(type.getName().toLowerCase());
		}

        String dictionaryDirectory = this.outputDirectory.toString();
        for (XmlType xmlType : model.getXmlTypes()) {
            if (lowerClasses.contains(xmlType.getName().toLowerCase())) {
                lowerClasses.remove(xmlType.getName().toLowerCase());
                String xmlObject = xmlType.getName();
                KradDictionaryCreator writer =
				        new KradDictionaryCreator(dictionaryDirectory,
				        model,
				        xmlObject,
				        writeManual,
				        writeGenerated);
                try {
					
					writer.write();
				} catch (Exception e) {
					log.warn("Generate Failed for: " + xmlObject, e);
					writer.delete();
					
				}
            }
        }
        if (!lowerClasses.isEmpty()) {
            StringBuilder buf = new StringBuilder();
            buf.append(lowerClasses.size());
            buf.append(" classes were not processed: ");
            String comma = "";
            for (String className : lowerClasses) {
                buf.append(comma);
                buf.append(className);
                comma = ", ";
            }
            if (throwExceptionIfNotAllFilesProcessed) {
                throw new MojoExecutionException(buf.toString());
            }
            else
            {
               log.info(buf.toString());
            }
        }
    }
}
