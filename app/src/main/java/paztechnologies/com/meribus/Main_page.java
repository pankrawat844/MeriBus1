package paztechnologies.com.meribus;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Admin on 2/21/2017.
 */

public class Main_page extends Fragment {
    Spinner current_location, des_location,select_route,select_end_time;
    android.widget.Spinner select_start_time;
    Button place_ride;
    String[] current_loc_arr = {"Delhi", "Gurgaon"};
    String startshiftresponse,endshiftresponse,route_response,pickup_response,seat_type="Both Pickup and Drop";
    List<String> route_id= new ArrayList<>();
    ProgressDialog progressDialog;
    RadioGroup radio_grp;
    RadioButton both,drop,pickup;
    TextView start_date,end_date;
    DatePickerDialog start_date_picker,end_date_picker;
    SimpleDateFormat dateFormatter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.main_page,container,false);
        progressDialog = new ProgressDialog(getActivity());
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        init(view);
        new Start_Shift_Time().execute();
        place_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ride_Detail ride_detail = new Ride_Detail();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, ride_detail).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        radio_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if(both.isChecked()){
                    seat_type="Both Pickup and Drop";
                    new Pickup_Point().execute();
                }else if(drop.isChecked()){
                    seat_type="Drop";
                    new Pickup_Point().execute();
                }else if(pickup.isChecked()){
                    seat_type="Pick Up";
                    new Pickup_Point().execute();
                }
            }
        });
