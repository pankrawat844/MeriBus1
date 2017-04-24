package paztechnologies.com.meribus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Admin on 2/27/2017.
 */

public class Payment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payment, container, false);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("CREDIT CARD"));
        tabLayout.addTab(tabLayout.newTab().setText("NET BANKING"));
        //    tabLayout.addTab(tabLayout.newTab().setText("WALLET"));
        tabLayout.addTab(tabLayout.newTab().setText("CASH ON DELIVERY"));

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);

        PagerAdapter1 adapter = new PagerAdapter1(getChildFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }


    public class PagerAdapter1 extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter1(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    Credit_Card_Fragment tab1 = new Credit_Card_Fragment();
                    return tab1;
                case 1:
                    Net_Banking tab2 = new Net_Banking();
                    return tab2;
                case 2:
                    COD_Fragment tab3 = new COD_Fragment();
                    return tab3;


                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}
