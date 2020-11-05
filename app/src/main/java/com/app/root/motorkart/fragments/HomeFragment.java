package com.app.root.motorkart.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.root.motorkart.R;
import com.app.root.motorkart.adapter.StoreAdapter;
import com.app.root.motorkart.adapter.TopSlider;
import com.app.root.motorkart.modelclass.StoreModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ViewPager vw_slider;
    List<String> imageslist = new ArrayList<>();
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    TextView tv_car_rgno, tv_bike_rgno, tv_car_prsv, tv_bike_prsv;
    CircleIndicator indicator;
    private OnFragmentInteractionListener mListener;
    LinearLayout ll_partstore, ll_servicecntr, ll_alignmentcntr, ll_tyrestore, ll_washingpts, ll_engineoil,
            ll_tools, ll_bikecar, ll_batteries;
    SharedPreferences sp_login;
    String cid;
    SharedPreferences.Editor ed_login;
    RecyclerView rv_store;
    List<StoreModel> storeModelList = new ArrayList<>();
    StoreAdapter storeAdapter;
    String user_location;
    EditText ed_search_store;
    String storeurl = "http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getCateg";
    String locationwise_storeurl = "http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getStoreLocationUser";
    String services1_url = "http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getServicesByLimit1";
    String services95_url = "http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getServicesByLimit95";
    String banners_url = "http://demo.digitalsolutionsplanet.com/motorkart/api/Register_android/getBanner";

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        sp_login = getContext().getSharedPreferences("LOGINDETAILS", Context.MODE_PRIVATE);
        ed_login = sp_login.edit();
        cid = sp_login.getString("cid", "");
        user_location = sp_login.getString("user_location", "");

        vw_slider = v.findViewById(R.id.vw_slider);
        ed_search_store = v.findViewById(R.id.ed_search_store);
        tv_car_rgno = v.findViewById(R.id.tv_car_rgno);
        tv_bike_rgno = v.findViewById(R.id.tv_bike_rgno);
        tv_car_prsv = v.findViewById(R.id.tv_car_pvsv);
        tv_bike_prsv = v.findViewById(R.id.tv_bike_prsv);
        //ll_partstore=v.findViewById(R.id.ll_partstore);
       
        /*ll_servicecntr=v.findViewById(R.id.ll_servicecntr);
        ll_alignmentcntr=v.findViewById(R.id.ll_alignmentcntr);
        ll_tyrestore=v.findViewById(R.id.ll_tyrestore);
        ll_washingpts=v.findViewById(R.id.ll_washingpts);
        ll_engineoil=v.findViewById(R.id.ll_engineoil);
        ll_tools=v.findViewById(R.id.ll_tools);
        ll_bikecar=v.findViewById(R.id.ll_bikecar);
        ll_batteries=v.findViewById(R.id.ll_batteries);*/
        indicator = v.findViewById(R.id.indicator);

        getBanners();


        servicestwowheeler(v);
        servicesfourwheeler(v);

        getStoreNames(v);

        ed_search_store.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                storeAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

      

      /*  ll_partstore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), PartStore.class));
            }
        });
        ll_servicecntr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ServiceCentre.class));
            }
        });

        ll_alignmentcntr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AlignmentCentre.class));
            }
        });

        ll_tyrestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TyreStore.class));
            }
        });

        ll_washingpts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), WashingPoints.class));
            }
        });


        ll_engineoil.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
        startActivity(new Intent(getContext(), EngineOil.class));

        }
        });

        ll_tools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ToolsNEquipments.class));

            }
        });

        ll_bikecar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), BikeNCarAccessories.class));

            }
        });


        ll_batteries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), BikeNCarBatteries.class));

            }
        });
*/

        return v;
    }

    private void getBanners() {

        RequestQueue rq = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.GET, banners_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equals("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int x = 0; x < jsonArray.length(); x++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(x);

                            String image = jsonObject1.getString("image");

                            imageslist.add(image);

                        }

                        TopSlider imageSlideradapter = new TopSlider(getContext(), imageslist);
                        vw_slider.setAdapter(imageSlideradapter);
                        indicator.setViewPager(vw_slider);

                        NUM_PAGES = imageslist.size();

                        final Handler handler = new Handler();
                        final Runnable Update = new Runnable() {
                            public void run() {
                                if (currentPage == NUM_PAGES) {
                                    currentPage = 0;
                                }
                                vw_slider.setCurrentItem(currentPage++, true);
                            }
                        };
                        Timer swipeTimer = new Timer();
                        swipeTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                handler.post(Update);
                            }
                        }, 3000, 3000);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        rq.add(sr);
    }

    private void servicestwowheeler(View v) {
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, services1_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equals("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("resultFirst");
                        for (int x = 0; x < jsonArray.length(); x++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(x);

                            String regno = jsonObject1.getString("registration_num");
                            String date_created = jsonObject1.getString("created_on");


                            //String output = regno.substring(0, 10);

                            tv_bike_rgno.setText(regno);


                            try {
                                String output = date_created.substring(0, 10);
                                tv_bike_prsv.setText(output);

                            } catch (Exception e) {
                                //  Toast.makeText(getActivity(), ""+e.toString(), Toast.LENGTH_SHORT).show();
                            }


                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("CID", cid);
                return map;
            }
        };
        rq.add(sr);
    }

    private void servicesfourwheeler(View v) {
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.POST, services95_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equals("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("resultFirst");
                        for (int x = 0; x < jsonArray.length(); x++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(x);
                            String regno = jsonObject1.getString("registration_num");
                            String date_created = jsonObject1.getString("created_on");
                            tv_car_rgno.setText(regno);

                            try {

                                //  DateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                //  Date date = inputFormatter.parse(date_created);

                                //   DateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");
                                //    String output = outputFormatter.format(date);
                                String output = date_created.substring(0, 10);
                                tv_car_prsv.setText(output);


                            } catch (Exception e) {
                                //  Toast.makeText(getActivity(), ""+e.toString(), Toast.LENGTH_SHORT).show();
                            }


                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("CID", cid);
                return map;
            }
        };
        rq.add(sr);
    }


    private void getStoreNames(final View view) {

        RequestQueue rq = Volley.newRequestQueue(getActivity());
        StringRequest sr = new StringRequest(Request.Method.GET, storeurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");

                    if (status.equals("success")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        for (int x = 0; x < jsonArray.length(); x++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(x);
                            StoreModel storeModel = new StoreModel(jsonObject1.getString("tid"),
                                    jsonObject1.getString("name"), jsonObject1.getString("avtar"));
                            storeModelList.add(storeModel);
                        }
                        Log.e("msg", "StoreResponse: " + storeModelList.size());
                        rv_store = view.findViewById(R.id.rv_store);
                        rv_store.setLayoutManager(new GridLayoutManager(getContext(), 4));
                        storeAdapter = new StoreAdapter(getActivity(), storeModelList);
                        rv_store.setAdapter(storeAdapter);
                    } else {
                        Toast.makeText(getActivity(), "No store", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), "Check Internet Connection", Toast.LENGTH_SHORT).show();


                //Toast.makeText(LoginActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
        rq.add(sr);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        void onFragmentInteraction(Uri uri);
    }
}
