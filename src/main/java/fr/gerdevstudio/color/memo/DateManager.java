package fr.gerdevstudio.color.memo;

import java.text.SimpleDateFormat;
import java.util.Date;


// Class used to manage dates
public class DateManager {

    // SimpleDateFormat can be used to control the date/time display format:
    //   E (day of week): 3E or fewer (in text xxx), >3E (in full text)
    //   M (month): M (in number), MM (in number with leading zero)
    //              3M: (in text xxx), >3M: (in full text full)
    //   h (hour): h, hh (with leading zero)
    //   m (minute)
    //   s (second)
    //   a (AM/PM)
    //   H (hour in 0 to 23)
    //   z (time zone)

    // Used to know if date is today date
    public static boolean IsToday(Date date){

        Date todayDate=new Date();
        // using SimpleDateFormat to compare dates through easy format
        SimpleDateFormat dateFormatToCompareDay = new SimpleDateFormat("ddMMyyyy");

        // formating both dates
        String todayDateFormatted=dateFormatToCompareDay.format(todayDate);
        String dateFormatted=dateFormatToCompareDay.format(date);

        // comparing
        return (dateFormatted.equals(todayDateFormatted));
    }

    // returns hour is date is today, or date if date is before yesterday
    public static SimpleDateFormat DateFormat(Date date){
        if (IsToday(date)){
            return new SimpleDateFormat("HH:mm");
        }
        else {
            return new SimpleDateFormat("dd//MM");
        }
    }

    public static String DateFormatted(Date date){
        SimpleDateFormat dateFormat=DateFormat(date);
        return dateFormat.format(date);
    }
}
