package com.wafflehaus.wearuatt.phone;

import com.wafflehaus.wearuatt.phone.R;


import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.wearable.view.CircledImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ConfirmFragment extends Fragment    {
	private String presence;
	
	public ConfirmFragment(String p){
		this.presence = p;
	}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirmfragment,
                                     container, false);

        CircledImageView nextButton = (CircledImageView) view.findViewById(R.id.button_confirm);
        
        return view;
    }
	
	public String getPresence() {
		return presence;
	}
	public void setPresence(String presence) {
		this.presence = presence;
	}
	
}
