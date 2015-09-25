package fr.gerdevstudio.color.memo;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

// this class is the builder, used to create DialogBox, when user want to switch the style
public class ViewStyleDialogBuilder extends AlertDialog.Builder {
    private MainActivity mContext;

    public ViewStyleDialogBuilder(Context context){
        super(context);
        mContext=(MainActivity)context;
        this.setTitle(R.string.view_style_title);
        this.setItems(R.array.view_style_list, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: mContext.setViewStyle("liste");
                        break;
                    case 1: mContext.setViewStyle("liste_detaillee");
                        break;
                    case 2: mContext.setViewStyle("grille");
                        break;
                }
            }
        });
    }
}