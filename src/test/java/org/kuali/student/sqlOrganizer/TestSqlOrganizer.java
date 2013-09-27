package org.kuali.student.sqlOrganizer;



import com.akiban.sql.*;
import com.akiban.sql.parser.*;
import org.junit.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
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
    private List<String> unparsableStmts;


    public enum DatabaseModule {
        // ENUM(module.endsWith)
        RICE("Rice", "rice-sql"),
        KSCM("KS Curriculum Managment", "lum-sql"),
        KSENR("KS Enrollment", "enroll-sql"),
        KSCORE("KS Core", "core-sql"),
        EXCEPTION("UNKNOWN", "exception-sql");

        private final String label;
        private final String endsWith;

        private DatabaseModule(String label, String endsWith) {
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

    @Before
    public void init() {
        this.unparsableStmts = new ArrayList<String>();
    }

    @After
    public void printSummary() {
        if (this.unparsableStmts.size() > 0) {
            System.out.println("\n\nUnparsable Statements");
            StringBuilder sbStmts = new StringBuilder();
            for (String stmt : this.unparsableStmts) {
                sbStmts.append(stmt + "\n/\n");
            }
            System.out.println(sbStmts);
        }
    }

    @Test
    public void testUnparsables() throws IOException {
        processSqlFile("unparsables.sql");
    }

    @Test
    public void testEnrollProcessFiles() throws IOException {
        String projectPath = "C:\\data\\development\\intellijProjects\\enr-1.0-aggregate";
        subProject = "ks-enroll";
        module = subProject + "-sql";
        testProcessFiles(projectPath);
    }

    @Test
    public void testLumProcessFiles() throws IOException {
        String projectPath = "C:\\data\\development\\intellijProjects\\enr-1.0-aggregate";
        subProject = "ks-lum";
        module = subProject + "-sql";
        testProcessFiles(projectPath);
    }

    @Test
    public void testCoreProcessFiles() throws IOException {
        String projectPath = "C:\\data\\development\\intellijProjects\\enr-1.0-aggregate";
        subProject = "ks-core";
        module = subProject + "-sql";
        testProcessFiles(projectPath);
    }

    @Test
    public void testRiceProcessFiles() throws IOException {
        String projectPath = "C:\\data\\development\\intellijProjects\\enr-1.0-aggregate";
        subProject = "ks-core";
        module = "ks-rice-sql";
        testProcessFiles(projectPath);
    }


    private void testProcessFiles(String projectPath) throws IOException {
        String modulePath = projectPath + "\\" + subProject + "\\" + module;
        String resourceListingFile =  modulePath + "\\target\\classes\\META-INF\\org\\kuali\\student\\" + module + "\\oracle\\other.resources";


        BufferedReader br = new BufferedReader(new FileReader(resourceListingFile));
        String sqlFile;
        while ((sqlFile = br.readLine()) != null) {
            // use contents of resource file to get list of file names
            sqlFile = sqlFile.replace("classpath:", modulePath + "\\src\\main\\resources\\");
            processSqlFile(sqlFile);
        }
        br.close();
    }

    public void processSqlFile(String sqlFile) {
        System.out.println("parsing file: " + sqlFile);
        try {
            processSqlStatements(sqlFile);
        } catch (IOException e) {
            System.out.println ("**************************************\n" +
                                "*         ERROR READING FILE         *\n" +
                                "**************************************\n" +
                                "\n");
            e.printStackTrace();

        }
    }

    private String fileToString(String sqlFile) throws IOException {
        byte[] encoded;
        try {
            encoded = Files.readAllBytes(Paths.get(sqlFile));
        } catch (NoSuchFileException nsfe) {
            // check resources
            URL resource = Thread.currentThread().getContextClassLoader().getResource(sqlFile);
            if (resource == null) {
                throw new NoSuchFileException("File not found: " + sqlFile);
            }
            try {
                encoded = Files.readAllBytes(Paths.get(resource.toURI()));
            } catch (URISyntaxException urise) {
                throw new IOException(urise);
            }
        }
        return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(encoded)).toString();
    }

    // excepts fully qualified paths or
    private void processSqlStatements(String sqlFile) throws IOException {
        String sqlStatments = fileToString(sqlFile);
        String[] statements = splitStatements(sqlStatments);
        Map<DatabaseModule, List<String>> locationMap = new HashMap<DatabaseModule, List<String>>();
        System.out.println("num of statements in file: " + statements.length);
        for (String statement : statements) {
            Map<DatabaseModule, List> tableMap = new HashMap<DatabaseModule, List>();

            List<String> tableNames = null;
            try {
                tableNames = getTableNamesForStatement(statement);

                for (String table : tableNames) {
                    addItemToTableMap(tableMap, table);
                }

                Set<Map.Entry<DatabaseModule,List>> entries = tableMap.entrySet();
                if (entries.size() == 1) {
                    Map.Entry<DatabaseModule,List> entry = entries.iterator().next();
                    if (!tableTypeBelongs(entry.getKey())) {
                        if (entry.getKey() == DatabaseModule.EXCEPTION) {
                            List<String> tables = entry.getValue();
                            System.out.println("Exception mapping table(s) to module: " + tables);
                        } else {
                            System.out.println("Stmt moving to: " + entry.getKey().getEndsWith());
                        }
                    }
                } else {
                    System.out.println("Stmt contains more than one module reference");
                }

//                String report = getTableReport(tableMap);
//                if (report.length() > 0) {
//                    System.out.println(report);
//                }
            } catch (StandardException e) {
                System.out.println("Error parsing statement: " + statement);
                this.unparsableStmts.add(statement);
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


    private String getTableReport(Map<DatabaseModule, List> tableMap) {
        Set<Map.Entry<DatabaseModule,List>> entries = tableMap.entrySet();
        StringBuilder report = new StringBuilder();
        if (entries.size() == 1) {
            Map.Entry<DatabaseModule,List> entry = entries.iterator().next();
            if (!tableTypeBelongs(entry.getKey())) {
                List<String> tables = entry.getValue();
                report.append ("statement contains tables that belong in another module. " + tables.toString() + "\n");
            }
        } else if (entries.size() == 0 ) {
            report.append ("No table found in statement");
        } else {
            report.append("ERROR statement contains multiple table modules." + "\n");
            for (Map.Entry<DatabaseModule,List> entry : entries) {
                List<String> tables = tableMap.get(entry.getKey());
                report.append(entry.getKey().getLabel() + " " + tables.toString() + "\n");
            }

        }


        return report.toString();
    }

    private boolean tableTypeBelongs(DatabaseModule tableType) {
        if (module.endsWith(tableType.getEndsWith())) {
            return true;
        }
        return false;
    }


    private void addItemToTableMap(Map<DatabaseModule, List> tableReport, String table) {
        DatabaseModule category = getTableModule(table);


        List contents = tableReport.get(category);
        if (contents == null) {
            contents = new ArrayList<String>();
        }
        contents.add(table);
        tableReport.put(category,contents);
    }

    private DatabaseModule getTableModule(String table) {
        if (isRiceTable(table)) {
            return DatabaseModule.RICE;
        } else if (isCMTable(table)) {
            return DatabaseModule.KSCM;
        } else if (isEnrTable(table)) {
            return DatabaseModule.KSENR;
        } else if (isKSTable(table)) {
            return DatabaseModule.KSCORE;
        } else {
            return DatabaseModule.EXCEPTION;
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
