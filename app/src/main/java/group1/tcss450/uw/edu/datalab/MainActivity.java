package group1.tcss450.uw.edu.datalab;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.List;

import group1.tcss450.uw.edu.datalab.data.ColorDB;
import group1.tcss450.uw.edu.datalab.data.ColorEntry;

public class MainActivity extends AppCompatActivity implements ColorFragment.OnFragmentInteractionListener{

    private SharedPreferences mPrefs;
    ColorFragment mColorFragment;
    private ColorDB mCourseDB;
    public Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mPrefs = getSharedPreferences(getString(R.string.SHARED_PREFS), Context.MODE_PRIVATE);
        findViewById(R.id.content_main).
                setBackgroundColor(mPrefs.getInt(getString(R.string.COLOR), Color.RED));
        int pos = mPrefs.getInt(getString(R.string.POSITION), 0);

        if (savedInstanceState == null) {
            if (findViewById(R.id.content_main) != null) {
                mColorFragment = new ColorFragment();
                if (pos != 0) {
                    Bundle args = new Bundle();
                    args.putInt(getString(R.string.POSITION), pos);
                    mColorFragment.setArguments(args);
                }

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.content_main, mColorFragment)
                        .commit();
            }
        }
//        if (!mPrefs.contains(getString(R.string.SHARED_PREFS))) {
//            mMenu.findItem(R.id.file_menu_item).setVisible(true);
//        }
//        if (mCourseDB != null && mCourseDB.getColors().size() < 1){
//            mMenu.findItem(R.id.db_menu_item).setVisible(true);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        mMenu = menu;


        if (!mPrefs.contains(getString(R.string.SHARED_PREFS))) {
            mMenu.findItem(R.id.file_menu_item).setVisible(true);
        }
        //if (mCourseDB != null && mCourseDB.getColors().size() < 1){
            mMenu.findItem(R.id.db_menu_item).setVisible(true);
        //}



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        //noinspection SimplifiableIfStatement
        if (id == R.id.file_menu_item) {

            mColorFragment.getArguments().putInt(getString(R.string.POSITION),
                    mPrefs.getInt(getString(R.string.POSITION), 0));

            //mMenu.findItem(R.id.trashcan).setVisible(true);

            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_main, new FileFragment(),"fileFragment")
                    .addToBackStack(null);
            // Commit the transaction
            transaction.commit();
        } else if (id == R.id.db_menu_item) {
            if (mCourseDB == null) {
                mCourseDB = new ColorDB(this);
            }

            mColorFragment.getArguments().putInt(getString(R.string.POSITION),
                    mPrefs.getInt(getString(R.string.POSITION), 0));

            DBFragment dbf = new DBFragment();
            Bundle args = new Bundle();
            args.putSerializable(getString(R.string.DB_NAME),
                    (Serializable) mCourseDB.getColors());
            dbf.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_main, dbf, "dbFragment")
                    .addToBackStack(null);
                // Commit the transaction
            transaction.commit();
//        } else if (id == R.id.file_trashcan && 1 > 9) {
//            mPrefs.edit().clear().apply();
//            wipeFile();
//            Toast.makeText(this, "clear mPref", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.db_trashcan) {
            mCourseDB.dropTable();
            Toast.makeText(this, "clear DB", Toast.LENGTH_SHORT).show();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(int color, int pos) {
        Log.d("main","here");
        saveToSharedPrefs(color, pos);
        saveToFile(color, pos);
        saveToSqlite(color);
    }

    private void saveToSharedPrefs(int color, int pos) {
        mPrefs.edit().putInt(getString(R.string.COLOR), color).apply();
        mPrefs.edit().putInt(getString(R.string.POSITION), pos).apply();

    }

    private void saveToFile(int color, int pos) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                    openFileOutput(getString(R.string.COLOR)
                            , Context.MODE_APPEND));
            outputStreamWriter.append("color = r:" );
            outputStreamWriter.append(Color.red(color) + ", g:");
            outputStreamWriter.append(Color.green(color) + ", b:");
            outputStreamWriter.append(Color.blue(color) + ", a:");
            outputStreamWriter.append(Color.alpha(color) + "\n");
            outputStreamWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void wipeFile(){
        try{
            OutputStreamWriter opsw = new OutputStreamWriter(
                    openFileOutput(getString(R.string.COLOR)
                            , Context.MODE_PRIVATE));
            opsw.append("");
            opsw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveToSqlite(int color) {
        if (mCourseDB == null) {
            mCourseDB = new ColorDB(this);
        }
        mCourseDB.insertColor(System.currentTimeMillis(), color);
    }
}
