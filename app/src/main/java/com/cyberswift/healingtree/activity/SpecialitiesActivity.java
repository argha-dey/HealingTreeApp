package com.cyberswift.healingtree.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import com.cyberswift.healingtree.R;
import com.cyberswift.healingtree.adapters.DoctorDepartmentAdapter;
import com.cyberswift.healingtree.model.DoctorDepartmentModel;
import com.cyberswift.healingtree.model.DoctorDidDName;
import com.cyberswift.healingtree.retrofit.ApiClient;
import com.cyberswift.healingtree.retrofit.ApiInterface;
import com.cyberswift.healingtree.utils.Urls;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

public class SpecialitiesActivity extends AppCompatActivity {
    private Activity activity;
    ArrayList<DoctorDidDName> doctorDidDNameArrayList;
    RecyclerView specialistRecyclerView;
    EditText et_department_search;
    DoctorDepartmentAdapter doctorDepartmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentLayout(R.layout.activity_specialities);
        setContentView(R.layout.activity_specialities);

        initialViews();

      //  getSupportActionBar().setTitle("");
    //    checkedMenuItem = navView.getMenu().findItem(Constants.HOME_NAV_ID);
     //   checkedMenuItem.setChecked(true);

        doctorDepartmentServiceCall();
    }

    private void doctorDepartmentServiceCall() {
        String categoryUrl = Urls.DEPARTMENT_LIST;
        ApiInterface apiService = ApiClient.getRetrofit().create(ApiInterface.class);
        Call<DoctorDepartmentModel> call = apiService.doctorDepartment(categoryUrl);
        call.enqueue(new Callback<DoctorDepartmentModel>() {
            @Override
            public void onResponse(Call<DoctorDepartmentModel> call, Response<DoctorDepartmentModel> response) {
               DoctorDepartmentModel model = response.body();
                doctorDidDNameArrayList = new ArrayList<>();
                if(response.body().getStatus().equals("true")){
                    doctorDidDNameArrayList = model.getDoctorDidDNameList();
                    setDoctorDidDNameInList(doctorDidDNameArrayList);
                }
            }

            @Override
            public void onFailure(Call<DoctorDepartmentModel> call, Throwable t) {

            }
        });


    }


    private void initialViews() {
        activity = SpecialitiesActivity.this;
     //   emergency_icon.setVisibility(View.VISIBLE);
        et_department_search =   findViewById(R.id.et_department_search);
        specialistRecyclerView = (RecyclerView) findViewById(R.id.specialistRecyclerView);
        et_department_search.setText("");
        et_department_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });


    }
    void filter(String text){
        if(doctorDidDNameArrayList.size()>0) {
            ArrayList<DoctorDidDName> temp = new ArrayList();
            for (DoctorDidDName d : doctorDidDNameArrayList) {
                if (d.getDEPT_NAME().toUpperCase().trim().contains(text.toUpperCase().trim())) {
                    temp.add(d);
                }
            }
            doctorDepartmentAdapter.updateList(temp);
        }
    }
  //add icons
  public void setDoctorDidDNameInList(ArrayList<DoctorDidDName> doctorDidDNameArray) {
      specialistRecyclerView.setLayoutManager(new GridLayoutManager(activity, 4));
      specialistRecyclerView.setItemAnimator(new DefaultItemAnimator());
      doctorDepartmentAdapter = new DoctorDepartmentAdapter(activity, doctorDidDNameArray);
      specialistRecyclerView.setAdapter(doctorDepartmentAdapter);
  }

public void onSpecialistDoctorBackButtonClick(View view){
    Intent intentBack = new Intent(activity,HomeActivity.class);
    intentBack.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intentBack);
}

}
