package paztechnologies.com.meribus;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Book_Per_Day extends Fragment {

    ArrayAdapter<CharSequence> shift_start_time;
    Spinner shift_start_time_spinner, shift_end_time_spinner, select_route_spinner, pickup_spinner, drop_spinner;
    private String[] start_time_arr={"10:00AM","11:00AM","8:00AM","8:30AM","9:00AM"};
    private String[] end_time_arr={"5:00PM","6:00PM","7:00PM","8:00PM"};
    private String[] select_route_arr={"Anand Vihar to Cyber City GGN","Avantika to Cyber City GGN","Badarpur to Cyber  city GGN","Burari to Cyber City GGN","Dilshad Garden to Cyber City GGN","Indirapuram to Cyber City "};
    private String[] pickup_arr={"Cyber City Gurgaon","Shankar Chowk/cyber city","Park centra","Signature tower","Iffco chowk","Airtel footover bridge"};
    private String[] drop_arr={"Cyber City Gurgaon","Shankar Chowk/cyber city","Park centra","Signature tower","Iffco chowk","Airtel footover bridge"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.activity_book__monthly,container,false);
        init(v);
        return v;
    }

    void init(View v){
        shift_start_time_spinner=(Spinner)v.findViewById(R.id.shift_time);
        shift_end_time_spinner=(Spinner)v.findViewById(R.id.shift_endtime);
        select_route_spinner=(Spinner)v.findViewById(R.id.select_route);
        pickup_spinner=(Spinner)v.findViewById(R.id.select_pickup);
        drop_spinner=(Spinner)v.findViewById(R.id.select_drop);
        shift_start_time= new ArrayAdapter<CharSequence>(getActivity(),android.R.layout.simple_list_item_1,start_time_arr);
        ArrayAdapter<String> shift_end_adapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,end_time_arr);
       ArrayAdapter<String> select_route_adapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,select_route_arr);
       ArrayAdapter<String> picup_adapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,pickup_arr);
        ArrayAdapter<String> drop_adapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,drop_arr);

        // shift_start_time= ArrayAdapter.createFromResource(getActivity(),start_time_arr,android.R.layout.simple_spinner_item);
        shift_start_time.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shift_start_time_spinner.setAdapter(new NothingSelectedSpinnerAdapter(shift_start_time,R.layout.nothing_selected_shift_start,getActivity()));
        shift_end_time_spinner.setAdapter(new NothingSelectedSpinnerAdapter(shift_end_adapter,R.layout.nothing_selected_shift_end_time,getActivity()));
        select_route_spinner.setAdapter(new NothingSelectedSpinnerAdapter(select_route_adapter,R.layout.nothing_selected_select_route,getActivity()));
        pickup_spinner.setAdapter(new NothingSelectedSpinnerAdapter(picup_adapter,R.layout.nothing_selected_pickup,getActivity()));
        drop_spinner.setAdapter(new NothingSelectedSpinnerAdapter(drop_adapter,R.layout.nothing_selected_drop_point,getActivity()));
    }
}
