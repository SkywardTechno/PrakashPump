package skyward.pp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import skyward.pp.model.CategoryListClass;
import skyward.pp.holder.CategoryListViewHolder;
import skyward.pp.ColorGenerator;
import skyward.pp.R;
import skyward.pp.TextDrawable;

/**
 * Created by Binal on 19-Oct-16.
 */
public class CategoryListAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;

    private ArrayList<CategoryListClass> arraylist;


    public CategoryListAdapter(Context mContext, ArrayList<CategoryListClass> arraylist) {
        this.mContext = mContext;
        this.arraylist = arraylist;
        inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return arraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView name,value;
        ImageView icon;
        CategoryListViewHolder item;
        if (convertView==null)
        {

            convertView=inflater.inflate(R.layout.category_listitem,null);
           // name=(TextView)convertView.findViewById(R.id.txtcategorytype);
            value=(TextView)convertView.findViewById(R.id.txt_categoryvalue);
            icon = (ImageView) convertView.findViewById(R.id.ivcategory);
            item=new CategoryListViewHolder(value,icon);
            convertView.setTag(item);
        }
        else
        {
            item=(CategoryListViewHolder)convertView.getTag();
        }
        item.getTxtCategoryValue().setText(arraylist.get(position).getCategoryValue());
      //  item.getTxtCategoryName().setText(arraylist.get(position).getCategoryName());

        String firstLetter = String.valueOf(arraylist.get(position).getCategoryName().toUpperCase().charAt(0));

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color = generator.getColor(getItem(position));
        //int color = generator.getRandomColor();

        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter, color); // radius in px

        item.getIcon().setImageDrawable(drawable);
        return convertView;

    }
}
