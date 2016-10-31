package com.fiuba.tallerii.lincedin.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.fiuba.tallerii.lincedin.R;
import com.fiuba.tallerii.lincedin.events.MessageEvent;
import com.fiuba.tallerii.lincedin.fragments.HTTPConfigurationDialogFragment;
import com.fiuba.tallerii.lincedin.utils.GooglePlayServicesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private ArrayAdapter mSkillsAdapter;
    private ArrayList<String> listSkills = new ArrayList<>();

    public ArrayList<String> getArrayList(){
        return listSkills;
    }
    public ArrayAdapter getArrayAdapter(){
        return mSkillsAdapter;
    }

    public void setArrayAdapter(ArrayAdapter adapter){
        mSkillsAdapter = adapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        EventBus.getDefault().register(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        setTabLayoutIcons(tabLayout);


        // TODO: 06/09/16 Only for testing purposes. Remove this request!
        // ------------------------------------------------------------->
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Testing...", Snackbar.LENGTH_LONG)
                        .setAction(
                                "Enviar request a AppServer",
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                            openHTTPConfigurationDialog();

                                    }
                                }
                        )
                        .show();
            }
        });
        // <-------------------------------------------------------------
    }

    @Override
    protected void onResume() {
        super.onResume();
        GooglePlayServicesUtils.checkGooglePlayServicesAvailability(this);
    }



    public void addskillsTolist(String string){
        //ListView listView = (ListView) findViewById(R.id.list_skills);
        //ArrayAdapter adapter = (ArrayAdapter) listView.getAdapter();
        //adapter.add("adeentroo...");
        //adapter.notifyDataSetChanged();
        mSkillsAdapter.add(string);
    }

    private void openHTTPConfigurationDialog() {
        DialogFragment httpDialog = new HTTPConfigurationDialogFragment();
        HTTPConfigurationDialogFragment http = (HTTPConfigurationDialogFragment) httpDialog;
        http.setAdapter(mSkillsAdapter);
        httpDialog.show(getSupportFragmentManager(), "HTTPConfigurationDialogFragment");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHTTPResponseReceived(MessageEvent event) {
        Toast.makeText(this, event.message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setTabLayoutIcons(TabLayout tabLayout) {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_account_circle);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_group);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_person_add);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_chat);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";


        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            //TODO: 6/10/16 List for skills... only testing, remove.
            // Instancia del ListView.
            ListView mSkillsList = (ListView) rootView.findViewById(R.id.list_skills);
            HomeActivity activity = (HomeActivity) getActivity();
            ArrayList<String> listSkills = activity.getArrayList();
            ArrayAdapter mSkillsAdapter;
            mSkillsAdapter = new ArrayAdapter<>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    listSkills);
            activity.setArrayAdapter(mSkillsAdapter);
            mSkillsList.setAdapter(mSkillsAdapter);

            return rootView;
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private static final int COUNT_SECTIONS = 4;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show total pages.
            return COUNT_SECTIONS;
        }

    }

}
