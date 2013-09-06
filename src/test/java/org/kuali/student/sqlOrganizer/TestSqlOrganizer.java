package org.kuali.student.sqlOrganizer;



import com.akiban.sql.*;
import com.akiban.sql.parser.*;
import org.junit.*;

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
        String[] statements = sql.split("\n\\s*" + "/" + "\\s*" + "(\n|\\Z)");

        assertTrue(statements.length == 4);
        checkStatement(statements[0],6);
        checkStatement(statements[1],1);
        checkStatement(statements[2],1);
        checkStatement(statements[3],2);

    }

    private void checkStatement(String statement, int expectedNumTables) {
        SQLParser parser = new SQLParser();
        System.out.println(statement.trim());
        try{
            StatementNode stmt = parser.parseStatement(statement);
            NodeVisitor nodeVisitor = new NodeVisitor();
            List tableNames = nodeVisitor.getTableNames(stmt);
            assertTrue(tableNames.size() == expectedNumTables);
            System.out.println(tableNames.toString()+"\n");
            //stmt.treePrint();
        } catch (StandardException pe) {
            System.out.println(pe.getMessage());
        }
    }
}
