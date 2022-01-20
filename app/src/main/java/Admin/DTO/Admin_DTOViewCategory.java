package Admin.DTO;

import android.widget.Button;

public class Admin_DTOViewCategory {

    public Admin_DTOViewCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    String categoryName;
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}
