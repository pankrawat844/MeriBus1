package paztechnologies.com.meribus;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Admin on 5/3/2017.
 */

public class Booking_Tab extends Fragment
{
    TabLayout tabLayout;
    ViewPager viewPager;
    View_pager view_pager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.booking_tab,container,false);
        init(view);
        return view;
    }

    private void init(View v){

        tabLayout=(TabLayout)v.findViewById(R.id.tabLayout);
        viewPager=(ViewPager)v.findViewById(R.id.viewPager);
            view_pager= new View_pager(getChildFragmentManager());
            viewPager.setAdapter(view_pager);
        tabLayout.setupWithViewPager(viewPager);
                    }



    private class View_pager extends FragmentPagerAdapter{

        public View_pager(FragmentManager fm) {
            super(fm);


        }

        @Override
        public Fragment getItem(int i) {
            if(i==0){
                Main_page monthly= new Main_page();
                return monthly;
            }else{
                Book_Per_Day per_day= new Book_Per_Day();
                return per_day;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position==0){
                return "Monthly";
            }else {
                return "PerDay";
            }

        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
