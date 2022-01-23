package Admin.DTO;

public class Admin_DTOPostFeed2 {
    String ProfileImage;
    String FullName;
    String PhoneNumber;
    String Age;

    public Admin_DTOPostFeed2() {
        FullName = "";
        ProfileImage = "";
        PhoneNumber = "";
        Age = "";
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }
    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
}
