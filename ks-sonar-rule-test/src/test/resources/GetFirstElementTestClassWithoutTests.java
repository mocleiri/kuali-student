import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lsymms
 * Date: 7/10/13
 * Time: 11:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class GetFirstElementTestClassWithoutTests {


    public void testAcceptableUse2() {
        acceptableUse2();
    }

    public void acceptableUse2() {

        List<String> list = GetFirstElement.getList();

        list.add("b");
        list.add("c");
        list.add("a");

        // in a test class
        assert(list.get(0).equals("b"));
    }
}
