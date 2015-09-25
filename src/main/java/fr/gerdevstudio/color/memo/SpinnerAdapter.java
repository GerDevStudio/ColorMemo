package fr.gerdevstudio.color.memo;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

// this spinner appears in AddTask activity
// it allows user to choose the priority of the task
public class SpinnerAdapter extends ArrayAdapter<Integer> {

    private ArrayList<String> mStringsArray; // to store strings of priorities names
    private Context mContext;

    public SpinnerAdapter(Context context,ArrayList<String> stringsArray,ArrayList<Integer> colorsArray){
        super(context,0,colorsArray);
        this.mStringsArray =stringsArray;
        this.mContext=context;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }


    // used in getDropDownView and getView methods
    public View getCustomView(int position, View convertView, ViewGroup parent) {
        // si la convertView est nulle,on inflate la vue avec le layout
        if (convertView==null){
            LayoutInflater inflater= (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.spinner_element,parent,false);
        }
        // Textview of the spinner is filled using the array of priorities
        TextView textView=(TextView)convertView.findViewById(R.id.spinner_text);
        textView.setText(mStringsArray.get(position));

        // the ImageView is generated depending on the color.
        // the lowest position in spinner is the lowest priority

        int color;
        int borderColor;

        ImageView imageView=(ImageView)convertView.findViewById(R.id.spinner_image);
        switch (position) {
            case 0 :
                borderColor= ContextCompat.getColor(mContext, R.color.low_priority_dark);
                color=ContextCompat.getColor(mContext,R.color.low_priority_light);
                break;
            case 1:
                borderColor=ContextCompat.getColor(mContext,R.color.medium_priority_dark);
                color=ContextCompat.getColor(mContext, R.color.medium_priority_light);
                break;
            case 2:
                borderColor=ContextCompat.getColor(mContext,R.color.high_priority_dark);
                color=ContextCompat.getColor(mContext,R.color.high_priority_light);
                break;
            case 3:
                borderColor=ContextCompat.getColor(mContext, R.color.very_high_priority_dark);
                color=ContextCompat.getColor(mContext,R.color.very_high_priority_light);
                break;
            default:borderColor= ContextCompat.getColor(mContext, R.color.low_priority_dark);
                color=ContextCompat.getColor(mContext,R.color.low_priority_light);
                break;
        }
        imageView.setImageDrawable(new ColorDrawable(color));
        GradientDrawable gradientDrawable = (GradientDrawable) imageView.getBackground();
        gradientDrawable.setStroke(2, borderColor);
        return convertView;
    }


}
