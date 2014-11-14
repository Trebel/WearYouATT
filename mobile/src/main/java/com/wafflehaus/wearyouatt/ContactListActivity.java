package com.wafflehaus.wearyouatt;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class ContactListActivity extends Activity {

    private static final String TAG = "ContactListActivity";

    enum Availability {
        AVAILABLE, BUSY, OFFLINE
    }

    public static final String KEY_CONTACT = "contact";
    public static final String KEY_NAME = "name";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_STATUS = "status";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_AVAILABILITY = "availability";

    private ListView mList;
    private LazyAdapter mAdapter;
    private ImageView mAvailability;
    private IntentFilter mReceiveFilter;
    private BroadcastReceiver mAvailabilityHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        mList = (ListView) findViewById(R.id.listView1);
        mAvailability = (ImageView) findViewById(R.id.contact_header_availability);

        // Setup Broadcast receiver
        mAvailabilityHandler = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(TAG, "ContactListActivity.handler.new BroadcastReceiver() {...}.onReceive(): " + Thread.currentThread());
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    String presence = extras.getString("presence");
                    if (presence != null) {
                        setAvailabilityLight(Availability.valueOf(presence));
                    }
                }
            }
        };
        mReceiveFilter = new IntentFilter(getClass().getName());
        LocalBroadcastManager.getInstance(this).registerReceiver(mAvailabilityHandler, mReceiveFilter);

        // Read contact data from XML file
        XMLParser parser = new XMLParser();
        String xml = convertStreamToString(getResources().openRawResource(
                R.raw.contactdata));
        Document doc = parser.getDomElement(xml);

        // Build contact list from DOM
        ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();
        NodeList nl = doc.getElementsByTagName(KEY_CONTACT);
        for (int i = 0; i < nl.getLength(); i++) {
            HashMap<String, String> contact = new HashMap<String, String>();
            Element e = (Element) nl.item(i);

            contact.put(KEY_NAME, parser.getValue(e, KEY_NAME));
            contact.put(KEY_LOCATION, parser.getValue(e, KEY_LOCATION));
            contact.put(KEY_STATUS, parser.getValue(e, KEY_STATUS));
            contact.put(KEY_IMAGE, parser.getValue(e, KEY_IMAGE));
            contact.put(KEY_AVAILABILITY, parser.getValue(e, KEY_AVAILABILITY));

            contactList.add(contact);
        }

        // Bind contact data to list adapter
        mAdapter = new LazyAdapter(this, contactList);
        mList.setAdapter(mAdapter);
    }

    private void setAvailabilityLight(Availability availability) {
        int availabilityId;
        switch (availability) {
            case AVAILABLE:
                availabilityId = R.drawable.status_available;
                break;
            case BUSY:
                availabilityId = R.drawable.status_busy;
                break;
            case OFFLINE:
                availabilityId = R.drawable.status_offline;
                break;
            default:
                availabilityId = R.drawable.status_offline;
                break;
        }
        mAvailability.setImageDrawable(getResources().getDrawable(
                availabilityId));
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
