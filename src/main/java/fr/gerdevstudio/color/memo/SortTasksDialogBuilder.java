package fr.gerdevstudio.color.memo;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

// this builder shows when user want to change the sorting method of tasks
// by date of creation, or by color.
public class SortTasksDialogBuilder extends AlertDialog.Builder {
    private MainActivity mContext;

    public SortTasksDialogBuilder(Context context, ArrayList<Task> al){
        super(context);
        mContext=(MainActivity)context;
        this.setTitle(R.string.sort_tasks_title);
        this.setItems(R.array.sort_tasks_list, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: mContext.SortTasksList(0);
                        break;
                    case 1: mContext.SortTasksList(1);
                        break;
                }
            }
        });
    }
}