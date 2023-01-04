package skyward.pp.model;

/**
 * Created by Skyward_Mob1 on 08-02-2017.
 */
public class ProductSubCategoryClass {

    int id;
    String categoryname;

    public ProductSubCategoryClass(int id, String categoryname) {
        this.id = id;
        this.categoryname = categoryname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }
}
