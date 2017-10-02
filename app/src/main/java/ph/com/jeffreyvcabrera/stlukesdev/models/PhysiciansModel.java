package ph.com.jeffreyvcabrera.stlukesdev.models;

/**
 * Created by Jeffrey on 9/25/2017.
 */

public class PhysiciansModel {

    Integer id, role, duty, gender;
    String firstname, middlename, lastname, email, image, md5_password, mac_address, mobile, date_added, last_login;

    public Integer getId() {
        return id;
    }

    public Integer getRole() {
        return role;
    }

    public Integer getGender() { return gender; }

    public String getFirstname() {
        return firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public String getLastname() {
        return lastname;
    }

    // setter

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
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
}
