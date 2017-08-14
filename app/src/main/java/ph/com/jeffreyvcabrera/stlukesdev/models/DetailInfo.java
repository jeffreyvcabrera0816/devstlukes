package ph.com.jeffreyvcabrera.stlukesdev.models;

/**
 * Created by Jeffrey on 6/5/2017.
 */

public class DetailInfo {

    Integer id, change_dressing, priority, post_op, trach_care, status, gender;
    String firstname, middlename, lastname, age, room, date_admitted, date_released, diagnosis, image, physician;

    public Integer getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public String getAge() {
        return age;
    }

    public Integer getGender() {
        return gender;
    }

    public String getRoom() {
        return room;
    }

    public String getImage() {
        return image;
    }

    public String getPhysician() {
        return physician;
    }

    public String getDate_admitted() {
        return date_admitted;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getDate_released() {
        return date_released;
    }

    public Integer getChange_dressing() {
        return change_dressing;
    }

    public Integer getPriority() {
        return priority;
    }

    public Integer getPost_op() {
        return post_op;
    }

    public Integer getTrach_care() {
        return trach_care;
    }

    // setter

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPhysician(String physician) {
        this.physician = physician;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setDate_admitted(String date_admitted) {
        this.date_admitted = date_admitted;
    }

    public void setDate_released(String date_released) {
        this.date_released = date_released;
    }

    public void setChange_dressing(Integer change_dressing) {
        this.change_dressing = change_dressing;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public void setPost_op(Integer post_op) {
        this.post_op = post_op;
    }

    public void setTrach_care(Integer trach_care) {
        this.trach_care = trach_care;
    }
}
