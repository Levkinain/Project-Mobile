package com.netcracker.myapplication.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.netcracker.myapplication.Entity.OrderEntityTO;

public class ApplicationPreferences {

    public static final String APP_PREFERENCES = "AuthData";

    private SharedPreferences sharedPreferences;
    private Context context;
    private SharedPreferences.Editor mEditor;

    public ApplicationPreferences(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        this.mEditor = sharedPreferences.edit();
    }

    public void saveObject(Object object){
        String val = new Gson().toJson(object);
        mEditor.putString(object.getClass().getCanonicalName(), val).commit();
    }

    public Object getObject(Object object){
        try {
            String val = sharedPreferences.getString(object.getClass().getCanonicalName(), "");
            object = object.getClass();
            object = new Gson().fromJson(val, (Class<Object>) object);
        }catch (JsonSyntaxException exception){
            ///обработать исключение
        }
        return object;
    }

    public void putString(String key, String value ){
        mEditor.putString(key,value).commit();
    }

    public String getString(String key ){
        return sharedPreferences.getString(key, "");
    }

    public void putBoolean(String key, boolean value ){
        mEditor.putBoolean(key, value);
    }

    public boolean getBoolean(String key){
       return sharedPreferences.getBoolean(key, false);
    }

    public void clear(){
        mEditor.clear().commit();
    }

    public void clear(String nameAttrPref){
        try {
            sharedPreferences.edit().remove(nameAttrPref).commit();
        }catch (JsonSyntaxException exception){
        }
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public SharedPreferences.Editor getEditor() {
        return mEditor;
    }

}