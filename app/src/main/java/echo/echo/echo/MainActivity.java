package echo.echo.echo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.VoiceInteractor;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.GetChars;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    AlarmItems sampleItem=new AlarmItems();
    ArrayList<AlarmItems> alarmList;
    AlarmAdapter alarmAdapter;
    Button addBtn;
    DatabaseHandler db;
    static final int PICK_CONTACT=1;
    String newEvent, newRemindText, newDetailText, newKey = "1";
    int smpId=0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(new ServiceReceiver(), new IntentFilter("android.intent.action.PHONE_STATE"));
        registerReceiver(new NetworkChangeReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        //registerReceiver(new WifiBroadcastReceiver(), new IntentFilter("android.net.wifi.WIFI_STATE_CHANGED"));

        db = new DatabaseHandler(this);

        alarmList = new ArrayList<>();
        alarmList = db.getAllAlarmItems();

        listView = (ListView) findViewById(R.id.listView1);
        addBtn = (Button) findViewById(R.id.addBtn);

        alarmAdapter = new AlarmAdapter(this, alarmList);
        listView.setAdapter(alarmAdapter);

        if(listView.getAdapter().getCount() == 0) {
            AlarmItems dummy = new AlarmItems();
            alarmList.add(dummy);
        }

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                final View setAlarmDialogView = factory.inflate(
                        R.layout.set_alarm, null);
                final AlertDialog setAlarmDialog = new AlertDialog.Builder(MainActivity.this).create();
                setAlarmDialog.setView(setAlarmDialogView);
                setAlarmDialog.setCancelable(false);
                final Spinner eventSpinner = (Spinner) setAlarmDialogView.findViewById(R.id.eventSpinner);
                final EditText remindEditText = (EditText) setAlarmDialogView.findViewById(R.id.remindEditText);
                final TextView detailTextView = (TextView) setAlarmDialogView.findViewById(R.id.detailTextView);
                final Button button = (Button) setAlarmDialogView.findViewById(R.id.okBtn);
                eventSpinner.setSelection(5);

                eventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        switch (pos) {
                            case 0:
                                button.setEnabled(true);
                                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                                startActivityForResult(intent, PICK_CONTACT);
                                break;
                            case 1:
                                button.setEnabled(true);
                                Toast.makeText(MainActivity.this, "Outgoing calls", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                button.setEnabled(true);
                                Toast.makeText(MainActivity.this, "Internet connected", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                button.setEnabled(true);
                                Toast.makeText(MainActivity.this, "Wifi connected", Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                button.setEnabled(true);
                                Toast.makeText(MainActivity.this, "Specific Wifi", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(MainActivity.this, Main2Activity.class);
                                startActivityForResult(i, 2);
                            case 5:
                                button.setEnabled(true);
                                break;
                            default:
                                Log.e("Unknown Event", "0-4");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                setAlarmDialogView.findViewById(R.id.okBtn).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //add to list

                        newEvent = eventSpinner.getSelectedItem().toString();
                        newRemindText = remindEditText.getText().toString();
                        newDetailText = "Event: " + newEvent;
                        detailTextView.setText(newDetailText);
                        sampleItem.setEvent(newEvent);
                        sampleItem.setRemind(newRemindText);
                        sampleItem.setDetail(newDetailText);
                        sampleItem.setCheck(1);
                        sampleItem.setKey(newKey);
                        db.addAlarmItems(sampleItem);

                        alarmList.add(sampleItem);
                        alarmAdapter = new AlarmAdapter(MainActivity.this, alarmList);
                        listView.setAdapter(alarmAdapter);
                        //refresh the list
                        listView.invalidateViews();
                        alarmAdapter.notifyDataSetChanged();

                        setAlarmDialog.dismiss();

                    }
                });
                setAlarmDialog.show();
            }
        });
        listView.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //edit the alarmitem
                Log.d("Entered","Onclick"+position);
                LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                final View setAlarmDialogView = factory.inflate(
                        R.layout.set_alarm, null);

                final AlertDialog setAlarmDialog = new AlertDialog.Builder(MainActivity.this).create();
                setAlarmDialog.setView(setAlarmDialogView);
                setAlarmDialog.setCanceledOnTouchOutside(false);
                setAlarmDialogView.findViewById(R.id.deleteBtn).setVisibility(View.VISIBLE);
                final Spinner eventSpinner = (Spinner) setAlarmDialogView.findViewById(R.id.eventSpinner);
                final EditText remindEditText = (EditText) setAlarmDialogView.findViewById(R.id.remindEditText);
                final TextView detailTextView = (TextView) setAlarmDialogView.findViewById(R.id.detailTextView);
                final AlarmItems curItem= (AlarmItems) listView.getItemAtPosition(position);
                final Button button = (Button) setAlarmDialogView.findViewById(R.id.okBtn);

                eventSpinner.setSelection(((ArrayAdapter<String>) eventSpinner.getAdapter()).getPosition(curItem.getEvent()));
                remindEditText.setText(curItem.getRemind());
                detailTextView.setText(curItem.getDetail());
                setAlarmDialog.show();
                eventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        switch (pos) {
                            case 0:
                                button.setEnabled(true);
                                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                                startActivityForResult(intent, PICK_CONTACT);
                                break;
                            case 1:
                                button.setEnabled(true);
                                Toast.makeText(MainActivity.this,"Outgoing calls",Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                button.setEnabled(true);
                                Toast.makeText(MainActivity.this,"Internet connected",Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                button.setEnabled(true);
                                Toast.makeText(MainActivity.this,"Wifi connected",Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                button.setEnabled(true);

                                Toast.makeText(MainActivity.this, "Specific Wifi", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(MainActivity.this, Main2Activity.class);
                                startActivityForResult(i, 2);
                                break;
                            case 5:
                                button.setEnabled(false);
                                break;
                            default:
                                Log.e("Unknown Event","0-4");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                setAlarmDialogView.findViewById(R.id.okBtn).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //edit the alarm


                        db.deleteAlarmItems(curItem);

                        newEvent = eventSpinner.getSelectedItem().toString();
                        newRemindText = remindEditText.getText().toString();
                        newDetailText = "Event: " + newEvent;
                        detailTextView.setText(newDetailText);


                        sampleItem.setEvent(newEvent);
                        sampleItem.setRemind(newRemindText);
                        sampleItem.setDetail(newDetailText);
                        sampleItem.setCheck(1);
                        sampleItem.setKey(newKey);

                        Log.d("Saving", sampleItem.getKey());

                        alarmList.add(alarmList.indexOf(curItem), sampleItem);
                        alarmList.remove(alarmList.indexOf(curItem));
                        db.addAlarmItems(sampleItem);
                        //set the adapter

                        alarmAdapter = new AlarmAdapter(MainActivity.this, alarmList);
                        listView.setAdapter(alarmAdapter);
                        //refresh the list
                        listView.invalidateViews();
                        alarmAdapter.notifyDataSetChanged();
                        //dismiss
                        setAlarmDialog.dismiss();
                    }
                });

                setAlarmDialogView.findViewById(R.id.deleteBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //delete
                        AlarmItems curItem = new AlarmItems();
                        curItem = (AlarmItems) listView.getItemAtPosition(position);
                        db.deleteAlarmItems(curItem);
                        alarmList.remove(position);
                        //set the adapter
                        alarmAdapter = new AlarmAdapter(MainActivity.this, alarmList);
                        listView.setAdapter(alarmAdapter);
                        //refresh the list
                        listView.invalidateViews();
                        alarmAdapter.notifyDataSetChanged();
                        //dismiss
                        setAlarmDialog.dismiss();

                    }
                });
            }
        });



    }




    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String cNumber=new String();

                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                                    null, null);
                            phones.moveToFirst();
                            cNumber = phones.getString(phones.getColumnIndex("data1"));

                        }
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                        Log.d("number",cNumber);
                        Log.d("name",name);

                        cNumber = cNumber.replaceAll(" ", "");
                        newKey = cNumber;
                        for(AlarmItems i: db.getAllAlarmItems()){
                            if(i.getId()>smpId)
                                smpId=i.getId();
                        }
                        sampleItem.setId(smpId);
                        db.deleteAlarmItems(sampleItem);
                        sampleItem.setKey(newKey);
                        db.addAlarmItems(sampleItem);

                    }
                }
                break;

            case 0 : Toast.makeText(MainActivity.this, "no Wifi", Toast.LENGTH_SHORT).show();
                break;

            case 2 :
                if (resultCode == Activity.RESULT_OK)
                {
                    String cNumber = new String();
                    cNumber = data.getStringExtra("result");
                    newKey = cNumber;
                    for(AlarmItems i: db.getAllAlarmItems()){
                    if(i.getId()>smpId)
                        smpId=i.getId();
                    }
                    sampleItem.setId(smpId);
                    db.deleteAlarmItems(sampleItem);
                    sampleItem.setKey(newKey);
                    db.addAlarmItems(sampleItem);
                }
                break;

        }
    }

}