package com.gzeinnumer.androidformvalidation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.gzeinnumer.afv.Validator;
import com.gzeinnumer.afv.constant.TypeForm;
import com.gzeinnumer.afv.model.FormInput;
import com.gzeinnumer.afv.model.Rule;

public class NotRealTimeActivity extends AppCompatActivity {
    TextInputEditText formNama, formAlamat, formNim,
            formJurusan, formEmail, formUmur, formNoHp;
    TextInputLayout formNamaParent, formAlamatParent, formNimParent,
            formJurusanParent, formEmailParent, formUmurParent, formNoHpParent;

    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_real_time);

        formNama = findViewById(R.id.form_nama);
        formNamaParent = findViewById(R.id.form_nama_p);
        formAlamat = findViewById(R.id.form_alamat);
        formAlamatParent = findViewById(R.id.form_alamat_p);
        formNim = findViewById(R.id.form_nim);
        formNimParent = findViewById(R.id.form_nim_p);
        formJurusan = findViewById(R.id.form_jurusan);
        formJurusanParent = findViewById(R.id.form_jurusan_p);
        formEmail = findViewById(R.id.form_email);
        formEmailParent = findViewById(R.id.form_email_p);
        formUmur = findViewById(R.id.form_umur);
        formUmurParent = findViewById(R.id.form_umur_p);
        formNoHp = findViewById(R.id.form_nohp);
        formNoHpParent = findViewById(R.id.form_nohp_p);

        btnSubmit = findViewById(R.id.submit);

        Validator validator = new Validator();

        validator.addView(formNama);
        validator.addView(
                formAlamat,
                new Rule(TypeForm.TEXT)
        );
        validator.addView(
                new FormInput(formNimParent, formNim)
        );
        validator.addView(
                new FormInput(formJurusanParent, formJurusan),
                new Rule(TypeForm.TEXT_NO_SYMBOL)
        );
        validator.addView(
                new FormInput(formEmailParent, formEmail),
                new Rule(TypeForm.EMAIL, 2)
        );
        validator.addView(
                new FormInput(formUmurParent, formUmur),
                new Rule(TypeForm.NUMBER, 2, "Umur tidak boleh kosong")
        );
        validator.addView(
                new FormInput(formNoHpParent, formNoHp),
                new Rule(TypeForm.TEXT_NO_SYMBOL, 2, "NoHp tidak boleh kosong", "Format NoHp salah", "~@#$%^&*:;<>.,/}{+")
        );

        //validator.removeView(formNoHp);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = validator.validate();
                //true if validate success
                //false if validate failed
                if (result) {
                    Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Not Done", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Log.d(getClass().getSimpleName(), "onCreate: "+validator.getAllEditText().size());
    }
}