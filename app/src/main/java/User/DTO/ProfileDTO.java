package User.DTO;

public class ProfileDTO {
    String FullName;
    String Age;
    String PhoneNumber;
    String ProfileImage;

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public ProfileDTO() {
        FullName = FullName;
        Age = Age;
        PhoneNumber = PhoneNumber;
        ProfileImage = ProfileImage;

    }
}
