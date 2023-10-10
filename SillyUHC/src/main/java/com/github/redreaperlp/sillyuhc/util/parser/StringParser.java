package com.github.redreaperlp.sillyuhc.util.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringParser {
    public static ParsedComponent[] parseString(String input) {
        List<ParsedComponent> components = new ArrayList<>();
        Pattern pattern = Pattern.compile("(%\\w+%|<#[0-9a-fA-F]{2,6}>|[^<>%]+|[<>%#])");

        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String match = matcher.group();
            if (match.length() > 1 && match.startsWith("%") && match.endsWith("%")){
                components.add(new ParsedPlaceholder(match));
            } else if (match.matches("<#[0-9a-fA-F]{2,6}>")) {
                components.add(new ParsedColor(match.substring(2, match.length() - 1)));
            } else {
                components.add(new ParsedText(match));
            }
        }

        return components.toArray(new ParsedComponent[0]);
    }
}
