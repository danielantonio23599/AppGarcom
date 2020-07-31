package com.daniel.appgarcom.fragment.cadastro;

import android.Manifest;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.daniel.appgarcom.Cadastro2Activity;
import com.daniel.appgarcom.R;
import com.daniel.appgarcom.util.AppSignatureHashHelper;
import com.daniel.appgarcom.util.GeradorNumeros;
import com.daniel.appgarcom.util.MaskEditUtil;
import com.daniel.appgarcom.util.PermissionUtils;
import com.daniel.appgarcom.util.SMSReceiver;
import com.google.android.material.textfield.TextInputLayout;

//import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

public class TelefoneFragment extends Fragment {
    private EditText telefone;
    private TextInputLayout layTelefone;
    public static final String TAG = Cadastro2Activity.class.getSimpleName();
    private String codigo;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    private SMSReceiver smsReceiver;
    AppSignatureHashHelper appSignatureHashHelper;

    public String getTelefone() {
        return telefone.getText() + "";
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro_telefone, container, false);

        telefone = (EditText) view.findViewById(R.id.input_telefone);
        layTelefone = (TextInputLayout) view.findViewById(R.id.layTelefone);
        telefone.addTextChangedListener(MaskEditUtil.mask(telefone, MaskEditUtil.FORMAT_FONE));
        telefone.requestFocus();
        telefone.setSelection(telefone.length());

        appSignatureHashHelper = new AppSignatureHashHelper(getActivity());

        // Este código requer um tempo para obter as chaves Hash, comentar e compartilhar as chaves
        Log.i(TAG, "HashKey: " + appSignatureHashHelper.getAppSignatures().get(0));
        String[] permissoes = new String[]{
                Manifest.permission.SEND_SMS,
                Manifest.permission.BROADCAST_SMS
        };
        PermissionUtils.validate(getActivity(), 0, permissoes);
        return view;
    }

    public void enviarSMS() {
       setCodigo(GeradorNumeros.geraNumeroInterio(5)+"");
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(telefone.getText() + "", null, "<#>Use o Codigo " + getCodigo() + " para confirmar o numero de cadastro " + appSignatureHashHelper.getAppSignatures().get(0), null, null);
    }

    public boolean verificaTelefone() {
        if (telefone.getText().length() < 14) {
            layTelefone.setError("Telefone incorreto (00)90000-0000");
            return false;
        } else {
            layTelefone.setErrorEnabled(false);
            return true;
        }
    }

}
