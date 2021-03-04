package com.gzeinnumer.androidformvalidation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.gzeinnumer.afv.ValidatorRealTime;
import com.gzeinnumer.afv.constant.TypeForm;
import com.gzeinnumer.afv.helper.ValidatorCallBack;
import com.gzeinnumer.afv.model.FormInput;
import com.gzeinnumer.afv.model.Rule;

public class RealTimeActivity extends AppCompatActivity {


    TextInputEditText formUserName, formPass;
    TextInputLayout formUserNameParent, formPassParent;
    Button btnSubmit, btnValidate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time);

        formUserName = findViewById(R.id.form_username);
        formUserNameParent = findViewById(R.id.form_username_p);

        formPass = findViewById(R.id.form_password);
        formPassParent = findViewById(R.id.form_password_p);

        btnSubmit = findViewById(R.id.submit);
        btnValidate = findViewById(R.id.validate);

        validateData();
    }

    private void validateData() {
        ValidatorRealTime validatorRealTime = new ValidatorRealTime();

        validatorRealTime.addView(
                formUserName,
                new Rule(TypeForm.EMAIL)
        );
        validatorRealTime.addView(
                new FormInput(formPassParent, formPass),
                new Rule(TypeForm.TEXT_NO_SYMBOL, 8, "Minimal 8 karakter", "Format salah", "~@#$%^&*:;<>.,/}{+")
        );
        //validatorRealTime.removeView(formUserName);

        validatorRealTime.setEnableRealtimeMessageError(true);
        validatorRealTime.build();

        validatorRealTime.observer(new ValidatorCallBack() {
            @Override
            public void result(boolean isDone) {
                btnSubmit.setEnabled(isDone);
            }
        });

        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatorRealTime.getResult()) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}