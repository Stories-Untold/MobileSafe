package com.myapp.mobilesafe.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.myapp.mobilesafe.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 庹大伟 on 2014/9/10.
 */
public class SelectContactFragment extends DialogFragment implements AdapterView.OnItemClickListener {

    private AlertDialog dialog;
    private List<Map<String, String>> contactInfo = new ArrayList<Map<String, String>>();

    public static SelectContactFragment newInstance(String title) {
        SelectContactFragment selectContactFragment = new SelectContactFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        selectContactFragment.setArguments(args);
        return selectContactFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View contact = View.inflate(getActivity(), R.layout.activity_select_contact, null);
        ListView contactList = (ListView) contact.findViewById(R.id.contact_list);
        contactInfo = getContactInfo();
        contactList.setAdapter(new SimpleAdapter(getActivity(), contactInfo, R.layout.list_contact_item, new String[]{"name", "phone"}, new int[]{R.id.name, R.id.phone}));
        contactList.setOnItemClickListener(this);
        String title = getArguments().getString("title");
        dialog = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setView(contact)
                .create();
        return dialog;
    }

    /**
     * 读取联系人
     *
     * @return
     */
    private List<Map<String, String>> getContactInfo() {
        ContentResolver resolver = getActivity().getContentResolver();
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri uriData = Uri.parse("content://com.android.contacts/data");
        Cursor cursor = resolver.query(uri, new String[]{"contact_id"}, null, null, null);
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        while (cursor.moveToNext()) {
            String contact_id = cursor.getString(0);
            if (contact_id != null) {
                Map<String, String> map = new HashMap<String, String>();
                Cursor dataCursor = resolver.query(uriData, new String[]{"data1", "mimetype"}, "contact_id=?", new String[]{contact_id}, null);
                while (dataCursor.moveToNext()) {
                    String data1 = dataCursor.getString(0);
                    String mimetype = dataCursor.getString(1);
                    //姓名
                    if ("vnd.android.cursor.item/name".equals(mimetype)) {
                        map.put("name", data1);
                    } else if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) { //电话
                        map.put("phone", data1);
                    }
                }
                data.add(map);
                dataCursor.close();
            }
        }
        cursor.close();
        return data;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        EditText safeNum = (EditText) getActivity().findViewById(R.id.safeNum);
        safeNum.setText(contactInfo.get(i).get("phone") + "(" + contactInfo.get(i).get("name") + ")");
        dialog.dismiss();
    }
}
