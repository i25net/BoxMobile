package com.cgstate.boxmobile.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cgstate.boxmobile.R;
import com.cgstate.boxmobile.activities.CodeScanActivity;

/**
 * Created by Administrator on 2017/2/17.
 */

public class BarCodeDialogFragment extends DialogFragment implements View.OnClickListener {

    private TextView btnHandInput;
    private TextView btnAutoInput;
    private String str;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        Bundle bundle = getArguments();
        if(bundle!=null){
            str = bundle.getString("str");
        }

        View view = inflater.inflate(R.layout.barcode_select_dialog, container);
        btnHandInput = (TextView) view.findViewById(R.id.btn_hand_input_barcode);
        btnAutoInput = (TextView) view.findViewById(R.id.btn_auto_input_barcode);

        btnHandInput.setOnClickListener(this);
        btnAutoInput.setOnClickListener(this);


        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_hand_input_barcode:
                Log.d("BarCodeDialogFragment", "手动输入");
                break;

            case R.id.btn_auto_input_barcode:
                dismiss();
                Intent intent =new Intent(getActivity(), CodeScanActivity.class);
                intent.putExtra("str",str);
                startActivity(intent);
                break;
        }
    }
}
