package skyward.pp.model;

/**
 * Created by Binal on 19-Oct-16.
 */
public class CategoryListClass {

    String CategoryValue,CategoryName;
    int id;

    public CategoryListClass(String categoryName, int id) {
        CategoryName = categoryName;
        this.id = id;
    }


    public String getCategoryValue() {
        return CategoryValue;
    }

    public void setCategoryValue(String categoryValue) {
        CategoryValue = categoryValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CategoryListClass(String categoryValue, String categoryName, int id) {
        CategoryValue = categoryValue;
        CategoryName = categoryName;
        this.id = id;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
}
