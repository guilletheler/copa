package com.gt.copa.infra;

public class IntegerTextFieldValidator implements ITextFieldValidator {

    @Override
    public boolean isValid(String text) {
        if(text == null || text.isEmpty()) {
            return true;
        }
        try {
            Integer.valueOf(text);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}