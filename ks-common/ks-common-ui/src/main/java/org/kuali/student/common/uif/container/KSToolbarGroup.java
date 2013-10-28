package org.kuali.student.common.uif.container;

import org.kuali.rice.krad.uif.container.Group;

/**
 * Created with IntelliJ IDEA.
 * User: aliabad4
 * Date: 5/2/13
 * Time: 4:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class KSToolbarGroup extends Group {

    private static final long serialVersionUID = 7230145606607506418L;

    private boolean noLeftBorder;

    public boolean isNoLeftBorder() {
        return noLeftBorder;
    }

    public void setNoLeftBorder(boolean noLeftBorder) {
        this.noLeftBorder = noLeftBorder;
    }

    /**
     * @see org.kuali.rice.krad.uif.component.ComponentBase#copy()
     */
    @Override
    protected <T> void copyProperties(T component) {
        super.copyProperties(component);

        KSToolbarGroup ksToolbarGroupCopy = (KSToolbarGroup) component;

        ksToolbarGroupCopy.setNoLeftBorder(this.noLeftBorder);
    }
}
