package com.smartwave.taskr.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartwave.taskr.R;
import com.smartwave.taskr.core.AppController;
import com.smartwave.taskr.core.SharedPreferencesCore;
import com.smartwave.taskr.test.BlurBitmap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        TextView mTextUser = (TextView) view.findViewById(R.id.username);
        mTextUser.setText(SharedPreferencesCore.getSomeStringValue(AppController.getInstance(),"username"));

        ImageView mImageCover = (ImageView) view.findViewById(R.id.imagebackground);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sunflowers);
        Bitmap blurred = BlurBitmap.blurRenderScript(getActivity(),bitmap, 25);
        mImageCover.setImageBitmap(blurred);



        return view;
    }

}
