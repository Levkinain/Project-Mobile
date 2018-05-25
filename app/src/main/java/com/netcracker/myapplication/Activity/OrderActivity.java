package com.netcracker.myapplication.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.netcracker.myapplication.Application.AppDriverAssist;
import com.netcracker.myapplication.Entity.DriverEntity;
import com.netcracker.myapplication.Entity.OrderEntityTO;
import com.netcracker.myapplication.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends MainActivity {

    private TextView nameOrder;
    private TextView firstAnfLastNameClient;
    private TextView adressOfClient;
    private String clientPhoneNumber;
    private Button callClientButton;
    private Button callOperatorsButton;
    private Button atClientButton;
    private Button completeOrderButton;
    private OrderEntityTO order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        nameOrder = findViewById(R.id.name_order);
        firstAnfLastNameClient = findViewById(R.id.name_client);
        adressOfClient = findViewById(R.id.adress_client);
        callClientButton = findViewById(R.id.call_client_button);
        callOperatorsButton = findViewById(R.id.call_operators_button);
        atClientButton = findViewById(R.id.at_client_button);
        completeOrderButton = findViewById(R.id.complete_order_buttton);
        order = new OrderEntityTO();
    }

    @Override
    protected void onStart() {

        super.onStart();
        order = (OrderEntityTO) AppDriverAssist.getApplicationPreferences().getObject(order);
        if (order != null) {

            nameOrder.setText(order.getName());
            firstAnfLastNameClient.setText(order.getClientFirstName() + " " + order.getClientLastName());
            adressOfClient.setText(order.getAddress());
            clientPhoneNumber = order.getClientPhoneNumber();

            if (order.getStatusOrder().equals(OrderEntityTO.DRIVER_TO_CLIENT)) {
                completeOrderButton.setVisibility(View.GONE);
                atClientButton.setVisibility(View.VISIBLE);
            }

            if (order.getStatusOrder().equals(OrderEntityTO.PICKED_CLIENT)) {
                completeOrderButton.setVisibility(View.VISIBLE);
                atClientButton.setVisibility(View.GONE);
            }

        }else{
            onStop();
        }
    }

    @Override
    protected void onStop() {
        if(order==null){
            Intent startWaitingActivity = new Intent(getApplicationContext(),WaitingActivity.class);
            startWaitingActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(startWaitingActivity);
        }
        super.onStop();
    }

    public void CallOperator(View view) {
        doCall(AppDriverAssist.OPERATOR_PHONE_NAMBER);
    }

    public void CallClient(View view) {
        if (order != null) {
            doCall(order.getClientPhoneNumber());
        }
    }

    public void SetStatusAtClient(final View view) {

        order.setStatusOrder(OrderEntityTO.PICKED_CLIENT);
        AppDriverAssist.getApi().pickClient(order).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    atClientButton.setVisibility(View.GONE);
                    completeOrderButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void SetStatusCompleteOrder(View view) {

        order.setStatusOrder(OrderEntityTO.ORDER_COMPLETE);
        AppDriverAssist.getApi().closeOrder(order).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    AppDriverAssist.getApplicationPreferences().clear(order.getClass().getCanonicalName());
                    //открываем форму waiting activity
                    Intent startOrderActivity = new Intent(getApplicationContext(), WaitingActivity.class);
                    startOrderActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(startOrderActivity);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });

        AppDriverAssist.getApplicationPreferences().clear(order.getClass().getCanonicalName());
    }

    public void doCall(String phoneNumber) {
        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intentCall);
    }

    public void showWayToClientClick(View view) {
        Intent showRoute = new Intent(OrderActivity.this,MapsActivity.class);
        String geoData = AppDriverAssist.getApplicationPreferences().getString(DriverEntity.DRIVER_GEO_LOCATION);
        if(geoData.isEmpty()){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Невозможно определить текущее местоположение!", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        showRoute.putExtra("beginPoint",geoData);
        showRoute.putExtra("endPoint",order.getGeoData());
        startActivity(showRoute);

    }

    public void showWayToDestinationClick(View view) {
        Intent showRoute = new Intent(OrderActivity.this,MapsActivity.class);
        showRoute.putExtra("beginPoint",order.getGeoData());
        showRoute.putExtra("endPoint",order.getDestinationGeoData());
        startActivity(showRoute);
    }

}
