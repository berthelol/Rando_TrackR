package com.example.loic.rando_trackr.Map_data;

/**
 * Created by loic on 16/12/2016.
 */

public class Value_total {
    private String text;
    private int value;
    public Value_total(String text, int value)
    {
        this.text=text;
        this.value=value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
