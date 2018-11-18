package org.anhcraft.spaciouslib.builders.command;

import net.md_5.bungee.BungeeCord;
import org.anhcraft.spaciouslib.utils.RegEx;

import java.util.function.Predicate;

public enum ArgumentType {
    /**
     * The players can type anything they want
     */
    ANYTHING(s -> true, ""),
    URL(RegEx.URL, "&cYou must type a valid URL! (e.g: https://example.com/)"),
    EMAIL(RegEx.EMAIL, "&cYou must type a valid email address! (e.g: email@website.com)"),
    ONLINE_PLAYER(player -> BungeeCord.getInstance().getPlayer(player) != null, "&cThat player isn't online!"),
    INTEGER(RegEx.INTEGER, "&cYou must type a valid integer! (e.g: 1, 5, 10, -3, etc)"),
    REAL_NUMBER(RegEx.REAL_NUMBER, "&cYou must type a valid real number! (e.g: 0.1, 5, -3.2, 49.0, etc)"),
    BOOLEAN(s -> s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false"), "&cYou must type a valid boolean! (true, false)"),
    SERVER(s -> BungeeCord.getInstance().getServers().containsKey(s), "&cCouldn't find that server!"),
    UUID(RegEx.UUID, "&cYou must type a valid UUID!"),
    NEGATIVE_INTEGER(RegEx.NEGATIVE_INTEGER, "&cYou must type a negative integer!"),
    NEGATIVE_REAL_NUMBER(RegEx.NEGATIVE_REAL_NUMBER, "&cYou must type a negative real number!"),
    POSITIVE_INTEGER(RegEx.POSITIVE_INTEGER, "&cYou must type a positive integer!"),
    POSITIVE_REAL_NUMBER(RegEx.POSITIVE_REAL_NUMBER, "&cYou must type a positive real number!"),
    IP_V4(RegEx.IP_V4, "&cYou must type a valid valid IP v4!");

    private Predicate<String> validator;
    private String errorMessage;

    ArgumentType(Predicate<String> validator, String errorMessage) {
        this.validator = validator;
        this.errorMessage = errorMessage;
    }

    ArgumentType(RegEx validator, String errorMessage) {
        this.validator = validator::matches;
        this.errorMessage = errorMessage;
    }

    public boolean check(String str){
        return validator.test(str);
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
