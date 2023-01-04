package skyward.pp.holder;

import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by ANDROID 1 on 22-06-2017.
 */
public class EcommdashboardCategoryHolder {

    TextView CategoryName;
    RecyclerView productsrecycler;

    public EcommdashboardCategoryHolder(TextView categoryName, RecyclerView productsrecycler) {
        CategoryName = categoryName;
        this.productsrecycler = productsrecycler;
    }

    public TextView getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(TextView categoryName) {
        CategoryName = categoryName;
    }

    public RecyclerView getProductsrecycler() {
        return productsrecycler;
    }

    public void setProductsrecycler(RecyclerView productsrecycler) {
        this.productsrecycler = productsrecycler;
    }
}
