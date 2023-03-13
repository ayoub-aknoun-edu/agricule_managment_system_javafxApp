package com.app.engineerapp.Login.Verification;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class Format {
    TextField textField ;

    public Format(TextField textField) {
        this.textField = textField;
    }

    // filter only allows digits, and ensures only one digit the text field:
    UnaryOperator<TextFormatter.Change> textFilter = c -> {
        // if text is a single digit, replace current text with it:
        if (c.getText().matches("[0-9]")) {
            c.setRange(0, textField.getText().length());
            return c ;
        } else
            // if not adding any text (delete or selection change), accept as is
            if (c.getText().isEmpty()) {
                return c ;
            }
        return null ;
    };

    // filter only allows digits, and ensures only one digit the text field:
    UnaryOperator<TextFormatter.Change> PriceFilter = c -> {
        // if text is a single digit, replace current text with it:
        if (c.getText().matches("[0-9]")) {
            return c ;
        }else
            // if not adding any text (delete or selection change), accept as is
            if (c.getText().isEmpty()) {
                return c ;
            }
        return null ;
    };

    public TextFormatter format(){
        TextFormatter<Integer> formatter = new TextFormatter<>(textFilter);
        return formatter;
    }

    public TextFormatter formatPrice(){
        TextFormatter<Integer> formatter = new TextFormatter<>(PriceFilter);
        return formatter;
    }
    private void finish(){

    }
}
