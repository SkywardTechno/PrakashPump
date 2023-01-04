package skyward.pp.model;

/**
 * Created by ADNROID 2 on 15-11-2016.
 */
public class EcommCategoryClass {

    String CategoryValue,CategoryName;
    int id;

    public EcommCategoryClass(String categoryName, int id) {
        CategoryName = categoryName;
        this.id = id;
    }


    public String getCategoryValue() {
        return CategoryValue;
    }

    public void setCategoryValue(String categoryValue) {
        CategoryValue = categoryValue;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
