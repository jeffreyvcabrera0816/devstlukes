package ph.com.jeffreyvcabrera.stlukesdev.models;

/**
 * Created by Jeffrey on 6/5/2017.
 */

public class ActionsBody {

    String content, date_posted, physician;

    public String getContent() {
        return content;
    }

    public String getDate_posted() {
        return date_posted;
    }

    public String getPhysician() {
        return physician;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate_posted(String date_posted) {
        this.date_posted = date_posted;
    }

    public void setPhysician(String physician) {
        this.physician = physician;
    }
}
