package net.pandoragames.far.commandline;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import net.pandoragames.far.ui.UIBean;
import net.pandoragames.far.ui.model.FileNamePattern;
import net.pandoragames.far.ui.model.FindForm;
import net.pandoragames.far.ui.model.MessageBox;
import net.pandoragames.far.ui.model.ReplaceForm;
import net.pandoragames.far.ui.model.TargetFile;
import net.pandoragames.far.ui.swing.component.FileSetTableModel;
import net.pandoragames.util.i18n.DummyLocalizer;
import net.pandoragames.util.i18n.Localizer;

public class CommandLine {

    public static void main(String[] args) throws IOException {
        System.out.println("CommandLine execution...");
        String baseDirectory = "D:\\svn\\org-fixer";
        if (args.length > 0) {
            baseDirectory = args[0];
        }
        String fileNames = "*.java, *.xml, *.sql";
        if (args.length > 1) {
            fileNames = args[1];
        }
        String searchString = "kuali.org.Department";
        if (args.length > 2) {
            searchString = args[2];
        }
        String replacementString = "kuali.type.org.department";
        if (args.length > 3) {
            replacementString = args[3];
        }

        MessageBox messageBox = new TestBox();
        Localizer localizer = new DummyLocalizer();
        UIBean uiBean = new UIBean(messageBox, localizer);
        List<TargetFile> fileList = new ArrayList<TargetFile>();
        FileSetTableModel fileSetTableModel = new FileSetTableModel(fileList, localizer);
        FindForm findForm = new FindForm();
        findForm.setIncludeSubDirs(true);
        findForm.setRegexContentPattern(false);
        findForm.setBaseDirectory(new File(baseDirectory));
        FileNamePattern fileNamePattern = new FileNamePattern(fileNames);
        findForm.setFileNamePattern(fileNamePattern);
        findForm.setSearchStringContent(searchString);
        List<TargetFile> result = uiBean.findFiles(findForm);
        fileSetTableModel.setFileList(result, findForm.getBaseDirectory());
        System.out.println ("Found " + result.size() + " files");
        ReplaceForm replaceForm = new ReplaceForm();
        replaceForm.setDoBackup(false);
        replaceForm.setCharacterset(Charset.defaultCharset());
        replaceForm.setRegexContentPattern(false);
        replaceForm.setSearchStringContent(searchString);
        replaceForm.setReplacementString(replacementString);
        uiBean.replace(replaceForm, fileSetTableModel.listRows());

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
