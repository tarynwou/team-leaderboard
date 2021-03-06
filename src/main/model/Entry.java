package model;

import org.json.JSONObject;
import persistence.Writable;

// Represents an entry with an action type, comment, and teammate responsible
public class Entry implements Writable {
    private String actionType;
    private String comment;
    private String teammate;

    /*
     * REQUIRES: type is a real type defined in the system and person has a profile in the system
     * EFFECTS: builds an entry
     */
    public Entry(String type, String action, String person) {
        actionType = type;
        comment = action;
        teammate = person;
    }

    // FOR TESTING
    public String getActionType() {
        return actionType;
    }

    public String getComment() {
        return comment;
    }

    public String getTeammate() {
        return teammate;
    }

    // EFFECTS: returns string representation of this entry
    public String toString() {
        return actionType + ": " + comment + ": " + teammate;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("actionType", actionType);
        json.put("comment", comment);
        json.put("teammate", teammate);
        return json;
    }

}
