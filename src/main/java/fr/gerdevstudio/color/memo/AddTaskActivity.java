package fr.gerdevstudio.color.memo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import fr.gerdevstudio.color.memo.R;


public class AddTaskActivity extends Activity implements AdapterView.OnItemSelectedListener {

    // keys to store sharef prefs
    public final static String EXTRA_TASK ="fr.gerdevstudio.colormemo.EXTRA_TASK";
    public final static String EXTRA_TASK_INITIAL_POS="fr.gerdevstudio.colormemo.EXTRA_TASK_POS";

    private Integer mPriority;
    // store priority names and colors
    private ArrayList<String> mPriorityLabels =new ArrayList<>();
    private ArrayList<Integer> mPriorityColors =new ArrayList<>();
    private EditText mEditTextTask ;
    private EditText mEditTextTitre;
    // to copy description to title, while title unmodified
    private boolean mStopAutoWrite =false;
    private Spinner mSpinner;
    // to store original task position in tasks lists, stored in MainActivity
    private Integer mTaskModifiedPosition=-1;


    // to add a new task
    public void AddTask(View view){
        // task title and description from edittext boxes
        String tache=mEditTextTask.getText().toString();
        String titre= mEditTextTitre.getText().toString();

        // checking if title and description are empty to generate toast message
        if (tache.equals("")&&titre.equals("")){
            Toast.makeText(this,getString(R.string.empty_task_toast_message),Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent=new Intent(this,MainActivity.class);


        Task task=new Task(titre,tache,mPriority);
        intent.putExtra(EXTRA_TASK, task);

        // if task is a modified task, adding its original position to the intent
        if (mTaskModifiedPosition!=-1) {
            intent.putExtra(EXTRA_TASK_INITIAL_POS,mTaskModifiedPosition);
        }

        Toast.makeText(this,getString(R.string.new_task_toast_message), Toast.LENGTH_SHORT).show();
        // flag to clear the stack of mainActivity, and avoid problem while using "backbutton" in mainActivity
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mEditTextTask = (EditText)findViewById(R.id.task);
        mEditTextTitre = (EditText)findViewById(R.id.title);

        // getting priorities labels
        String[] tmpPriorityStringArray = getResources().getStringArray(R.array.priorities);
        for(String str:tmpPriorityStringArray)
        {
            mPriorityLabels.add(str);
        }

        // getting priorities colors

        mPriorityColors.add(ContextCompat.getColor(this, R.color.low_priority_dark));
        mPriorityColors.add(ContextCompat.getColor(this, R.color.medium_priority_dark));
        mPriorityColors.add(ContextCompat.getColor(this, R.color.high_priority_dark));
        mPriorityColors.add(ContextCompat.getColor(this, R.color.very_high_priority_dark));

        // initialize the spinner to choose priority
        InitializeSpinner();

        // if title is focused, autowrite from description to title is stopped
        View.OnFocusChangeListener titleFocusListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mStopAutoWrite =true;

                }
            }};

        mEditTextTitre.setOnFocusChangeListener(titleFocusListener);


        // Initializing values, when modifying a task from MainActivity call
        Intent intent=getIntent();
        Task task=intent.getParcelableExtra(EXTRA_TASK);
        if (task!=null) {
            mEditTextTitre.setText(task.getTitle());
            mEditTextTask.setText(task.getDescription());
            mSpinner.setSelection(task.getPriority());
            mTaskModifiedPosition=intent.getIntExtra(EXTRA_TASK_INITIAL_POS,-1);

            // remove extra keys that has been used
            intent.removeExtra(EXTRA_TASK);
            intent.removeExtra(EXTRA_TASK_INITIAL_POS);

            // Disabling auto copy from description to title, if they are different
            if (!task.getTitle().equals(task.getDescription())) {
                mStopAutoWrite = true;
            }
        }


        // to copy description to title automatically
        TextWatcher textWatcher=new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }
            // if auto copy enable, copying description to title, removing lines breaks.
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mStopAutoWrite) {
                    mEditTextTitre.setText(mEditTextTask
                            .getText()
                            .toString()
                            .replaceAll("\n", " "));
                }
            }

        };
        mEditTextTask.addTextChangedListener(textWatcher);

        // placing input cursor to the end of input. So we can write to the end of the description directly.
        mEditTextTask.setSelection(mEditTextTask.getText().length());    }




    // Initialises the spinner of priorities
    public void InitializeSpinner(){
        mSpinner=(Spinner)findViewById(R.id.spinner);
        // the adapter is a custom adapter
        SpinnerAdapter adapter=new SpinnerAdapter(this, mPriorityLabels, mPriorityColors);
        mSpinner.setAdapter(adapter);

        // getting default priority from shared preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultPriorityValueString = sharedPref.getString("priority", getString(R.string.pref_defaut_priority_value));
        Integer defaultPriorityValue = Integer.parseInt(defaultPriorityValueString);

        // getting string resources linked to priorities
        String[] priorityStringArray = getResources().getStringArray(R.array.pref_priority_list_values);

        // setting spinner default selection according to shared preferences
        for(String str:priorityStringArray){

            int priorityValue = Integer.parseInt(str);

            if (defaultPriorityValue == priorityValue){
                mSpinner.setSelection(priorityValue);            }
        }

        // adding a listener to store user choice
        mSpinner.setOnItemSelectedListener(this);
    }

    // on implémente l'interface de ArrayAdapter.OnSelectedItemListener
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        mPriority=pos;
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_task, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
