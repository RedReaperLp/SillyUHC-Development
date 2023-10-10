package com.github.redreaperlp.sillyuhc.util.parser;

public class ParsedText implements ParsedComponent {
    private String content;

    public ParsedText(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }
}
