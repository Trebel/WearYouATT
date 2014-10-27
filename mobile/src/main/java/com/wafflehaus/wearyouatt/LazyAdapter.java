package com.wafflehaus.wearyouatt;

// So Lazy

import java.util.ArrayList;
import java.util.HashMap;

import com.wafflehaus.wearyouatt.ContactListActivity.Availability;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;

	public LazyAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.list_item_contact, null);

		TextView contactName = (TextView) vi.findViewById(R.id.contact_name);
		TextView contactLocation = (TextView) vi
				.findViewById(R.id.contact_location);
		TextView contactStatus = (TextView) vi
				.findViewById(R.id.contact_status);
		ImageView contactImage = (ImageView) vi
				.findViewById(R.id.contact_image);
		ImageView contactAvailability = (ImageView) vi
				.findViewById(R.id.contact_header_availability);

		HashMap<String, String> contact = new HashMap<String, String>();
		contact = data.get(position);

		// Setting all values in listview
		contactName.setText(contact.get(ContactListActivity.KEY_NAME));
		contactLocation.setText(contact.get(ContactListActivity.KEY_LOCATION));
		contactStatus.setText(contact.get(ContactListActivity.KEY_STATUS));
		int imageId = activity.getResources().getIdentifier(
				contact.get(ContactListActivity.KEY_IMAGE), "drawable",
				activity.getPackageName());
		contactImage.setImageDrawable(activity.getResources().getDrawable(
				imageId));

		int availabilityId;

		String avail = contact.get(ContactListActivity.KEY_AVAILABILITY);
		switch (ContactListActivity.Availability.valueOf(avail)) {
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

		contactAvailability.setImageDrawable(activity.getResources()
				.getDrawable(availabilityId));
		return vi;
	}
}
