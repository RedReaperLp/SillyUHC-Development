package com.github.redreaperlp.sillyuhc.util.parser;

public class ParsedColor implements ParsedComponent {
    private String code;

    public ParsedColor(String code) {
        this.code = code;
    }

    @Override
    public String getContent() {
        return code;
    }
}
