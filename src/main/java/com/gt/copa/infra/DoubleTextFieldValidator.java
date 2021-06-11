package com.gt.copa.infra;

public class DoubleTextFieldValidator implements ITextFieldValidator {

    @Override
    public boolean isValid(String text) {
        try {
            Double.valueOf(text);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}