//        select_start_time.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(Spinner parent, View view, int position, long id) {
//            parent.setSelection(position);
//            }
//        });
//        current_location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // findPlace(v);
//            }
//        });

        select_route.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                new Pickup_Point().execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_date_picker.show();
            }
        });
        Calendar newCalendar = Calendar.getInstance();
        start_date_picker= new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar=Calendar.getInstance();
                calendar.set(year,month,dayOfMonth);

                start_date.setText(dateFormatter.format(calendar.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        return view;
    }





    void init(View v){
        current_location = (Spinner) v.findViewById(R.id.current_location);
        des_location = (Spinner) v.findViewById(R.id.des_location);
        select_route=(Spinner)v.findViewById(R.id.select_route);
        select_start_time=(android.widget.Spinner) v.findViewById(R.id.start_time);

        select_end_time=(Spinner)v.findViewById(R.id.end_time);
        radio_grp=(RadioGroup)v.findViewById(R.id.radio_grp) ;
        both=(RadioButton)v.findViewById(R.id.both);
        pickup=(RadioButton)v.findViewById(R.id.pickup);
        drop=(RadioButton)v.findViewById(R.id.drop);
        start_date=(TextView)v.findViewById(R.id.start_date);
        end_date=(TextView)v.findViewById(R.id.end_date);
        place_ride = (Button) v.findViewById(R.id.submit);
        ArrayAdapter<CharSequence> current_adapter =  ArrayAdapter.createFromResource(getActivity(),R.array.arr,R.layout.support_simple_spinner_dropdown_item);
       // current_location.setAdapter(new NothingSelectedSpinnerAdapter(current_adapter, R.layout.nothing_selected_pickup, getActivity()));
       // des_location.setAdapter(new NothingSelectedSpinnerAdapter(current_adapter, R.layout.nothing_selected_drop_point, getActivity()));
    }





    private class Start_Shift_Time extends AsyncTask<String[], Void, String> {

        private Login activity;
        private String soapAction;
        private String methodName;
        private String paramsName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String[]... params) {
            startshifttime();
            return startshiftresponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.e("resulttttt",s);
            progressDialog.dismiss();

            try {
                List<String> time= new ArrayList<>();
         //   Toast.makeText(getActivity(),s,3).show();
                JSONArray jsonArray= new JSONArray(s);
                JSONArray arr=jsonArray.getJSONArray(0);
                for(int i=0;i<arr.length();i++){
                    JSONObject jsonObject= arr.getJSONObject(i);
                    time.add(jsonObject.getString("StartShiftTime"));
                }
                ArrayAdapter<String> start_adapter=new ArrayAdapter<String>(getActivity(), R.layout.nothing_selected_drop_point, time);
                start_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_start_time.setAdapter(new NothingSelectedSpinnerAdapter(start_adapter,R.layout.nothing_selected_shift_start,getContext()));
                new End_Shift_Time().execute();
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    private void startshifttime() {
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "_getShiftTime");



            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport =
                    new HttpTransportSE("http://sales.meribus.com/Service1.svc", 5000);
            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/IService1/_getShiftTime", envelope);
            SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();

            startshiftresponse = soapPrimitive.toString();
            Log.e("TAG", "Soap primitive1" + startshiftresponse);
        } catch (SocketTimeoutException e) {

        } catch (Exception e) {
            Log.e("TAG", "Soap Exception" + e.toString());
        }
    }



    private class End_Shift_Time extends AsyncTask<String[], Void, String> {

        private Login activity;
        private String soapAction;
        private String methodName;
        private String paramsName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String[]... params) {
            endshifttime();
            return endshiftresponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.e("resulttttt",s);
         //   progressDialog.dismiss();

            try {
                List<String> time= new ArrayList<>();
              //  Toast.makeText(getActivity(),s,3).show();
                JSONArray jsonArray= new JSONArray(s);
                JSONArray arr=jsonArray.getJSONArray(0);
                for(int i=0;i<arr.length();i++){
                    JSONObject jsonObject= arr.getJSONObject(i);
                    time.add(jsonObject.getString("EndShiftTime"));
                }
                ArrayAdapter<String> end_adapter=new ArrayAdapter<String>(getActivity(),R.layout.select_end_time, time);
                end_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                select_end_time.setAdapter(new NothingSelectedSpinnerAdapter(end_adapter,R.layout.nothing_selected_shift_end_time,getActivity()));
                new Select_Route().execute();
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    private void endshifttime() {
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "_getEndTime");



            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport =
                    new HttpTransportSE("http://sales.meribus.com/Service1.svc", 5000);
            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/IService1/_getEndTime", envelope);
            SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();

            endshiftresponse = soapPrimitive.toString();
            Log.e("TAG", "Soap primitive1" + endshiftresponse);
        } catch (SocketTimeoutException e) {

        } catch (Exception e) {
            Log.e("TAG", "Soap Exception" + e.toString());
        }
    }


    private class Select_Route extends AsyncTask<String[], Void, String> {

        private Login activity;
        private String soapAction;
        private String methodName;
        private String paramsName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String[]... params) {
            setSelect_route();
            return route_response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.e("resulttttt",s);
            progressDialog.dismiss();

            try {
                List<String> time= new ArrayList<>();
                HashMap<String,String> hash= new HashMap<>();
             //   Toast.makeText(getActivity(),s,3).show();
                JSONArray jsonArray= new JSONArray(s);
                JSONArray arr=jsonArray.getJSONArray(0);
                for(int i=0;i<arr.length();i++){
                    JSONObject jsonObject= arr.getJSONObject(i);
                    time.add(jsonObject.getString("RouteName"));
                    route_id.add(jsonObject.getString("routeId"));
                }
                ArrayAdapter<String> route_adapter=new ArrayAdapter<String>(getActivity(),R.layout.select_route_list, time);
                route_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                select_route.setAdapter(new NothingSelectedSpinnerAdapter(route_adapter,R.layout.nothing_selected_select_route,getContext()));

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    private void setSelect_route() {
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "getrouteList");



            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport =
                    new HttpTransportSE("http://sales.meribus.com/Service1.svc", 5000);
            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/IService1/getrouteList", envelope);
            SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();

            route_response = soapPrimitive.toString();
            Log.e("TAG", "Soap primitive1" + route_response);
        } catch (SocketTimeoutException e) {

        } catch (Exception e) {
            Log.e("TAG", "Soap Exception" + e.toString());
        }
    }


    private class Pickup_Point extends AsyncTask<String[], Void, String> {

        private Login activity;
        private String soapAction;
        private String methodName;
        private String paramsName;
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog= new ProgressDialog(getActivity());
            dialog.setMessage("Loading....");

            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String[]... params) {
            setpickup_point();
            return pickup_response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.e("resulttttt",s);
            dialog.dismiss();

            try {
                List<String> time= new ArrayList<>();
                //   Toast.makeText(getActivity(),s,3).show();
                JSONArray jsonArray= new JSONArray(s);
                JSONArray arr=jsonArray.getJSONArray(0);
                for(int i=0;i<arr.length();i++){
                    JSONObject jsonObject= arr.getJSONObject(i);
                    time.add(jsonObject.getString("PickUpName"));
                }
                ArrayAdapter<String> route_adapter=new ArrayAdapter<String>(getActivity(),R.layout.selected_pickup_location, time);
                route_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                current_location.setAdapter(new NothingSelectedSpinnerAdapter(route_adapter,R.layout.nothing_selected_pickup,getContext()));
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    private void setpickup_point() {
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "getPickUpList");

            PropertyInfo Orderid = new PropertyInfo();
            Orderid.setType(android.R.string.class);
            Orderid.setName("_routeId");
            Orderid.setValue(route_id.get(select_route.getSelectedItemPosition()-1));
            request.addProperty(Orderid);

            PropertyInfo aa123 = new PropertyInfo();
            aa123.setType(android.R.string.class);
            aa123.setName("Seattype");
            aa123.setValue(seat_type);
            request.addProperty(aa123);
            //Toast.makeText(getActivity(),route_id.get(select_route.getSelectedItemPosition())+" "+seat_type,3).show();

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport =
                    new HttpTransportSE("http://sales.meribus.com/Service1.svc", 5000);
            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/IService1/getPickUpList", envelope);
            SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();

            pickup_response = soapPrimitive.toString();
            Log.e("TAG", "Soap primitive1" + pickup_response);
        } catch (SocketTimeoutException e) {

        } catch (Exception e) {
            Log.e("TAG", "Soap Exception" + e.toString());
        }
    }


}
