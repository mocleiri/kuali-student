package org.kuali.student.sqlOrganizer;



import com.akiban.sql.*;
import com.akiban.sql.parser.*;
import org.junit.*;

import java.io.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: larry
 * Date: 8/30/13
 * Time: 7:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestSqlOrganizer {

    @Test
    public void testTablesNamesFinder()  {
        String sql = "SELECT * FROM MY_TABLE1, MY_TABLE2, (SELECT * FROM MY_TABLE3) AS TABLE_3" +
                " LEFT OUTER JOIN MY_TABLE4 ON TABLE_3.ID=MYTABLE4.ID2"+
                " WHERE ID = (SELECT MAX(ID) FROM MY_TABLE5) AND ID2 IN (SELECT * FROM MY_TABLE6)\n;\n\n" +
                "INSERT INTO KRMS_TABLE VALUES ('VAL_1', 'VAL_2', '', null)//\n\n" +
                "UPDATE KSEN_TABLE set COL_1=true;";

        String[] statements = sql.split("\n;\\w*\n|\n//\n");


        SQLParser parser = new SQLParser();
        for(String s : statements) {
            System.out.println(s);
            try {
                StatementNode stmt = parser.parseStatement(s);
                stmt.treePrint();
            } catch (StandardException pe) {
                System.out.println(pe.getMessage());
            }
        }
    }
}
