package com.gt.copa.infra;

public class DoubleTextFieldValidator implements ITextFieldValidator {

    @Override
    public boolean isValid(String text) {
        if (text == null || text.isEmpty() || text.equals("-") || text.equals(".") || text.equals("-.")) {
            return true;
        }
        try {
            Double.valueOf(text);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}