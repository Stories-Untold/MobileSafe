package com.myapp.mobilesafe.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.myapp.mobilesafe.R;

/**
 * Created by 庹大伟 on 2014/9/20.
 */
public class AddressSelectFragment extends DialogFragment {

    private AlertDialog dialog;
    private String[] name = {"蓝色", "金属灰", "绿色", "橙色", "灰色"};
    private Integer[] background = {
            R.drawable.call_locate_blue,
            R.drawable.call_locate_gray,
            R.drawable.call_locate_green,
            R.drawable.call_locate_orange,
            R.drawable.call_locate_white
    };
    private SharedPreferences sharedPreferences;

    public static AddressSelectFragment newInstance(String title) {
        AddressSelectFragment addressSelectFragment = new AddressSelectFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        addressSelectFragment.setArguments(args);
        return addressSelectFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        sharedPreferences = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        int address_style_id = sharedPreferences.getInt("address_style_id", 3);
        dialog = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setSingleChoiceItems(name, address_style_id, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        int style = background[i];
                        editor.putInt("address_style", style);
                        editor.putInt("address_style_id", i);
                        editor.commit();
                        dialog.dismiss();
                    }
                })
                .create();
        return dialog;
    }
}
