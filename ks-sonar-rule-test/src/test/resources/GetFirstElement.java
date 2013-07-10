import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lsymms
 * Date: 7/9/13
 * Time: 3:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class GetFirstElement {

    public static List<String> getList() {
        List<String> list = new ArrayList<String>();
        list.add("b");
        list.add("c");
        list.add("a");

        return list;
    }

    // This one will be caught by sonar but would need to be resolved manually
    public void acceptableUse1() {
        List<String> list = getList();

        Collections.sort(list);

        // ordered
        if(list.size() == 1) {
            String firstValue = list.get(0);
        }
    }

    public String unacceptableUse() {
        List<String> list = getList();

        // if the order or content of the list changes we could run into problems
        return list.get(0);
    }

}


