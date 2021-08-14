package com.coolspy3.commandviewer;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("commandviewer")
public class CommandViewer {
    
    public static final CommandAlterer COMMAND_ALTERER = new CommandAlterer();

    public CommandViewer() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onChatMessageRecieved(ClientChatReceivedEvent event) {
        if(!CommandAlterer.commandPattern.matcher(event.getMessage().getString()).find()) {
            return;
        }
        event.setCanceled(true);
        Minecraft.getInstance().gui.getChat().addMessage(alterMessage(event.getMessage(), COMMAND_ALTERER));
    }

    public static ITextComponent alterMessage(ITextComponent message, StyledTextAcceptor func) {
        if(message == null) {
            return null;
        }
        if(message instanceof TranslationTextComponent) {
            TranslationTextComponent component = (TranslationTextComponent)message.copy();
            for(int i = 0; i < component.getArgs().length; i++) {
                if(component.getArgs()[i] instanceof ITextComponent) {
                    component.getArgs()[i] = alterMessage((ITextComponent)component.getArgs()[i], func);
                }
            }
            return component;
        }
        ITextComponent component = func.acceptOrCopy(message);
        component.getSiblings().replaceAll(sibling -> alterMessage(sibling, func));
        return component;
    }

}
