package com.example.adssdk.billing;

import static com.example.adssdk.constants.Constant.def_value;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class PurchasePrefs {

     final SharedPreferences preferences;
    public PurchasePrefs(Context appContext) {
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    }
    // Getters

    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    @NonNull
    public ArrayList<Integer> getListInt(String key) {
        String[] myList = TextUtils.split(preferences.getString(key, ""), "‚‗‚");
        ArrayList<String> arrayToList = new ArrayList<>(Arrays.asList(myList));
        ArrayList<Integer> newList = new ArrayList<>();

        for (String item : arrayToList)
            newList.add(Integer.parseInt(item));

        return newList;
    }


    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    // Put methods
    public void putInt(String key, int value) {
        checkForNullKey(key);
        preferences.edit().putInt(key, value).apply();
    }

    public void putLong(String key, long value) {
        checkForNullKey(key);
        preferences.edit().putLong(key, value).apply();
    }

    public void putBoolean(String key, boolean value) {
        checkForNullKey(key);
        preferences.edit().putBoolean(key, value).apply();
    }

    public void remove(String key) {
        preferences.edit().remove(key).apply();
    }

    public void clear() {
        preferences.edit().clear().apply();
    }

    private void checkForNullKey(@Nullable String key){
        if (key == null){
            throw new NullPointerException();
        }
    }
    public int getInt1(String key) {
        return preferences.getInt(key, 1);
    }

    public void putInt1(String key, int value) {
        checkForNullKey(key);
        preferences.edit().putInt(key, value).apply();
    }
    public void putStringT(@NotNull String s, @NotNull String s1) {
        checkForNullKey(s);
        preferences.edit().putString(s, s1).apply();
    }

    public String getStringT(@NotNull String s1) {
        return preferences.getString(s1, def_value);
    }

    public float getFloatCorner(String key) {
        return preferences.getFloat(key, 30F);
    }

    public void putFloatCorner(String key, float value) {
        checkForNullKey(key);
        preferences.edit().putFloat(key, value).apply();
    }
}
