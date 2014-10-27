package com.wafflehaus.wearyouatt;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.ImageReference;
import android.util.AttributeSet;
import android.view.Gravity;

/**
 * Constructs fragments as requested by the GridViewPager. For each row a
 * different background is provided.
 */
public class SampleGridPagerAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    
    public String selectedOption;
	enum Availability {
		AVAILABLE, BUSY, OFFLINE
	}

    public SampleGridPagerAdapter(Context ctx, FragmentManager fm) {
        super(fm);
        mContext = ctx;
    }

    static final int[] BG_IMAGES = new int[] {
            R.drawable.ic_busy,
            R.drawable.ic_available,
            R.drawable.ic_ooo2
    };

    /** A simple container for static data in each page */
    private static class Page {
        int titleRes;
        int textRes;
        int iconRes;
        int cardGravity = Gravity.BOTTOM;
        boolean expansionEnabled = true;
        float expansionFactor = 1.0f;
        int expansionDirection = CardFragment.EXPAND_DOWN;

        public Page(int titleRes, int textRes, boolean expansion) {
            this(titleRes, textRes, 0);
            this.expansionEnabled = expansion;
        }

        public Page(int titleRes, int textRes, boolean expansion, float expansionFactor) {
            this(titleRes, textRes, 0);
            this.expansionEnabled = expansion;
            this.expansionFactor = expansionFactor;
        }

        public Page(int titleRes, int textRes, int iconRes) {
            this.titleRes = titleRes;
            this.textRes = textRes;
            this.iconRes = iconRes;
        }

        public Page(int titleRes, int textRes, int iconRes, int gravity) {
            this.titleRes = titleRes;
            this.textRes = textRes;
            this.iconRes = iconRes;
            this.cardGravity = gravity;
            
        }
        
        public Page(){
        }
    }

    private final Page[][] PAGES = {
            {
                    new Page(R.string.busy_label, R.string.busy_description, true,
                            Gravity.BOTTOM), new Page(),
            },
            {
                    new Page(R.string.available_label, R.string.available_description, true,
                    		Gravity.BOTTOM), new Page(),
            },
            {
                    new Page(R.string.OOO_label, R.string.OOO_description, true, Gravity.BOTTOM), new Page(),
            },
            
    };
    

   
    @Override
    public Fragment getFragment(int row, int col) {
    	
    	if(col > 0){
    		if(row == 0){
    			selectedOption = "BUSY";
    			return new ConfirmFragment("BUSY");
    		}else if(row == 1){
    			selectedOption = "AVAILABLE";
    			return new ConfirmFragment("AVAILABLE");
    			
    		}else if(row == 2){
    			selectedOption = "OFFLINE";
    			return new ConfirmFragment("OFFLINE");
    		}
    	}
        
    	Page page = PAGES[row][col];
        String title = page.titleRes != 0 ? mContext.getString(page.titleRes) : null;
        String text = page.textRes != 0 ? mContext.getString(page.textRes) : null;
        CardFragment fragment = CardFragment.create(title, text, page.iconRes);
        // Advanced settings
        
        fragment.setCardGravity(page.cardGravity);
        fragment.setExpansionEnabled(page.expansionEnabled);
        fragment.setExpansionDirection(page.expansionDirection);
        fragment.setExpansionFactor(page.expansionFactor);

        return fragment;
    }

    @Override
    public ImageReference getBackground(int row, int column) {
        return ImageReference.forDrawable(BG_IMAGES[row % BG_IMAGES.length]);
    }

    @Override
    public int getRowCount() {
        return PAGES.length;
    }

    @Override
    public int getColumnCount(int rowNum) {
        return PAGES[rowNum].length;
    }
}