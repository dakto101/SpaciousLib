package org.anhcraft.spaciouslib.builders;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import org.anhcraft.spaciouslib.utils.Chat;
import org.anhcraft.spaciouslib.utils.GameVersion;

import java.util.LinkedHashMap;
import java.util.Map;

public class ChatComponentBuilder {
    private BaseComponent component;
    private ClickEvent clickEvent;
    private HoverEvent hoverEvent;
    private String insertion;
    private LinkedHashMap<Object, Object[]> extra = new LinkedHashMap<>();
    private ChatColor color = ChatColor.WHITE;
    private boolean bold = false;
    private boolean obfuscate = false;
    private boolean underline = false;
    private boolean italic = false;
    private boolean strikethrough = false;

    public ChatComponentBuilder(String text){
        this.component = new TextComponent(TextComponent.fromLegacyText(Chat.color(text)));
    }

    public ChatComponentBuilder(String text, Class<? extends BaseComponent> clazz){
        text = Chat.color(text);
        if(clazz.equals(TextComponent.class)){
            component = new TextComponent(TextComponent.fromLegacyText(text));
        }else if(clazz.equals(TranslatableComponent.class)){
            component = new TranslatableComponent(text);
        }else if(GameVersion.is1_12Above()) {
            if(clazz.equals(KeybindComponent.class)){
                component = new KeybindComponent(text);
            }else if(clazz.equals(ScoreComponent.class)){
                component = new ScoreComponent(text, "");
            }else if(clazz.equals(SelectorComponent.class)){
                component = new SelectorComponent(text);
            }
        }
    }

    public ChatComponentBuilder(Class<? extends BaseComponent> clazz){
        if(clazz.equals(TextComponent.class)){
            component = new TextComponent();
        }else if(clazz.equals(TranslatableComponent.class)){
            component = new TranslatableComponent();
        }else if(GameVersion.is1_12Above()) {
            if(clazz.equals(KeybindComponent.class)) {
                component = new KeybindComponent();
            } else if(clazz.equals(ScoreComponent.class)) {
                component = new ScoreComponent("", "");
            } else if(clazz.equals(SelectorComponent.class)) {
                component = new SelectorComponent("");
            }
        }
    }

    public ChatComponentBuilder(BaseComponent component){
        this.component = component;
    }

    public ChatComponentBuilder text(String text, Object... events){
        extra.put(Chat.color(text), events);
        return this;
    }

    public ChatComponentBuilder component(BaseComponent component){
        extra.put(component, new Object[]{});
        return this;
    }

    public ChatComponentBuilder color(ChatColor color){
        this.color = color;
        return this;
    }

    public ChatComponentBuilder event(ClickEvent clickEvent){
        this.clickEvent = clickEvent;
        return this;
    }

    public ChatComponentBuilder event(HoverEvent hoverEvent){
        this.hoverEvent = hoverEvent;
        return this;
    }

    public ChatComponentBuilder bold(){
        this.bold = !this.bold;
        return this;
    }

    public ChatComponentBuilder italic(){
        this.italic = !this.italic;
        return this;
    }

    public ChatComponentBuilder underline(){
        this.underline = !this.underline;
        return this;
    }

    public ChatComponentBuilder strikethrough(){
        this.strikethrough = !this.strikethrough;
        return this;
    }

    public ChatComponentBuilder obfuscate(){
        this.obfuscate = !this.obfuscate;
        return this;
    }

    public ChatComponentBuilder insertion(String text){
        this.insertion = text;
        return this;
    }

    public BaseComponent build(){
        if(clickEvent != null){
            component.setClickEvent(clickEvent);
        }
        if(hoverEvent != null){
            component.setHoverEvent(hoverEvent);
        }
        component.setColor(color);
        component.setBold(bold);
        component.setObfuscated(obfuscate);
        component.setItalic(italic);
        component.setStrikethrough(strikethrough);
        component.setUnderlined(underline);
        if(insertion != null){
            component.setInsertion(insertion);
        }
        for(Map.Entry<Object, Object[]> entry : extra.entrySet()){
            if(entry.getKey() instanceof String){
                ChatComponentBuilder cb = new ChatComponentBuilder((String) entry.getKey(), TextComponent.class);
                for(Object obj : entry.getValue()){
                    if(obj instanceof ClickEvent) {
                        cb.event((ClickEvent) obj);
                    }
                    else if(obj instanceof HoverEvent) {
                        cb.event((HoverEvent) obj);
                    }
                }
                component.addExtra(cb.build());
            }
            else if(entry.getKey() instanceof BaseComponent){
                ChatComponentBuilder cb = new ChatComponentBuilder((BaseComponent) entry.getKey());
                for(Object obj : entry.getValue()){
                    if(obj instanceof ClickEvent) {
                        cb.event((ClickEvent) obj);
                    }
                    else if(obj instanceof HoverEvent) {
                        cb.event((HoverEvent) obj);
                    }
                }
                component.addExtra(cb.build());
            }
        }
        return component;
    }
}
