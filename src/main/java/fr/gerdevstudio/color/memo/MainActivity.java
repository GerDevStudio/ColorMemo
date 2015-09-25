package fr.gerdevstudio.color.memo;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

import fr.gerdevstudio.color.memo.R;


public class MainActivity extends AppCompatActivity {

    // keys used by shared preferences
    private static final String KEY_LISTTASKSTODO = "fr.gerdevstudio.colormemo.KEY_LISTTASKSTODO";
    private static final String KEY_VIEWSTYLE = "fr.gerdevstudio.colormemo.KEY_VIEWSTYLE";

    // keys used for intent
    public final static String EXTRA_TASK ="fr.gerdevstudio.colormemo.EXTRA_TASK";
    public final static String EXTRA_TASK_INITIAL_POS="fr.gerdevstudio.colormemo.EXTRA_TASK_POS";

    // variable to choose view style (list, detailled list or grid )
    // -> 3 values possible "liste","liste_detaillee","grille".
    private String mViewStyle;

    // tasks to do
    private static ArrayList<Task> mListTasksToDo =new ArrayList<>();

    private Context context=this;

    // method that launches the activity to create activity
    public void OpenAddTaskActivity(){
        Intent intent=new Intent(this,AddTaskActivity.class);
        startActivity(intent);
    }

    // OverLoad of the methods, because can be called since a View ( button ) in UI.
    public void OpenAddTaskActivity(View view)
    {OpenAddTaskActivity();}

    // OverLoad to launch AddTaskActivity to modify an existing Task.
    public void OpenAddTaskActivity(Task task, Integer position){
        Intent intent=new Intent(this,AddTaskActivity.class);
        intent.putExtra(EXTRA_TASK,task);
        intent.putExtra(EXTRA_TASK_INITIAL_POS,position);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Show menu icon
        final ActionBar ab = getSupportActionBar();

        try {ab.setHomeAsUpIndicator(R.drawable.ic_launcher);
            ab.setDisplayHomeAsUpEnabled(true);}
        catch (Exception e){
            Log.v("MainActivity",e.toString());}

        // loading shared prefs
        SharedPreferences sharedPref=getPreferences(MODE_PRIVATE);
        //getting json object in shared preferences, that contains the list of tasks
        // checking if this list exists before getting it
        if (sharedPref.getString(KEY_LISTTASKSTODO,null)!=null) {
            //getting the Json Object
            Type listType = new TypeToken<ArrayList<Task>>() {}.getType();
            String json = sharedPref.getString(KEY_LISTTASKSTODO, "");
            mListTasksToDo= new Gson().fromJson(json, listType);
        }
        mViewStyle =sharedPref.getString(KEY_VIEWSTYLE,"liste");


        // if intent contains EXTRA_TASK, it means that a new task need to be added
        Intent intent=getIntent();
        if (intent.getParcelableExtra(AddTaskActivity.EXTRA_TASK)!=null) {
            mListTasksToDo.add((Task)intent.getParcelableExtra(AddTaskActivity.EXTRA_TASK));
            // not forget to remove the intent !
            intent.removeExtra(AddTaskActivity.EXTRA_TASK);
        }
        // if key EXTRA_TASK_INITIAL_POS is not default value,
        // it means that the task is not a new one. so we remove the old element, the new element
        // has been added in above code.
        Integer positionTaskModified=intent.getIntExtra(EXTRA_TASK_INITIAL_POS, -1);
        if (positionTaskModified!=-1){
            mListTasksToDo.remove(mListTasksToDo.get(positionTaskModified));
            intent.removeExtra(EXTRA_TASK_INITIAL_POS);
        }

        // creating recycler view that contains elements
        SetupRecyclerView();


    }


    @Override
    public void onPause(){
        super.onPause();
        // accessing shared prefs
        SharedPreferences prefs=getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edit=prefs.edit();
        //if the list of taks is non null, we save it.
        if (mListTasksToDo!=null){
            Gson gson = new Gson();
            String json = gson.toJson(mListTasksToDo);
            edit.putString(KEY_LISTTASKSTODO, json);
        }
        edit.putString(KEY_VIEWSTYLE, mViewStyle);
        edit.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent(this,SettingsActivity.class);

            // if API>=11, we open directly general prefs fragment.
            if (android.os.Build.VERSION.SDK_INT>=11){
                intent.putExtra( PreferenceActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.GeneralPreferenceFragment.class.getName() );
                intent.putExtra( PreferenceActivity.EXTRA_NO_HEADERS, true );}
            startActivity(intent);
            return true;
        }

        if (id == R.id.change_view) {
            ViewStyleDialogBuilder builder=new ViewStyleDialogBuilder(context);
            builder.create();
            builder.show();

            return true;
        }

        if (id == R.id.sort_tasks) {
            SortTasksDialogBuilder builder=new SortTasksDialogBuilder(context,mListTasksToDo);
            builder.create();
            builder.show();

            return true;
        }

        if (id == R.id.new_task) {
            OpenAddTaskActivity();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void setViewStyle(String string){
        mViewStyle =string;
        SetupRecyclerView();
    }

    public String getViewStyle(){
        return mViewStyle;
    }

    public void SetupRecyclerView(){
        // getting recyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_tasks);
        RecyclerView.LayoutManager layoutManager;
        // getting the right LayoutManager
        if (mViewStyle.equals("grille")){
            Display display = getWindowManager().getDefaultDisplay();
            DisplayMetrics outMetrics = new DisplayMetrics();
            display.getMetrics(outMetrics);
            float dpWidth  = outMetrics.widthPixels;
            int columns = Math.round(dpWidth/getResources().getDimension(R.dimen.grid_element_size));
            layoutManager = new GridLayoutManager(this,columns);
        }
        else
        {
            layoutManager = new LinearLayoutManager(this);
        }
        recyclerView.setLayoutManager(layoutManager);

        // Setting default animation when item of recyclerview is deleted
        RecyclerView.ItemAnimator animator=new DefaultItemAnimator();
        animator.setRemoveDuration(500);
        recyclerView.setItemAnimator(animator);

        // Setting adapter
        final TaskAdapter mAdapter = new TaskAdapter(this,mListTasksToDo);
        // Specifying adapter interface, to handle click on recyclerview elements
        mAdapter.setIViewHolderClick(
                new TaskAdapter.IViewHolderClick() {
                    @Override
                    public void onImageButtonPressed(ImageButton iB, final int position) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle(mListTasksToDo.get(position).getTitle());
                        builder.setMessage(getString(R.string.message_tache_effectuee));
                        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mListTasksToDo.remove(position);
                                mAdapter.notifyItemRemoved(position);
                            }
                        });
                        builder.setNegativeButton(getString(R.string.no), null);
                        builder.show();
                    }

                    @Override
                    public void onTaskPressed(View v, int position) {
                        OpenAddTaskActivity(mListTasksToDo.get(position), position);
                    }
                }


        );

        recyclerView.setAdapter(mAdapter);


    }
    // Allow to sort differents tasks
    // arg int = 1 -> by date
    // arg int = 2 -> by priority

    public void SortTasksList(Integer i){
        if (i==0){
            Collections.sort(mListTasksToDo,new Task.ComparatorByDate());
            SetupRecyclerView();}
        if (i==1){
            Collections.sort(mListTasksToDo,new Task.ComparatorByPriority());
            SetupRecyclerView();
        }
    }
}
