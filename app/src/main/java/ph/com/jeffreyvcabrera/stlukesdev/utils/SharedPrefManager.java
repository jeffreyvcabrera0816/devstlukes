package ph.com.jeffreyvcabrera.stlukesdev.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import ph.com.jeffreyvcabrera.stlukesdev.models.UserModel;

public class SharedPrefManager {

    Context c;
    public SharedPrefManager(Context c){
        this.c=c;
    }

    public void clearUser(){
        SharedPreferences s = c.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor x = s.edit();
        x.clear();
        x.commit();

    }

    public void saveUser(UserModel u){
        SharedPreferences s = c.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor x = s.edit();

        x.putInt("id", u.getId());
        x.putInt("duty", u.getDuty());
        x.putInt("role", u.getRole());
        x.putInt("gender", u.getGender());
        x.putString("email", u.getEmail());
        x.putString("firstname", u.getFirstname());
        x.putString("middlename", u.getMiddlename());
        x.putString("lastname", u.getLastname());
        x.putString("mobile", u.getMobile());
        x.putString("mac_address", u.getMac_address());
        x.putString("image", u.getImage());
        x.putString("md5_password", u.getMd5_password());
        x.putString("date_added", u.getDate_added());
        x.putString("last_login", u.getLast_login());
        x.commit();
        Log.e("pref saved!", "saved");
    }

    public UserModel getUser(){
       UserModel um = new UserModel();
        SharedPreferences s = c.getSharedPreferences("user", Context.MODE_PRIVATE);

        um.setId(s.getInt("id", 0));
        um.setDuty(s.getInt("duty", 0));
        um.setRole(s.getInt("role", 0));
        um.setGender(s.getInt("gender", 0));
        um.setEmail(s.getString("email", ""));
        um.setFirstname(s.getString("firstname", ""));
        um.setMiddlename(s.getString("middlename", ""));
        um.setLastname(s.getString("lastname", ""));
        um.setMobile(s.getString("mobile", ""));
        um.setImage(s.getString("image", ""));
        um.setMac_address(s.getString("mac_address", ""));
        um.setMd5_password(s.getString("md5_password", ""));
        um.setDate_added(s.getString("date_added", ""));
        um.setLast_login(s.getString("last_login", ""));

        return um;
    }

}
