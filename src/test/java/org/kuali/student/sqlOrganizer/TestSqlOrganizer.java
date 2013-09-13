package org.kuali.student.sqlOrganizer;



import com.akiban.sql.*;
import com.akiban.sql.parser.*;
import org.junit.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: larry
 * Date: 8/30/13
 * Time: 7:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestSqlOrganizer {
    private String subProject;
    private String module;

    public enum TableModule {
        // ENUM(module.endsWith)
        RICE("Rice", "rice-sql"),
        KSCM("KS Curriculum Managment", "lum-sql"),
        KSENR("KS Enrollment", "enroll-sql"),
        KSCORE("KS Core", "core-sql"),
        EXCEPTION("UNKNOWN", "exception-sql");

        private final String label;
        private final String endsWith;

        private TableModule(String label, String endsWith) {
            this.label = label;
            this.endsWith = endsWith;
        }

        private String getEndsWith() {
            return endsWith;
        }

        private String getLabel() {
            return label;
        }
    }


    @Test
    public void testGetTablesNames()  {
        String sql = "SELECT * FROM MY_TABLE1, MY_SCHEMA.MY_TABLE2, (SELECT * FROM MY_TABLE3) AS TABLE_3" +
                " LEFT OUTER JOIN MY_TABLE4 ON TABLE_3.ID=MYTABLE4.ID2"+
                " WHERE ID = (SELECT MAX(ID) FROM MY_TABLE5) AND ID2 IN (SELECT * FROM MY_TABLE6)\n/ \n" +
                "INSERT INTO KRMS_TABLE VALUES ('VAL_1', 'VAL_2', '', null)\n       /\n" +
                "UPDATE KSEN_TABLE set COL_1=true\n/\n" +
                "           DELETE FROM KSLU_TABLE WHERE ID IN (SELECT CLU_ID FROM KSEN_TABLE WHERE KSEN_COL>1)\n/";

        // split based on / on it's own line (with any whitespace before and after and a newline or EOF)
        // broken up with + for clarity
        String[] statements = splitStatements(sql);

        assertTrue(statements.length == 4);
        checkStatement(statements[0],6);
        checkStatement(statements[1],1);
        checkStatement(statements[2],1);
        checkStatement(statements[3],2);

    }

    public String[] splitStatements (String sqlStatements) {
        return sqlStatements.split("\n\\s*" + "/" + "\\s*" + "(\n|\\Z)");
    }

    @Test
    public void testProcessFiles() throws IOException {
        String projectPath = "C:\\data\\development\\intellijProjects\\enr-1.0-aggregate";
        subProject = "ks-enroll";
        module = subProject + "-sql";
        String modulePath = projectPath + "\\" + subProject + "\\" + module;
        String resourceListingFile =  modulePath + "\\target\\classes\\META-INF\\org\\kuali\\student\\" + module + "\\oracle\\other.resources";

        BufferedReader br = new BufferedReader(new FileReader(resourceListingFile));
        String sqlFile;
        while ((sqlFile = br.readLine()) != null) {
            sqlFile = sqlFile.replace("classpath:",modulePath + "\\src\\main\\resources\\");
            processSqlFile(sqlFile);
        }
        br.close();
    }

    public void processSqlFile(String sqlFile) throws IOException {
        System.out.println("parsing file: " + sqlFile);
        String sqlStatments = fileToString(sqlFile);

        processSqlStatements(sqlStatments);
    }

    private String fileToString(String sqlFile) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(sqlFile));
        return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(encoded)).toString();
    }

    private void processSqlStatements(String sqlStatments) {
        String[] statements = splitStatements(sqlStatments);
        System.out.println("num of statements in file: " + statements.length);
        for (String statement : statements) {
            Map<TableModule, List> tableReport = new HashMap<TableModule, List>();

            List<String> tableNames = null;
            try {
                tableNames = getTableNamesForStatement(statement);

                for (String table : tableNames) {
                    addItemToReport(tableReport, table);
                }

                String report = getTableReport(tableReport);
                if (report.length() > 0) {
                    System.out.println(report);
                }
            } catch (StandardException e) {
                System.out.println("Error parsing statement: " + statement);
                System.out.println(e);  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }


    private boolean isCMTable(String table) {
        return table.startsWith("KSLU");
    }

    private boolean isEnrTable(String table) {
        return table.startsWith("KSEN");
    }

    private boolean isRiceTable(String table) {
        return table.startsWith("KR");
    }

    private boolean isKSTable(String table) {
        return table.startsWith("KS");
    }


    private String getTableReport(Map<TableModule, List> tableReport) {
        Set<TableModule> keys = tableReport.keySet();
        StringBuilder report = new StringBuilder();
        if (keys.size() == 1) {
            TableModule key = keys.iterator().next();
            if (!tableTypeBelongs(key)) {
                List<String> tables = tableReport.get(key);
                report.append ("statement contains tables that belong in another module. " + tables.toString() + "\n");
            }
        } else if (keys.size() == 0 ) {
            report.append ("No table found in statement");
        } else {
            report.append("ERROR statement contains multiple table modules." + "\n");
            for (TableModule key : keys) {
                List<String> tables = tableReport.get(key);
                report.append(key.getLabel() + " " + tables.toString() + "\n");
            }

        }


        return report.toString();
    }

    private boolean tableTypeBelongs(TableModule tableType) {
        if (module.endsWith(tableType.getEndsWith())) {
            return true;
        }
        return false;
    }


    private void addItemToReport(Map<TableModule, List> tableReport, String table) {
        TableModule category = getTableModule(table);


        List contents = tableReport.get(category);
        if (contents == null) {
            contents = new ArrayList<String>();
        }
        contents.add(table);
        tableReport.put(category,contents);
    }

    private TableModule getTableModule(String table) {
        if (isRiceTable(table)) {
            return TableModule.RICE;
        } else if (isCMTable(table)) {
            return TableModule.KSCM;
        } else if (isEnrTable(table)) {
            return TableModule.KSENR;
        } else if (isKSTable(table)) {
            return TableModule.KSCORE;
        } else {
            return TableModule.EXCEPTION;
        }


    }


    private List<String> getTableNamesForStatement(String statement) throws StandardException {
        SQLParser parser = new SQLParser();
        StatementNode stmt = parser.parseStatement(statement);
        NodeVisitor nodeVisitor = new NodeVisitor();
        return nodeVisitor.getTableNames(stmt);
    }


    private void checkStatement(String statement, int expectedNumTables) {
        System.out.println(statement.trim());
        List tableNames = null;
        try {
            tableNames = getTableNamesForStatement(statement);
        } catch (StandardException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        assertTrue(tableNames.size() == expectedNumTables);
        System.out.println(tableNames.toString()+"\n");
    }
}
