package fr.gerdevstudio.color.memo;

import android.graphics.Color;

/**
 * Created by ger on 23/09/2015.
 * Class used to manage color properties, used in a adapters for example.
 */
public class ColorManager {
    // mix two colors, and return one.
    public static int mixColors(int col1, int col2) {
        int mr1, mg1, mb1, mr2, mg2, mb2;

        mr1 = Color.red(col1);
        mg1 = Color.green(col1);
        mb1 = Color.blue(col1);

        mr2 = Color.red(col2);
        mg2 = Color.green(col2);
        mb2 = Color.blue(col2);

        int r3 = (mr1 + 2*mr2)/3;
        int g3 = (mg1 + 2*mg2)/3;
        int b3 = (mb1 + 2*mb2)/3;

        return Color.rgb(r3, g3, b3);
    }
}
