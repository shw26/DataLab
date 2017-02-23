package group1.tcss450.uw.edu.datalab;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


/**
 * A simple {@link Fragment} subclass.
 */
public class FileFragment extends Fragment {

    private SharedPreferences mPrefs;
    private boolean clear = true;
    public FileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        clear = false;

        mPrefs = getActivity().getSharedPreferences(getString(R.string.SHARED_PREFS), Context.MODE_PRIVATE);

        LinearLayout view = (LinearLayout) inflater.inflate(
                R.layout.fragment_file, container, false);
        try {
            InputStream inputStream = getActivity().openFileInput(
                    getString(R.string.COLOR));
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                while ((receiveString = bufferedReader.readLine()) != null) {
                    TextView tv = new TextView(getActivity());
                    tv.setText(receiveString);
                    view.addView(tv);
                }


                inputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem trashcan = menu.findItem(R.id.file_trashcan);
        trashcan.setVisible(!clear);
        trashcan = menu.findItem(R.id.db_trashcan);
        trashcan.setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.file_trashcan) {
            mPrefs.edit().clear().apply();
            wipeFile();
            Toast.makeText(getActivity(), "clear mPref", Toast.LENGTH_SHORT).show();
        }

        LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.file_fragment);
        ll.removeAllViews();
        clear = true;
        return super.onOptionsItemSelected(item);
    }

    private void wipeFile(){
        try{
            OutputStreamWriter opsw = new OutputStreamWriter(
                    getActivity().openFileOutput(getString(R.string.COLOR)
                            , Context.MODE_PRIVATE));
            opsw.append("");
            opsw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }
}
