package com.mobapphome.mahandroidupdater;

/**
 * Created by settar on 7/12/16.
 */


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mobapphome.mahandroidupdater.tools.MAHUpdaterController;
import com.mobapphome.mahandroidupdater.types.DlgModeEnum;
import com.mobapphome.mahandroidupdater.types.ProgramInfo;
// ...

public class MAHUpdaterDlg extends DialogFragment implements
        android.view.View.OnClickListener {

    ProgramInfo programInfo;
    DlgModeEnum type;

    public MAHUpdaterDlg() {
        // Empty constructor required for DialogFragment
    }

    public static MAHUpdaterDlg newInstance(ProgramInfo programInfo, DlgModeEnum type) {
        MAHUpdaterDlg dialog = new MAHUpdaterDlg();

        Bundle args = new Bundle();
        Gson gson = new Gson();
        args.putString("programInfo", gson.toJson(programInfo));
        args.putSerializable("type", type);
        dialog.setArguments(args);
        return dialog;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("Test", "MAH Dld exit Created ");
        Bundle arg = getArguments();
        Gson gson = new Gson();
        programInfo = gson.fromJson(arg.getString("programInfo"), ProgramInfo.class);
        type = (DlgModeEnum) arg.getSerializable("type");
        Log.i("Test", "Updateinfo from bundle " + programInfo.getUpdateInfo());

        View view = inflater.inflate(R.layout.mah_updater_dlg, container);

        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && keyCode == KeyEvent.KEYCODE_BACK) {

                    onNo();
                    return true;
                }
                return false;
            }
        });
        Button btnYes = ((Button) view.findViewById(R.id.mah_updater_dlg_btn_update));
        btnYes.setOnClickListener(this);

        TextView tvInfo = (TextView)view.findViewById(R.id.tvInfoTxt);

        if(type.equals(DlgModeEnum.UPDATE)){
            btnYes.setText(getResources().getText(R.string.mah_android_updater_dlg_btn_yes_update_txt));
            tvInfo.setText(getResources().getText(R.string.mah_android_updater_info_update));
        }else if(type.equals(DlgModeEnum.INSTALL)){
            btnYes.setText(getResources().getText(R.string.mah_android_updater_dlg_btn_yes_install_txt));
            tvInfo.setText(getResources().getText(R.string.mah_android_updater_info_install));
        }

        TextView tvUpdateInfo = (TextView) view.findViewById(R.id.tvUpdateInfo);
        if(programInfo.getUpdateInfo() != null){
            tvUpdateInfo.setText(programInfo.getUpdateInfo());
            tvUpdateInfo.setVisibility(View.VISIBLE);
        }else{
            tvUpdateInfo.setVisibility(View.GONE);
        }

        Button btnNo = (Button) view.findViewById(R.id.mah_updater_dlg_btn_dont_update);
        btnNo.setOnClickListener(this);

        ((ImageButton) view.findViewById(R.id.mah_updater_dlg_btnCancel)).setOnClickListener(this);


        MAHUpdaterController.setFontTextView((TextView) view.findViewById(R.id.tvTitle));
        MAHUpdaterController.setFontTextView((TextView) view.findViewById(R.id.tvInfoTxt));
        MAHUpdaterController.setFontTextView(btnYes);
        MAHUpdaterController.setFontTextView(btnNo);

        return view;
    }

    public void onYes(){
        if(!programInfo.getUriCurrent().isEmpty()){
            Intent marketIntent = new Intent(Intent.ACTION_VIEW);
            marketIntent.setData(Uri.parse("market://details?id="+programInfo.getUriCurrent()));
            getActivity().startActivity(marketIntent);
        }
    };

    public void onNo(){
        dismiss();
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.mah_updater_dlg_btnCancel) {
            onNo();
        } else if (v.getId() == R.id.mah_updater_dlg_btn_update) {
            onYes();
        } else if (v.getId() == R.id.mah_updater_dlg_btn_dont_update) {
            onNo();
        }
    }
}