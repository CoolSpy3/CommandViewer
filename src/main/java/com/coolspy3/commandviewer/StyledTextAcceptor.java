package com.coolspy3.commandviewer;

import java.util.Optional;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;

public interface StyledTextAcceptor extends ITextProperties.IStyledTextAcceptor<ITextComponent> {

    public default Optional<ITextComponent> accept(ITextComponent comp) {
        return accept(comp.getStyle(), comp.getContents());
    }

    public default ITextComponent acceptOrCopy(ITextComponent comp) {
        return accept(comp.getStyle(), comp.getContents()).orElse(comp.copy());
    }

}