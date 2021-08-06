package com.gzeinnumer.afv;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.gzeinnumer.afv.constant.BaseMessage;
import com.gzeinnumer.afv.constant.TypeForm;
import com.gzeinnumer.afv.helper.RemoveSpaceAtFirst;
import com.gzeinnumer.afv.model.FormBase;
import com.gzeinnumer.afv.model.FormInput;
import com.gzeinnumer.afv.model.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private static final String TAG = "Validator";

    private final BaseMessage baseMessage;

    private final List<FormBase> views = new ArrayList<>();

    public Validator() {
        baseMessage = new BaseMessage();
    }

    public boolean validate() {
        return toValidate(this.views);
    }

    private static boolean isValidNoSymbol(String s, String finalPermitedSymbol) {
        if (s == null || s.trim().isEmpty()) {
            Log.d(TAG, "Incorrect format of string");
            return false;
        }
        Pattern p;
        Matcher m;
        if (finalPermitedSymbol == null) {
            p = Pattern.compile("[^A-Za-z0-9]");
        } else {
            p = Pattern.compile("[^A-Za-z0-9" + finalPermitedSymbol + "]");
        }
        m = p.matcher(s);
        return m.find();
    }

    private static boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private static boolean isValidNumber(String target) {
        for (char c : target.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    private static boolean isValidPhone(String number) {
        return Patterns.PHONE.matcher(number).matches();
    }

    private void viewError(TextInputLayout parent, EditText ed, String msg) {
        if (parent != null)
            parent.setError(msg);
        else
            ed.setError(msg);
    }

    private void goneError(TextInputLayout parent) {
        if (parent != null) {
            parent.setError(null);
            parent.setErrorEnabled(false);
        }
    }

    private boolean toValidate(List<FormBase> views) {
        String errorEmpty;
        String errorFormat;
        int minLength;
        boolean isValidate = true;
        String permitedSymbol;

        for (int i = 0; i < views.size(); i++) {
            FormBase view = views.get(i);
            EditText ed = view.getFormInput().getEditText();
            TextInputLayout parent = view.getFormInput().getParent();

            minLength = view.getRule().getMinLength();
            permitedSymbol = view.getRule().getPermitedSymbol();
            errorEmpty = (view.getRule().getErrorEmpty() != null) ? view.getRule().getErrorEmpty() : baseMessage.getEmpty();
            errorFormat = (view.getRule().getErrorFormat() != null) ? view.getRule().getErrorFormat() : baseMessage.getFormat();

            if (ed.getText().toString().length() == 0) {
                viewError(parent, ed, errorEmpty);
                isValidate = false;
                continue;
            }
            if (view.getRule().getTypeForm() == TypeForm.EMAIL) {
                if (!isValidEmail(ed.getText().toString())) {
                    viewError(parent, ed, errorFormat);
                    isValidate = false;
                }
            } else if (view.getRule().getTypeForm() == TypeForm.NUMBER) {
                if (!isValidNumber(ed.getText().toString())) {
                    viewError(parent, ed, errorFormat);
                    isValidate = false;
                }
            } else if (view.getRule().getTypeForm() == TypeForm.PHONE) {
                if (!isValidPhone(ed.getText().toString())) {
                    viewError(parent, ed, errorFormat);
                    isValidate = false;
                }
            } else if (view.getRule().getTypeForm() == TypeForm.TEXT) {
                if (ed.getText().toString().length() < minLength) {
                    viewError(parent, ed, errorFormat);
                    isValidate = false;
                }
            } else if (view.getRule().getTypeForm() == TypeForm.TEXT_NO_SYMBOL) {
                if (isValidNoSymbol(ed.getText().toString(), permitedSymbol)) {
                    viewError(parent, ed, errorFormat);
                    isValidate = false;
                }
            }
        }
        return isValidate;
    }

    public void addView(EditText view) {
        views.add(new FormBase(new FormInput(null, view)));
        build();
    }

    public void addView(FormInput formInput) {
        views.add(new FormBase(formInput));
        build();
    }

    public void addView(FormInput formInput, Rule rules) {
        views.add(new FormBase(formInput, rules));
        build();
    }

    public void addView(EditText formInput, Rule rules) {
        views.add(new FormBase(formInput, rules));
        build();
    }

    private void build() {
        FormBase formBase = views.get(views.size() - 1);
        EditText ed = formBase.getFormInput().getEditText();
        TextInputLayout parent = formBase.getFormInput().getParent();
        ed.addTextChangedListener(new RemoveSpaceAtFirst(ed, parent));
    }

    public void removeView(EditText view) {
        List<EditText> list = new ArrayList<>();
        for (int i = 0; i < views.size(); i++) {
            list.add(views.get(i).getFormInput().getEditText());
        }
        int index = list.indexOf(view);
        views.remove(index);
    }

    public ArrayList<EditText> getAllEditText(){
        ArrayList<EditText> v = new ArrayList<>();
        for (int i = 0; i < views.size(); i++) {
            v.add(views.get(i).getFormInput().getEditText());
        }
        return v;
    }
}
