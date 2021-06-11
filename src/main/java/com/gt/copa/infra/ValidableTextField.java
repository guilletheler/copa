package com.gt.copa.infra;

import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;

public class ValidableTextField extends TextField {

    @Getter
    @Setter
    ITextFieldValidator validator;

    public ValidableTextField() {
        super();
        addValidatorListener();
    }

    public ValidableTextField(ITextFieldValidator validator) {
        super();
        this.validator = validator;
        addValidatorListener();
    }

    public ValidableTextField(String content) {
        super(content);
        addValidatorListener();
    }

    public ValidableTextField(ITextFieldValidator validator, String content) {
        super(content);
        this.validator = validator;
        addValidatorListener();
    }

    private void addValidatorListener() {
        textProperty().addListener((observable, oldValue, newValue) -> {
            if (validator != null && !validator.isValid(newValue)) {
                setText(oldValue);
            } else {
                setText(newValue);
            }
        });
    }
}
