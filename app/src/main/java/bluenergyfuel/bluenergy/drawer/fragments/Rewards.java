package bluenergyfuel.bluenergy.drawer.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bluenergyfuel.bluenergy.R;
import bluenergyfuel.bluenergy.extendables.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class Rewards extends BaseFragment {
    private  TabLayout tabLayout;
    private  ViewPager viewPager;
    public static int int_items = 2 ;
    public Rewards() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_rewards, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return view;
    }
    private class MyAdapter extends FragmentPagerAdapter {

        private MyAdapter(FragmentManager fm) {
            super(fm);
        }


        /**
         * Return fragment with respect to Position .
         */

        @Override
        public Fragment getItem(int position)
        {
            switch (position){
                case 0 : return new RewardsPoints();
                case 1 : return new RewardsItem();
            }
            return null;
        }

        @Override
        public int getCount() {

            return int_items;

        }
        /**
         * This method returns the title of the tab according to the position.
         */
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "Points";
                case 1 :
                    return "Items";
            }
            return null;
        }
    }

}
