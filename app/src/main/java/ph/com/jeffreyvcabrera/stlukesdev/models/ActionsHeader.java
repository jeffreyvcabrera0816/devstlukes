package ph.com.jeffreyvcabrera.stlukesdev.models;

import java.util.ArrayList;

/**
 * Created by Jeffrey on 6/5/2017.
 */

public class ActionsHeader {

    private String name;
    private ArrayList<ActionsBody> actionsList = new ArrayList<ActionsBody>();;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<ActionsBody> getActionsList() {
        return actionsList;
    }
    public void setActionsList(ArrayList<ActionsBody> productList) {
        this.actionsList = actionsList;
    }

}