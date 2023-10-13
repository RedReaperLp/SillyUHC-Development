package com.github.redreaperlp.utils.parser;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringParser {
    public static ParsedComponent[] parseString(String input) {
        List<ParsedComponent> components = new ArrayList<>();
        Pattern pattern = Pattern.compile("(%\\w+%|<#[0-9a-fA-F]{2,6}>|&#[0-9a-fA-F]{6}|[^<>%&]+|.)");

        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            String match = matcher.group();
            if (match.length() > 1 && match.startsWith("%") && match.endsWith("%")){
                components.add(new ParsedPlaceholder(match));
            } else if (match.matches("<#[0-9a-fA-F]{2,6}>")) {
                components.add(new ParsedColor(match.substring(2, match.length() - 1)));
            } else if (match.matches("&#[0-9a-f]]{6}")) {
                components.add(new ParsedColor(match.substring(2)));
            } else if (match.matches("(&#[0-9a-fA-F]{6})")) {
                components.add(new ParsedColor(match.substring(2)));
            } else {
                components.add(new ParsedText(match));
            }
        }

        return components.toArray(new ParsedComponent[0]);
    }

    public static Component parse(String input) {
        ParsedComponent[] components = parseString(input);
        Component component = Component.empty();
        int currentColor = 0xffffff;
        for (ParsedComponent parsedComponent : components) {
            if (parsedComponent instanceof ParsedText text) {
                component = component.append(Component.text(text.getContent(), TextColor.color(currentColor)));
            } else if (parsedComponent instanceof ParsedColor color) {
                currentColor = color.getColor();
            } else if (parsedComponent instanceof ParsedPlaceholder) {
                component = component.append(Component.text("PLACEHOLDER", TextColor.color(currentColor)));
            }
        }
        return component;
    }
}
