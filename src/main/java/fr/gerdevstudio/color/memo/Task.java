package fr.gerdevstudio.color.memo;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;



// This class contains information about "task to do".
public class Task implements Parcelable{
    // variable used to stock date format, in order to parcel the instances through Parcelable interface
    private static final SimpleDateFormat dateFormatParcel = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",Locale.ENGLISH);

    private String mTitle;
    private String mDescription;
    private int mPriority;
    private Date mCreationDate;


    public Task (String title,String description,int priority) {
        this.mTitle =title;
        this.mDescription=description;
        this.mPriority =priority;
        this.mCreationDate =new Date();
    }
    // getters and setters
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public Date getCreationDate(){
        return mCreationDate;
    }

    public int getPriority(){return mPriority;}

    public void setPriority(Integer priority){ this.mPriority =priority;}

    // this comparator is used to sort tasks by its date
    static class ComparatorByDate implements Comparator<Task> {
        @Override
        public int compare(Task t1, Task t2) {
            return (t1.getCreationDate().compareTo(t2.getCreationDate()));
        }
    }

    // this comparator is used to sort tasks by its priority
    static class ComparatorByPriority implements Comparator<Task> {
        @Override
        public int compare(Task t1, Task t2) {
            if (t1.getPriority()<t2.getPriority()){return 1;}
            if (t1.getPriority()>t2.getPriority()){return -1;}
            return 0;
        }
    }


    // Implementation of Parcelable interface to transmit Task through intents, bundles,etc...
    // Parcelling part
    public Task(Parcel in){
        String[] data = new String[4];

        in.readStringArray(data);
        this.mTitle = data[0];
        this.mDescription = data[1];
        this.mPriority = Integer.parseInt(data[2]);
        // dateFormatParcel is SingleDateFormat. its not a secure thread, so we must catch exception
        try
        {this.mCreationDate = dateFormatParcel.parse(data[3]);}
        catch( Exception e){
            Log.v("System.out", e.toString());
        }
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]
                {this.mTitle,
                this.mDescription,
                Integer.toString(this.mPriority),
                dateFormatParcel.format(this.mCreationDate)
                });
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
