package ph.com.jeffreyvcabrera.stlukesdev.models;

/**
 * Created by Jeffrey on 7/31/2017.
 */

public class UserModel {

    Integer id, role, duty, gender;
    String firstname, middlename, lastname, email, image, md5_password, mac_address, mobile, date_added, last_login;
    boolean logged_in;
    // getter

    public Integer getId() {
        return id;
    }

    public Integer getRole() {
        return role;
    }

    public Integer getDuty() {
        return duty;
    }

    public Integer getGender() {
        return gender;
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

    public String getEmail() {
        return email;
    }

    public String getImage() {
        return image;
    }

    public String getMd5_password() {
        return md5_password;
    }

    public String getMac_address() {
        return mac_address;
    }

    public String getMobile() {
        return mobile;
    }

    public String getDate_added() {
        return date_added;
    }

    public String getLast_login() {
        return last_login;
    }

    public boolean isLogged_in() {
        return logged_in;
    }

    // setter


    public void setId(Integer id) {
        this.id = id;
    }

    public void setDuty(Integer duty) {
        this.duty = duty;
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

    public void setImage(String image) {
        this.image = image;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMac_address(String mac_address) {
        this.mac_address = mac_address;
    }

    public void setMd5_password(String md5_password) {
        this.md5_password = md5_password;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public void setLogged_in(boolean logged_in) {
        this.logged_in = logged_in;
    }


}
