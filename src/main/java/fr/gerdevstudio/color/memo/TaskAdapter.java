package fr.gerdevstudio.color.memo;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> mTasks;
    private MainActivity mContext;
    private IViewHolderClick mListener;


    // Description of view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnTouchListener {
        // each data item is just a string in this case
        public TextView title;
        public TextView description;
        public ImageView colorImage;
        public ImageView backgroundColorImage;
        public ImageButton imageButtonDone;
        public TextView creationDate;
        public Integer color1; // used to change color on touch
        public Integer color2; // same




        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            description = (TextView) v.findViewById(R.id.description);
            colorImage = (ImageView) v.findViewById(R.id.colorImage);
            imageButtonDone = (ImageButton) v.findViewById(R.id.imageButtonDone);
            backgroundColorImage = (ImageView) v.findViewById(R.id.backgroundColorImage);
            creationDate = (TextView) v.findViewById(R.id.creationDate);
            // click listener to done button, in order to delete task when complete.
            imageButtonDone.setOnClickListener(this);
            // touch listener to background image, in order to change color on touch, and launch activity
            // to modify the touched task
            backgroundColorImage.setOnTouchListener(this);
        }

        // onClick methods call interface defined in adapter.
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onImageButtonPressed((ImageButton) v, getAdapterPosition());
                Log.v("System.out","VUE CLICKEE" );
            }
        }
        // onTouch methods call interface defined in adapter.

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // onTouch only used on ImageView, so cast is OK.
            // ImageView is composed by Drawable(color), et d'un background (border).
            // so we have to change the drawable, not the background !!
            // colors are defined in the holder, and set during onBindViewHolder method.
            ImageView iv=(ImageView)v;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.v("System.out", "VUE TOUCHEE");
                    iv.setImageDrawable(new ColorDrawable(ColorManager.mixColors(this.color1, this.color2)));
                    iv.invalidate();
                    break;

                case MotionEvent.ACTION_CANCEL:
                    iv.setImageDrawable(new ColorDrawable(this.color2));
                    iv.invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    iv.setImageDrawable(new ColorDrawable(this.color2));
                    iv.invalidate();
                    if (mListener!=null){
                        mListener.onTaskPressed(v,getAdapterPosition());
                    }
                    break;
            }
            return true;
        }
    }

    // Constructor for adapter
    public TaskAdapter(MainActivity context,ArrayList<Task> tasks) {
        mTasks=tasks;
        mContext=context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        // layout depends on view style, variable is stocked in mainActivity.
        // it can have 3 values ( "grille", "list", "list_detailled")
        View v;
        switch (mContext.getViewStyle()){
            case "grille":
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_grid, parent, false);
                break;
            case "liste_detaillee":
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_detailled, parent, false);
                break;
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task, parent, false);
        }


        return new ViewHolder(v);

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Task tache=mTasks.get(position);
        holder.title.setText(tache.getTitle());
        holder.description.setText(tache.getDescription());
        // depending on priority, colors are stocked in the view Holder.

        switch (tache.getPriority()) {
            case 0 :
                holder.color1=ContextCompat.getColor(mContext, R.color.low_priority_dark);
                holder.color2=ContextCompat.getColor(mContext,R.color.low_priority_light);
                break;
            case 1:
                holder.color1=ContextCompat.getColor(mContext,R.color.medium_priority_dark);
                holder.color2=ContextCompat.getColor(mContext, R.color.medium_priority_light);
                break;
            case 2:
                holder.color1=ContextCompat.getColor(mContext,R.color.high_priority_dark);
                holder.color2=ContextCompat.getColor(mContext,R.color.high_priority_light);
                break;
            case 3:
                holder.color1=ContextCompat.getColor(mContext, R.color.very_high_priority_dark);
                holder.color2=ContextCompat.getColor(mContext,R.color.very_high_priority_light);
                break;}




        // Holder colors are set
        holder.backgroundColorImage.setImageDrawable(new ColorDrawable(holder.color2));
        holder.colorImage.setImageDrawable(new ColorDrawable(holder.color1));

        // colors of border and background are set
        GradientDrawable gradientDrawable = (GradientDrawable) holder.backgroundColorImage.getBackground();
        gradientDrawable.setStroke(2, holder.color1);

        // date is put in the holder, as a string.
        // it can have 2 format, depending on the day of the task was created
        // is the task is created today -> HH:MM, else DD:mm
        holder.creationDate.setText(DateManager.DateFormatted(tache.getCreationDate()));
        // button is colored too
        holder.imageButtonDone.setColorFilter(holder.color1);
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        // must handle the case wether the Tasks are null
        return (null==mTasks ? 0:mTasks.size());
    }

    // this interface will be used in mainactivity, to perform actions in adapter.
    public interface IViewHolderClick{
        void onImageButtonPressed(ImageButton iB,int position);
        void onTaskPressed(View v, int position);
    }

    public void setIViewHolderClick(IViewHolderClick listener){
        this.mListener=listener;
    }

}
