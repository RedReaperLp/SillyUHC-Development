package com.github.redreaperlp.utils.parser;

public class ParsedColor implements ParsedComponent {
    private String code;

    public ParsedColor(String code) {
        this.code = code;
    }

    @Override
    public String getContent() {
        return code;
    }

    public int getColor() {
        return Integer.parseInt(code, 16);
    }
}
