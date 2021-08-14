package com.coolspy3.commandviewer;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

import net.minecraft.util.text.Color;
import net.minecraft.util.text.TextFormatting;

public class ColorWheel implements Iterator<Color> {
    
    private static final Color[] COLORS = Stream.of(
        "YELLOW, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE, GOLD, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE"
        .split(", "))
        .map(TextFormatting::valueOf)
        .map(Color::fromLegacyFormat)
        .toArray(Color[]::new);

    public static final Color[] getColors() {
        return Arrays.copyOf(COLORS, COLORS.length);
    }

    private int i = 0;

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Color next() {
        if(i == COLORS.length) {
            i = 0;
        }
        return COLORS[i++];
    }

    public void reset() {
        i = 0;
    }

}
