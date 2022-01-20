package Admin.DTO;

public class Admin_DTOPostFeed {
    int User_image,Post_Image;

    String UserName,DateTime,Description;

    public Admin_DTOPostFeed(int user_image, int post_Image, String userName, String dateTime, String description) {
        User_image = user_image;
        Post_Image = post_Image;
        UserName = userName;
        DateTime = dateTime;
        Description = description;
    }


    public int getUser_image() {
        return User_image;
    }

    public void setUser_image(int user_image) {
        User_image = user_image;
    }

    public int getPost_Image() {
        return Post_Image;
    }

    public void setPost_Image(int post_Image) {
        Post_Image = post_Image;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }



}
