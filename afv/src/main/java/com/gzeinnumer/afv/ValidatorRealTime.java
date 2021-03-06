package com.gzeinnumer.afv;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.gzeinnumer.afv.constant.BaseMessage;
import com.gzeinnumer.afv.constant.TypeForm;
import com.gzeinnumer.afv.helper.ValidatorCallBack;
import com.gzeinnumer.afv.model.FormBase;
import com.gzeinnumer.afv.model.FormInput;
import com.gzeinnumer.afv.model.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorRealTime {

    private static final String TAG = "ValidatorRealTime";
    private final BaseMessage baseMessage;
    private final List<FormBase> views = new ArrayList<>();
    private boolean enableRealtimeMessageError = false;

    public ValidatorRealTime() {
        baseMessage = new BaseMessage();
    }

    public void addView(EditText view) {
        views.add(new FormBase(new FormInput(null, view)));
    }

    public void addView(FormInput formInput) {
        views.add(new FormBase(formInput));
    }

    public void addView(FormInput formInput, Rule rules) {
        views.add(new FormBase(formInput, rules));
    }

    public void addView(EditText formInput, Rule rules) {
        views.add(new FormBase(formInput, rules));
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

    public void removeView(EditText view) {
        List<EditText> list = new ArrayList<>();
        for (int i = 0; i < views.size(); i++) {
            list.add(views.get(i).getFormInput().getEditText());
        }
        int index = list.indexOf(view);
        views.remove(index);
    }

    public void setEnableRealtimeMessageError(boolean enableRealtimeMessageError) {
        this.enableRealtimeMessageError = enableRealtimeMessageError;
    }

    private void goneError(TextInputLayout parent) {
        if (parent != null) {
            parent.setError(null);
            parent.setErrorEnabled(false);
        }
    }

    private void changeValue(int finalI, boolean b) {
        views.get(finalI).setDone(b);
    }

    private void viewError(TextInputLayout parent, EditText ed, String msg) {
        if (parent != null)
            parent.setError(msg);
        else
            ed.setError(msg);
    }

    private boolean isValidEmail(String target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean isValidNumber(String target) {
        for (char c : target.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    private boolean isValidPhone(String number) {
        return Patterns.PHONE.matcher(number).matches();
    }

    public void build() {
        String errorEmpty;
        String errorFormat;
        int minLength;
        String permitedSymbol;

        for (int i = 0; i < views.size(); i++) {
            FormBase view = views.get(i);
            EditText ed = view.getFormInput().getEditText();
            TextInputLayout parent = view.getFormInput().getParent();

            minLength = view.getRule().getMinLength();
            permitedSymbol = view.getRule().getPermitedSymbol();
            errorEmpty = (view.getRule().getErrorEmpty() != null) ? view.getRule().getErrorEmpty() : baseMessage.getEmpty();
            errorFormat = (view.getRule().getErrorFormat() != null) ? view.getRule().getErrorFormat() : baseMessage.getFormat();

            int finalMinLength = minLength;
            String finalErrorEmpty = errorEmpty;
            String finalErrorFormat = errorFormat;
            int finalI = i;

            if (enableRealtimeMessageError) {
                ed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        if (!hasFocus) {
                            if (ed.getText().toString().length() == 0)
                                viewError(parent, ed, finalErrorEmpty);
                        }

                    }
                });
            }
            String finalPermitedSymbol = permitedSymbol;
            ed.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String text = ed.getText().toString();
                    if (text.startsWith(" ")) {
                        ed.setText(text.trim());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (ed.getText().toString().length() == 0) {
                        viewError(parent, ed, finalErrorEmpty);
                    }
                    if (view.getRule().getTypeForm() == TypeForm.EMAIL) {
                        if (!isValidEmail(ed.getText().toString())){
                            viewError(parent, ed, finalErrorFormat);
                            changeValue(finalI,false);
                        } else {
                            goneError(parent);
                            changeValue(finalI,true);
                        }
                    } else if (view.getRule().getTypeForm() == TypeForm.NUMBER) {
                        if (!isValidNumber(ed.getText().toString())) {
                            viewError(parent, ed, finalErrorFormat);
                            changeValue(finalI,false);
                        } else {
                            goneError(parent);
                            changeValue(finalI,true);
                        }
                    } else if (view.getRule().getTypeForm() == TypeForm.PHONE) {
                        if (!isValidPhone(ed.getText().toString())) {
                            viewError(parent, ed, finalErrorFormat);
                            changeValue(finalI,false);
                        } else {
                            goneError(parent);
                            changeValue(finalI,true);
                        }
                    } else if (view.getRule().getTypeForm() == TypeForm.TEXT) {
                        if (ed.getText().toString().length() < finalMinLength) {
                            viewError(parent, ed, finalErrorEmpty);
                            changeValue(finalI,false);
                        } else {
                            goneError(parent);
                            changeValue(finalI,true);
                        }
                    } else if (view.getRule().getTypeForm() == TypeForm.TEXT_NO_SYMBOL) {
                        if (ed.getText().toString().length() < finalMinLength) {
                            viewError(parent, ed, finalErrorEmpty);
                            changeValue(finalI,false);
                        } else {
                            if (isValidNoSymbol(ed.getText().toString(), finalPermitedSymbol)) {
                                viewError(parent, ed, finalErrorFormat);
                                changeValue(finalI, false);
                            } else {
                                goneError(parent);
                                changeValue(finalI, true);
                            }
                        }
                    }

                    triggerCallBack();
                }
            });
        }
    }

    private void triggerCallBack() {
        boolean isDone = true;
        for (int i=0; i<views.size(); i++){
            if (!views.get(i).isDone()){
                isDone = false;
                break;
            }
        }
        validatorCallBack.result(isDone);
    }

    public boolean getResult() {
        boolean isDone = true;
        for (int i=0; i<views.size(); i++){
            if (!views.get(i).isDone()){
                isDone = false;
                break;
            }
        }
        return isDone;
    }

    ValidatorCallBack validatorCallBack;

    public void observer(ValidatorCallBack validatorCallBack) {
        this.validatorCallBack = validatorCallBack;
    }
}
