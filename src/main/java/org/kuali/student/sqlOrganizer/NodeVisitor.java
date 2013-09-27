package org.kuali.student.sqlOrganizer;

import com.akiban.sql.StandardException;
import com.akiban.sql.parser.DMLModStatementNode;
import com.akiban.sql.parser.FromTable;
import com.akiban.sql.parser.InsertNode;
import com.akiban.sql.parser.Visitable;
import com.akiban.sql.parser.Visitor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lsymms
 * Date: 9/6/13
 * Time: 11:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class NodeVisitor implements Visitor {
    private List<String> tableNames;

    public List<String> getTableNames(Visitable node) throws StandardException {
        tableNames = new ArrayList<String>();
        node.accept(this);
        return tableNames;
    }

    public Visitable visit(FromTable fromTable) throws StandardException {
        if (fromTable.getOrigTableName() != null) {
            tableNames.add(fromTable.getOrigTableName().getTableName().toUpperCase());
        }
        return fromTable;
    }

    // simple insert does not have have FromTable element
    public Visitable visit(InsertNode insertNode) {
        if (insertNode.getTargetTableName() != null) {
            tableNames.add(insertNode.getTargetTableName().getTableName().toUpperCase());
        }
        return insertNode;
    }

    private Method getPolymorphicMethod(Visitable node) {
        Class cl = node.getClass();  // the bottom-most class
        // Check through superclasses for matching method
        while(!cl.equals(Object.class)) {
            try {
                return this.getClass().getDeclaredMethod("visit", new Class[] { cl });
            } catch(NoSuchMethodException ex) {
                cl = cl.getSuperclass();
            }
        }
        // Check through interfaces for matching method
        Class[] interfaces = node.getClass().getInterfaces();
        for (int i=0; i<interfaces.length; i++) {
            try {
                return this.getClass().getDeclaredMethod("visit", new Class[] { interfaces[i] });
            } catch(NoSuchMethodException ex) {
            }
        }
        return null;
    }

    @Override
    public Visitable visit(Visitable node) throws StandardException {
        Method downPolymorphic = getPolymorphicMethod(node);
        if (downPolymorphic != null) {
            try {
                downPolymorphic.invoke(this, new Object[] {node});
            } catch (IllegalAccessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InvocationTargetException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        return node;
    }

    @Override
    public boolean visitChildrenFirst(Visitable node) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean stopTraversal() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean skipChildren(Visitable node) throws StandardException {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
