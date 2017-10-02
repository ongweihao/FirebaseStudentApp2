package sg.edu.rp.webservices.firebasestudentapp2;

/**
 * Created by 15039840 on 9/8/2017.
 */

import java.io.Serializable;

public class UserProfile implements Serializable {

    private String id;
    private String contactNo;
    private String hobbies;

    public UserProfile() {
    }

    public UserProfile(String contactNo, String hobbies) {
        this.contactNo = contactNo;
        this.hobbies = hobbies;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "id='" + id + '\'' +
                ", contactNo='" + contactNo + '\'' +
                ", hobbies='" + hobbies + '\'' +
                '}';
    }
}
