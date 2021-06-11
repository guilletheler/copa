package com.gt.copa.infra;

public class IntegerTextFieldValidator implements ITextFieldValidator {

    @Override
    public boolean isValid(String text) {
        try {
            Integer.valueOf(text);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}