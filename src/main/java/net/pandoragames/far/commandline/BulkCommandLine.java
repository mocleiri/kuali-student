package net.pandoragames.far.commandline;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import net.pandoragames.far.ui.UIBean;
import net.pandoragames.far.ui.model.FileNamePattern;
import net.pandoragames.far.ui.model.FindForm;
import net.pandoragames.far.ui.model.MessageBox;
import net.pandoragames.far.ui.model.ReplaceForm;
import net.pandoragames.far.ui.model.TargetFile;
import net.pandoragames.far.ui.swing.component.FileSetTableModel;
import net.pandoragames.util.i18n.DummyLocalizer;
import net.pandoragames.util.i18n.Localizer;

public class BulkCommandLine {

    private static final String BASE_DIRECTORY_PROPERTY = "base.directory";
    private static final String FILE_NAMES_PATTERN_PROPERTY = "file.names.pattern";

    public static void main(String[] args) throws IOException {
        System.out.println("BulkCommandLine execution...");

        String bulkCommandLineProperties = "/bulkcommandline.properties";
        if (args.length > 0) {
            bulkCommandLineProperties = args[0];
        }
        Properties props = new Properties();
        File file = new File(bulkCommandLineProperties);
        if (file.exists()) {
            InputStream in = new FileInputStream(file);
            props.load(in);
            in.close();
        } else {
            InputStream in = BulkCommandLine.class.getResourceAsStream(bulkCommandLineProperties);
            if (in != null) {
                props.load(in);
                in.close();
            }
        }
        if (props.isEmpty()) {
            throw new IllegalArgumentException ("Could not find properties file " + bulkCommandLineProperties);
        }

        String baseDirectory = "D:\\svn\\org-fixer";
        baseDirectory = props.getProperty(BASE_DIRECTORY_PROPERTY, baseDirectory);
        String fileNamesPattern = "*.java, *.xml, *.sql";
        fileNamesPattern = props.getProperty(FILE_NAMES_PATTERN_PROPERTY, fileNamesPattern);
        for (String searchString : props.stringPropertyNames()) {
            if (searchString.equals(BASE_DIRECTORY_PROPERTY)) {
                continue;
            }
            if (searchString.equals(FILE_NAMES_PATTERN_PROPERTY)) {
                continue;
            }
            String replacementString = props.getProperty(searchString);
            MessageBox messageBox = new TestBox();
            Localizer localizer = new DummyLocalizer();
            UIBean uiBean = new UIBean(messageBox, localizer);
            List<TargetFile> fileList = new ArrayList<TargetFile>();
            FileSetTableModel fileSetTableModel = new FileSetTableModel(fileList, localizer);
            FindForm findForm = new FindForm();
            findForm.setIncludeSubDirs(true);
            findForm.setRegexContentPattern(false);
            findForm.setBaseDirectory(new File(baseDirectory));
            FileNamePattern fileNamePattern = new FileNamePattern(fileNamesPattern);
            findForm.setFileNamePattern(fileNamePattern);
            findForm.setSearchStringContent(searchString);
            List<TargetFile> result = uiBean.findFiles(findForm);
            fileSetTableModel.setFileList(result, findForm.getBaseDirectory());
            System.out.println("Found " + result.size() + " files");
            ReplaceForm replaceForm = new ReplaceForm();
            replaceForm.setDoBackup(false);
            replaceForm.setCharacterset(Charset.defaultCharset());
            replaceForm.setRegexContentPattern(false);
            replaceForm.setSearchStringContent(searchString);
            replaceForm.setReplacementString(replacementString);
            uiBean.replace(replaceForm, fileSetTableModel.listRows());
        }
    }

    static class TestBox implements MessageBox {

        private String error = null;
        private String info = null;

        @Override
        public void clear() {
            error = null;
            info = null;
        }

        @Override
        public void error(String message) {
            error = message;
        }

        @Override
        public void info(String message) {
            info = message;
        }

        public String getError() {
            return error;
        }

        public String getInfo() {
            return info;
        }
    }
}
