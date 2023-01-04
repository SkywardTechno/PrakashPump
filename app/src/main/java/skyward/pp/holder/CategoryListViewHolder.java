package skyward.pp.holder;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Binal on 19-Oct-16.
 */
public class CategoryListViewHolder {

    TextView txtCategoryName;
    TextView txtCategoryValue;
    ImageView icon;


    public ImageView getIcon() {
        return icon;
    }

    public void setIcon(ImageView icon) {
        this.icon = icon;
    }

    public CategoryListViewHolder(TextView txtCategoryName) {
        this.txtCategoryName = txtCategoryName;
    }

    public CategoryListViewHolder(TextView txtCategoryName, TextView txtCategoryValue, ImageView icon) {
        this.txtCategoryName = txtCategoryName;
        this.txtCategoryValue = txtCategoryValue;
        this.icon = icon;
    }

    public CategoryListViewHolder(TextView txtCategoryValue, ImageView icon) {
        this.txtCategoryValue = txtCategoryValue;
        this.icon = icon;
    }

    public TextView getTxtCategoryName() {
        return txtCategoryName;
    }

    public void setTxtCategoryName(TextView txtCategoryName) {
        this.txtCategoryName = txtCategoryName;
    }

    public TextView getTxtCategoryValue() {
        return txtCategoryValue;
    }

    public void setTxtCategoryValue(TextView txtCategoryValue) {
        this.txtCategoryValue = txtCategoryValue;
    }
}
