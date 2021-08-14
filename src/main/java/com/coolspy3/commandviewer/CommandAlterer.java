package com.coolspy3.commandviewer;

import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class CommandAlterer implements StyledTextAcceptor {

    public static final Pattern commandPattern = Pattern.compile("(?:^ *\\.|[^ ] +\\.?)\\/[^ ]");
    public static final Pattern noSpacePattern = Pattern.compile("(.*[^ ])( *)");

    private final ColorWheel colorWheel = new ColorWheel();

    @Override
    public Optional<ITextComponent> accept(Style style, String content) {
        Matcher commandMatcher = commandPattern.matcher(content);
        if(!acceptStyle(style) || !commandMatcher.find()) {
            return Optional.empty();
        }
        String beforeText = content.substring(0, commandMatcher.start()+1);
        String commandText = content.substring(commandMatcher.start()+1);
        beforeText += commandText.substring(0, commandText.indexOf("/"));
        commandText = commandText.substring(commandText.indexOf("/"));
        ITextComponent comp = new StringTextComponent(beforeText).withStyle(style);

        Matcher noSpaceMatcher = noSpacePattern.matcher(commandText);
        noSpaceMatcher.matches();
        commandText = noSpaceMatcher.group(1);
        StringTextComponent extraSpaces = new StringTextComponent(noSpaceMatcher.group(2));

        ArrayList<String> commandParts = toCommandParts(commandText);
        ArrayList<String> commands = new ArrayList<>(commandParts);
        for(int i = 0; i < commands.size()-1; i++) {
            commands.set(i+1, commands.get(i) + commands.get(i+1));
        }

        colorWheel.reset();
        for(int i = 0; i < commandParts.size(); i++) {
            comp.getSiblings().add(createCommandComponent(commandParts.get(i), commands.get(i)));
        }
        comp.getSiblings().add(extraSpaces);

        return Optional.of(comp);
    }

    public ITextComponent createCommandComponent(String text, String command) {
        Style color = Style.EMPTY.withColor(colorWheel.next());
        return new StringTextComponent(text)
            .withStyle(
                color
                .withUnderlined(true)
                .withHoverEvent(
                    new HoverEvent(HoverEvent.Action.SHOW_TEXT, new StringTextComponent("Run: \"" + command + "\"").withStyle(color))
                )
                .withClickEvent(
                    new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)
                )
            );
    }

    public static ArrayList<String> toCommandParts(String commandText) {
        ArrayList<String> parts = new ArrayList<>();

        boolean hasFoundNoSpace = false;
        String part = "";
        for(char c: commandText.toCharArray()) {
            if(c == ' ') {
                if(hasFoundNoSpace) {
                    parts.add(part);
                    hasFoundNoSpace = false;
                    part = " ";
                    continue;
                }
            } else {
                hasFoundNoSpace = true;
            }
            part += c;
        }
        parts.add(part);

        return parts;
    }

    public static boolean acceptStyle(Style style) {
        return style.getClickEvent() == null && style.getHoverEvent() == null;
    }

}
