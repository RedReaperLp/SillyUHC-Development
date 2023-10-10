package com.github.redreaperlp.utils.parser;

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
