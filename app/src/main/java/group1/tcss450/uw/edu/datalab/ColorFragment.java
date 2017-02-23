package group1.tcss450.uw.edu.datalab;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ColorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ColorFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private OnFragmentInteractionListener mListener;
    Spinner spinner;

    public ColorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem trashcan = menu.findItem(R.id.file_trashcan);
        trashcan.setVisible(false);
        trashcan = menu.findItem(R.id.db_trashcan);
        trashcan.setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_color, container, false);
        spinner = (Spinner) v.findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.auto_complete_colors, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int color, int position) {
        if (mListener != null) {
            mListener.onFragmentInteraction(color, position);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        int arg;
        if (getArguments() != null){

             arg = getArguments().getInt(getString(R.string.POSITION));
        }else {
            arg = 0;
        }
        spinner.setSelection(arg);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String color = (String) parent.getAdapter().getItem(position);
        Toast.makeText(getActivity(),
                "The color is " + color,
                Toast.LENGTH_SHORT).show();

        int[] colorArr = this.getResources().getIntArray(R.array.colorArray);

        getActivity().findViewById(R.id.content_main).setBackgroundColor(colorArr[position]);
        mListener.onFragmentInteraction(colorArr[position], position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(int color, int position);
    }
}
