package com.netcracker.myapplication.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.netcracker.myapplication.Application.AppDriverAssist;
import com.netcracker.myapplication.Entity.OrderEntityTO;
import com.netcracker.myapplication.R;
import com.netcracker.myapplication.Security.TokenService;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private AutoCompleteTextView mLoginView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        mLoginView = findViewById(R.id.login);
        populateAutoComplete();

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button SignInButton = findViewById(R.id.sign_in_button);
        SignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

   @Override
    protected void onStart() {
        super.onStart();
    }

   ///ToDo переписать данные методы были добавлены автоматически при создании активити
    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mLoginView, R.string.error_invalid_login, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void attemptLogin() {
        mLoginView.setError(null);
        mPasswordView.setError(null);

        String login = mLoginView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(login)) {
            mLoginView.setError(getString(R.string.error_field_required));
            focusView = mLoginView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            //получаем файл с настройками и устанавливаем токен, содержащий логин и пароль
            AppDriverAssist.getApplicationPreferences().getEditor().putString(TokenService.AUTH, TokenService.getToken(login, password)).commit();
            AppDriverAssist.getApi().getAuth().enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    if (response == null || !response.isSuccessful()) {
                        // если авторизация не прошла, то удаляем SharedPreferences и делаем какое то диалоговое окно показываем
                        if (response.code() == 401) {
                            AppDriverAssist.getApplicationPreferences().getEditor().clear().commit();
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle(R.string.error_incorrect_login_password);
                            builder.setNegativeButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            builder.create().show();

                            return;
                        }
                    }

                    BufferedSource source = response.body().source();

                    try {
                        source.request(Long.MAX_VALUE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String  body = source.buffer().clone().readString(Charset.forName("UTF-8"));

                    if (!body.equals("USER") && !body.equals("ADMIN")) {
                        AppDriverAssist.getApplicationPreferences().getEditor().putString(TokenService.ID_DRIVER, body).commit();
                    }

                    String token = response.headers().get("Authorization");
                    AppDriverAssist.getApplicationPreferences().getEditor().putString(TokenService.AUTH, token).commit();
                    setActivity();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.getCause();
                }
            });
            showProgress(false);
            mLoginView.setText("");
            mPasswordView.setText("");
        }
    }

    private void setActivity(){

        String idDriver = AppDriverAssist.getApplicationPreferences().getSharedPreferences().getString(TokenService.ID_DRIVER, "");
        if(!idDriver.equals("")){
           long id =  Long.parseLong(idDriver);
            AppDriverAssist.getApi().getDriverIsOnShiftById(id).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Class activityClass = WaitingActivity.class;

                    String body = "";
                    BufferedSource source = response.body().source();
                    try {
                        source.request(Long.MAX_VALUE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    body = source.buffer().clone().readString(Charset.forName("UTF-8"));

                    if (response.isSuccessful() && body.equals("true")) {
                        AppDriverAssist.getApplicationPreferences().getEditor().putBoolean(TokenService.DRIVER_ON_SHIFT, true).commit();
                        //если водитель на сменне, то нужно в Shared Preference искать заказ,
                        // если его там нет то переход на страницу ожидания иначе на страницу заказа
                        OrderEntityTO order  = (OrderEntityTO) AppDriverAssist.getApplicationPreferences().getObject(new OrderEntityTO());
                        if (order!=null){
                            if(!order.getStatusOrder().equals(OrderEntityTO.ORDER_COMPLETE)){
                                activityClass = OrderActivity.class;
                            }
                        }
                    } else {
                        AppDriverAssist.getApplicationPreferences().getEditor().putBoolean(TokenService.DRIVER_ON_SHIFT, false).commit();
                    }
                    Intent intent = new Intent(getApplicationContext(), activityClass);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }

    });
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}