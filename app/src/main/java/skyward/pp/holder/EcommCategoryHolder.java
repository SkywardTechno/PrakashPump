package skyward.pp.holder;

import android.widget.TextView;

/**
 * Created by ADNROID 2 on 15-11-2016.
 */
public class EcommCategoryHolder {

    TextView CategoryName;

    public EcommCategoryHolder(TextView categoryName) {
        CategoryName = categoryName;
    }

    public TextView getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(TextView categoryName) {
        CategoryName = categoryName;
    }
}
