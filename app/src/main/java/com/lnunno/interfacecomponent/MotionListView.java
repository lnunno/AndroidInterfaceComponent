package com.lnunno.interfacecomponent;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Lucas on 2/3/2015.
 */
public class MotionListView extends ListView {

    public MotionListView(Context context){
        super(context);
    }

    public MotionListView(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    public MotionListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,defStyleAttr);
    }
}
