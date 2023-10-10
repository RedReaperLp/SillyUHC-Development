package com.github.redreaperlp.sillyuhc.util.parser;

public class ParsedPlaceholder implements ParsedComponent{
    private String content;

    public ParsedPlaceholder(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }
}
