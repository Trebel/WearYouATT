package com.wafflehaus.wearuatt.phone;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;

public class ContactListActivity extends Activity {

	enum Availability {
		AVAILABLE, BUSY, OFFLINE
	}

	public static final String KEY_CONTACT = "contact";
	public static final String KEY_NAME = "name";
	public static final String KEY_LOCATION = "location";
	public static final String KEY_STATUS = "status";
	public static final String KEY_IMAGE = "image";
	public static final String KEY_AVAILABILITY = "availability";

	ListView mList;
	LazyAdapter mAdapter;
	ImageView mAvailability;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_list);

		ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

		XMLParser parser = new XMLParser();

		String xml = convertStreamToString(getResources().openRawResource(R.raw.contactdata));
															// URL
		Document doc = parser.getDomElement(xml); // getting DOM element

		NodeList nl = doc.getElementsByTagName(KEY_CONTACT);
		// looping through all song nodes <song>
		for (int i = 0; i < nl.getLength(); i++) {
			// creating new HashMap
			HashMap<String, String> map = new HashMap<String, String>();
			Element e = (Element) nl.item(i);
			// adding each child node to HashMap key => value
			map.put(KEY_NAME, parser.getValue(e, KEY_NAME));
			map.put(KEY_LOCATION, parser.getValue(e, KEY_LOCATION));
			map.put(KEY_STATUS, parser.getValue(e, KEY_STATUS));
			map.put(KEY_IMAGE, parser.getValue(e, KEY_IMAGE));
			map.put(KEY_AVAILABILITY, parser.getValue(e, KEY_AVAILABILITY));

			// adding HashList to ArrayList
			songsList.add(map);
		}

		mList = (ListView) findViewById(R.id.listView1);
		
		mAvailability = (ImageView) findViewById(R.id.contact_header_availability);

		// Getting adapter by passing xml data ArrayList
		mAdapter = new LazyAdapter(this, songsList);
		mList.setAdapter(mAdapter);
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
