package com.example.madara.training.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.madara.training.MyCards;
import com.example.madara.training.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by madara on 3/27/18.
 */

public class GetPassword extends DialogFragment {
    private static final String TAG = "GetPasswordFragment";
    @BindView(R.id.et_get_password)
    EditText _password;
    @BindView(R.id.tv_action_ok)
    TextView _ok;
    @BindView(R.id.tv_action_cancel)
    TextView _cancel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_get_password,container,false);
        ButterKnife.bind(this,view);

        return view;
    }

    @OnClick({R.id.tv_action_cancel, R.id.tv_action_ok})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.tv_action_cancel:
                Log.d(TAG, "onClick: closing dialog");
                getDialog().dismiss();
                break;
            case R.id.tv_action_ok:
                String password = _password.getText().toString();
                getDialog().dismiss();
                MyCards myCards = (MyCards) getActivity();
                myCards.start(password);

                break;
        }

    }

}
