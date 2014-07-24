package com.wafflehaus.wearuatt.phone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

public class DataLayerListenerService extends WearableListenerService {
	
	private static final String TAG = "DataLayerListenerServic";
	private static final String PRESENCE_KEY = "presence";
	private static final String PRESENCE_PATH = "/presence";
	private static final int TIMEOUT_MS = 5000;
	
	GoogleApiClient mGoogleApiClient;
	
	
    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
        
        
    }
    
    
    
    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
    	Toast.makeText(getApplicationContext(), "on data change",
     		   Toast.LENGTH_LONG).show();
    	
        LOGD(TAG, "onDataChanged: " + dataEvents);
        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        dataEvents.close();
        if(!mGoogleApiClient.isConnected()) {
            ConnectionResult connectionResult = mGoogleApiClient
                    .blockingConnect(30, TimeUnit.SECONDS);
            if (!connectionResult.isSuccess()) {
                Log.e(TAG, "DataLayerListenerService failed to connect to GoogleApiClient.");
                return;
            }
        }
        
        for (DataEvent event : dataEvents) {
        	String presence;
            if (event.getType() == DataEvent.TYPE_CHANGED &&
                event.getDataItem().getUri().getPath().equals(PRESENCE_PATH)) {
              DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem()); 
              Asset asset = dataMapItem.getDataMap().getAsset(PRESENCE_KEY);
              try {
            	  presence = loadStringFromAsset(asset);
            	  Toast.makeText(getApplicationContext(), "Presence:"+presence,
                		   Toast.LENGTH_LONG).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				 Log.e(TAG, "Failed to parse asset.");
			}
              
            }
            
          }

        
    }
    
    public String loadStringFromAsset(Asset asset) throws IOException {
        if (asset == null) {
            throw new IllegalArgumentException("Asset must be non-null");
        }
        ConnectionResult result =
               mGoogleApiClient.blockingConnect(TIMEOUT_MS, TimeUnit.MILLISECONDS);
        if (!result.isSuccess()) {
            return null;
        }
        // convert asset into a file descriptor and block until it's ready
        InputStream assetInputStream = Wearable.DataApi.getFdForAsset(
                mGoogleApiClient, asset).await().getInputStream();
                mGoogleApiClient.disconnect();

        if (assetInputStream == null) {
            Log.w(TAG, "Requested an unknown Asset.");
            return null;
        }
        // decode the stream into a string
        
        StringBuilder buf=new StringBuilder();
        BufferedReader in=
            new BufferedReader(new InputStreamReader(assetInputStream, "UTF-8"));
        String str;

        while ((str=in.readLine()) != null) {
          buf.append(str);
        }

        in.close();
       
        return buf.toString();
        
    }
    


    @Override
    public void onPeerConnected(Node peer) {
        super.onPeerConnected(peer);

        String id = peer.getId();
        String name = peer.getDisplayName();

        Log.d(TAG, "Connected peer name & ID: " + name + "|" + id);
    }

    @Override
    public void onPeerDisconnected(Node peer) {
        LOGD(TAG, "onPeerDisconnected: " + peer);
    }
    
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
    	Toast.makeText(getApplicationContext(), "message received",
     		   Toast.LENGTH_LONG).show();
        if (messageEvent.getPath().equals("/ContactListActivity")) {
        	
        	Toast.makeText(getApplicationContext(), new String (messageEvent.getData()),
          		   Toast.LENGTH_LONG).show();
            Intent startIntent = new Intent(this, ContactListActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startIntent.putExtra("presence", new String (messageEvent.getData()));
            startActivity(startIntent);
        }
    }

    public static void LOGD(final String tag, String message) {
        if (Log.isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, message);
        }
    }
	

}
