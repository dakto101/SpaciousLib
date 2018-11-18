package org.anhcraft.spaciouslib.command;

import org.anhcraft.spaciouslib.utils.RegEx;
import org.anhcraft.spaciouslib.utils.Chat;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * A sub-command is one or many static arguments of a command (which created by CommandBuilder)<br>
 * A sub-command (static argument) can have unlimited (dynamic) arguments<br>
 * With a dynamic argument, players can type anything they want but it needs to depend on the type of that argument<br>
 * A argument can be optional or required.<br>
 * E.g: players can only type integer number if the type of that argument which they was typed in is INTEGER_NUMBER
 */
@Deprecated
public class SubCommandBuilder extends CommandString{
    private String name;
    private String description;
    private CommandRunnable rootRunnable;
    private LinkedHashMap<CommandArgument.Type, String> argErrorMessages = new LinkedHashMap<>();
    private LinkedHashMap<CommandArgument, CommandArgument.Type> args = new LinkedHashMap<>();
    private String doesNotEnoughArgsErrorMessage;
    protected String canNotFindCmdErrorMessage;
    protected String suggestionMessage;
    private boolean hideTypeCommandString = true;

    /**
     * Creates a new SubCommandBuilder instance
     * @param name the name of this sub command
     * @param description the description of this sub command
     * @param rootRunnable a runnable which triggers if a player run this sub command
     */
    public SubCommandBuilder(String name, String description, CommandRunnable rootRunnable) throws Exception {
        this.name = name.trim().toLowerCase();
        this.rootRunnable = rootRunnable;
        this.description = description;
        init();
    }

    private void init() {
        setArgErrorMessage(CommandArgument.Type.URL,
                "&cYou must type a valid URL! (e.g: https://example.com/)");
        setArgErrorMessage(CommandArgument.Type.EMAIL,
                "&cYou must type a valid email address! (e.g: email@website.com)");
        setArgErrorMessage(CommandArgument.Type.ONLINE_PLAYER,
                "&cThat player isn't online!");
        setArgErrorMessage(CommandArgument.Type.INTEGER,
                "&cYou must type a valid integer! (e.g: 1, 5, 10, -3, etc)");
        setArgErrorMessage(CommandArgument.Type.REAL_NUMBER,
                "&cYou must type a valid real number! (e.g: 0.1, 5, -3.2, 49.0, etc)");
        setArgErrorMessage(CommandArgument.Type.BOOLEAN,
                "&cYou must type a valid boolean! (true, false)");
        setArgErrorMessage(CommandArgument.Type.WORLD,
                "&cCouldn't find that world!");
        setArgErrorMessage(CommandArgument.Type.UUID,
                "&cYou must type a valid UUID!");
        setArgErrorMessage(CommandArgument.Type.NEGATIVE_INTEGER,
                "&cYou must type a negative integer!");
        setArgErrorMessage(CommandArgument.Type.NEGATIVE_REAL_NUMBER,
                "&cYou must type a negative real number!");
        setArgErrorMessage(CommandArgument.Type.POSITIVE_INTEGER,
                "&cYou must type a positive integer!");
        setArgErrorMessage(CommandArgument.Type.POSITIVE_REAL_NUMBER,
                "&cYou must type a positive real number!");
        setArgErrorMessage(CommandArgument.Type.IP_V4,
                "&cYou must type a valid valid IP v4!");
        setDoesNotEnoughArgsErrorMessage("&cNot enough arguments!");
        setCanNotFindCmdMessage("&cCan't find that command. Please recheck the syntax.");
        setSuggestionMessage("&6Maybe this is the command which you want: &f");
    }

    /**
     * Gets the name of this sub command
     * @return the name
     */
    public String getName(){
        return this.name;
    }

    /**
     * Gets the type of a given argument
     * @param arg the argument
     * @return the type of that argument
     */
    public CommandArgument.Type getArgumentType(CommandArgument arg){
        return this.args.get(arg);
    }

    /**
     * Get all arguments of this sub command
     * @return list of arguments
     */
    public List<CommandArgument> getArguments(){
        return new ArrayList<>(this.args.keySet());
    }

    /**
     * Hides the type of arguments when you get the command string
     * @return this object
     */
    public SubCommandBuilder hideTypeCommandString(){
        this.hideTypeCommandString = true;
        return this;
    }

    /**
     * Gets all arguments of this sub command
     * @param optional true if you just want to get the optional command
     * @return the list of arguments
     */
    public List<CommandArgument> getArguments(boolean optional){
        List<CommandArgument> x = new ArrayList<>();
        for(CommandArgument c : getArguments()){
            if(c.isOptional() == optional){
                x.add(c);
            }
        }
        return x;
    }

    /**
     * Removes an argument out of this sub command
     * @param arg argument object
     * @return this object
     */
    public SubCommandBuilder removeArgument(CommandArgument arg){
        this.args.remove(arg);
        return this;
    }

    /**
     * Creates a new dynamic argument for this sub command
     * @param arg CommandArgument object
     * @param type type of this argument
     * @return this object
     */
    public SubCommandBuilder addArgument(CommandArgument arg, CommandArgument.Type type){
        this.args.put(arg, type);
        return this;
    }

    /**
     * Creates a new dynamic argument for this subcommand
     * @param name the name of this argument
     * @param argRunnable runnable for this argument, only triggers if a player runs the command which has this argument at the end
     * @param type the type of this argument
     * @param optional true if you want the player can skip this argument
     * @return this object
     */
    public SubCommandBuilder addArgument(String name, CommandRunnable argRunnable, CommandArgument.Type type, boolean optional) throws Exception {
        addArgument(new CommandArgument(name, argRunnable, optional), type);
        return this;
    }

    /**
     * A message will sent to the command executor if there's an invalid argument.
     * @param type the type of the argument
     * @param message the message
     * @return this object
     */
    public SubCommandBuilder setArgErrorMessage(CommandArgument.Type type, String message){
        this.argErrorMessages.put(type, Chat.color(message));
        return this;
    }

    /**
     * A message will sent to the command executor if that command doesn't have enough static arguments.
     * @param message the message
     * @return this object
     */
    public SubCommandBuilder setDoesNotEnoughArgsErrorMessage(String message){
        this.doesNotEnoughArgsErrorMessage = Chat.color(message);
        return this;
    }

    /**
     * A message will sent to the command executor if that command can't be found.
     * @param message the message
     * @return this object
     */
    public SubCommandBuilder setCanNotFindCmdMessage(String message){
        this.canNotFindCmdErrorMessage = Chat.color(message);
        return this;
    }

    protected String getCommandString(boolean color){
        StringBuilder cmd;
        if(color){
            cmd = new StringBuilder((color ? gcs(Type.BEGIN_SUB_COMMAND) : "") + this.name);
            for(CommandArgument arg : args.keySet()){
                cmd = cmd.append(" ").append(arg.isOptional() ? gcs(Type.BEGIN_ARGUMENT_OPTIONAL) : gcs(Type.BEGIN_ARGUMENT))
                        .append(arg.isOptional() ? gcs(Type.ARGUMENT_NAME_OPTIONAL) : gcs(Type.ARGUMENT_NAME))
                        .append(arg.getName());
                if(!hideTypeCommandString){
                    cmd = cmd.append(arg.isOptional() ? gcs(Type.ARGUMENT_TYPE_OPTIONAL) : gcs(Type.ARGUMENT_TYPE))
                            .append(args.get(arg).toString());
                }
                cmd = cmd.append(arg.isOptional() ? gcs(Type.END_ARGUMENT_OPTIONAL) : gcs(Type.END_ARGUMENT));
            }
            if(description != null){
                cmd = cmd.append(gcs(Type.DESCRIPTION)).append(" ").append(description);
            }
        } else {
            cmd = new StringBuilder(this.name);
            for(CommandArgument arg : args.keySet()){
                cmd = cmd.append(" ").append(arg.getName());
                if(!hideTypeCommandString){
                    cmd = cmd.append(args.get(arg).toString());
                }
            }
            if(description != null) {
                cmd = cmd.append(" ").append(description);
            }
        }
        return cmd.toString();
    }

    /**
     * Normalizes this sub command<br>
     * It'll sort optional arguments into the end of this sub command. The order between optional arguments or non-optional arguments won't be change.
     * @return this object
     */
    public SubCommandBuilder normalize(){
        LinkedHashMap<CommandArgument, CommandArgument.Type> require = new LinkedHashMap<>();
        LinkedHashMap<CommandArgument, CommandArgument.Type> optional = new LinkedHashMap<>();
        for(CommandArgument c : getArguments()){
            if(c.isOptional()) {
                continue;
            }
            require.put(c, getArgumentType(c));
        }
        for(CommandArgument c : getArguments()){
            if(!c.isOptional()) {
                continue;
            }
            optional.put(c, getArgumentType(c));
        }
        this.args = new LinkedHashMap<>();
        this.args.putAll(require);
        this.args.putAll(optional);
        return this;
    }

    /**
     * Checks is this sub command valid
     * @return this object
     */
    public boolean isValid(){
        int a = 0;
        for(CommandArgument c : getArguments()){
            if(c.isOptional()) {
                a = 1;
            } else {
                if(a == 1){
                  return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass() == this.getClass()){
            SubCommandBuilder c = (SubCommandBuilder) o;
            return new EqualsBuilder()
                    .append(c.description, this.description)
                    .append(c.name, this.name)
                    .append(c.rootRunnable, this.rootRunnable)
                    .append(c.argErrorMessages, this.argErrorMessages)
                    .append(c.args, this.args)
                    .append(c.doesNotEnoughArgsErrorMessage, this.doesNotEnoughArgsErrorMessage)
                    .append(c.canNotFindCmdErrorMessage, this.canNotFindCmdErrorMessage)
                    .append(c.suggestionMessage, this.suggestionMessage)
                    .append(c.hideTypeCommandString, this.hideTypeCommandString)
                    .build();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(15, 47)
                .append(this.description)
                .append(this.name)
                .append(this.rootRunnable)
                .append(this.argErrorMessages)
                .append(this.args)
                .append(this.doesNotEnoughArgsErrorMessage)
                .append(this.canNotFindCmdErrorMessage)
                .append(this.suggestionMessage)
                .append(this.hideTypeCommandString).toHashCode();
    }

    void execute(CommandBuilder cmd, CommandSender s, String[] a) throws Exception {
        if(!isValid()){
            throw new Exception("This sub-command isn't valid, please normalize first");
        }        SubCommandBuilder sc = this;
        // [vi] neu khong co tham so dong
        if(a.length == 0){
            rootRunnable.run(cmd, sc, s, a, "");
        }
        // [vi] neu co thi se check chung
        else {
            LinkedHashMap<CommandArgument, String> values = new LinkedHashMap<>();
            int i = 0;
            // [vi] lap tung tham so dong
            for(String value : a) {
                // [vi] neu tham so dong da nhap khong duoc chi dinh thi bo qua
                if(getArguments().size() <= i) {
                    break;
                }
                // [vi] tung tham so dong cua lenh da nhap se tuong ung vs loai cua tham so chi dinh
                values.put(getArguments().get(i), value);
                i++;
            }
            // [vi] neu tham so dong da nhap ti hon tham so chi dinh thi se tinh la "khong du tham so"
            if(values.size() < getArguments(false).size()) {
                s.sendMessage(sc.doesNotEnoughArgsErrorMessage);
            } else {
                boolean hasError = false;
                argTypeValidator:
                // [vi] lap tung tham so dong
                for(CommandArgument arg : values.keySet()) {
                    String value = values.get(arg);
                    switch(getArgumentType(arg)) {
                        case URL:
                            if(!RegEx.URL.matches(value)) {
                                s.sendMessage(sc.argErrorMessages.get(CommandArgument.Type.URL));
                                hasError = true;
                                break argTypeValidator;
                            }
                            break;
                        case EMAIL:
                            if(!RegEx.EMAIL.matches(value)) {
                                s.sendMessage(sc.argErrorMessages.get(CommandArgument.Type.EMAIL));
                                hasError = true;
                                break argTypeValidator;
                            }
                            break;
                        case BOOLEAN:
                            if(!value.equalsIgnoreCase("true")
                                    && !value.equalsIgnoreCase("false")) {
                                s.sendMessage(sc.argErrorMessages.get(CommandArgument.Type.BOOLEAN));
                                hasError = true;
                                break argTypeValidator;
                            }
                            break;
                        case WORLD:
                            if(Bukkit.getServer().getWorld(value) == null) {
                                s.sendMessage(sc.argErrorMessages.get(CommandArgument.Type.WORLD));
                                hasError = true;
                                break argTypeValidator;
                            }
                            break;
                        case REAL_NUMBER:
                            if(!RegEx.REAL_NUMBER.matches(value)) {
                                s.sendMessage(sc.argErrorMessages.get(CommandArgument.Type.REAL_NUMBER));
                                hasError = true;
                                break argTypeValidator;
                            }
                            break;
                        case INTEGER:
                            if(!RegEx.INTEGER.matches(value)) {
                                s.sendMessage(sc.argErrorMessages.get(CommandArgument.Type.INTEGER));
                                hasError = true;
                                break argTypeValidator;
                            }
                            break;
                        case ONLINE_PLAYER:
                            if(!Bukkit.getServer().getOfflinePlayer(value).isOnline()) {
                                s.sendMessage(sc.argErrorMessages.get(CommandArgument.Type.ONLINE_PLAYER));
                                hasError = true;
                                break argTypeValidator;
                            }
                            break;
                        case UUID:
                            if(!RegEx.UUID.matches(value)) {
                                s.sendMessage(sc.argErrorMessages.get(CommandArgument.Type.UUID));
                                hasError = true;
                                break argTypeValidator;
                            }
                            break;
                        case NEGATIVE_INTEGER:
                            if(!RegEx.NEGATIVE_INTEGER.matches(value)) {
                                s.sendMessage(sc.argErrorMessages.get(CommandArgument.Type.NEGATIVE_INTEGER));
                                hasError = true;
                                break argTypeValidator;
                            }
                            break;
                        case NEGATIVE_REAL_NUMBER:
                            if(!RegEx.NEGATIVE_REAL_NUMBER.matches(value)) {
                                s.sendMessage(sc.argErrorMessages.get(CommandArgument.Type.NEGATIVE_REAL_NUMBER));
                                hasError = true;
                                break argTypeValidator;
                            }
                            break;
                        case POSITIVE_INTEGER:
                            if(!RegEx.POSITIVE_INTEGER.matches(value)) {
                                s.sendMessage(sc.argErrorMessages.get(CommandArgument.Type.POSITIVE_INTEGER));
                                hasError = true;
                                break argTypeValidator;
                            }
                            break;
                        case POSITIVE_REAL_NUMBER:
                            if(!RegEx.POSITIVE_REAL_NUMBER.matches(value)) {
                                s.sendMessage(sc.argErrorMessages.get(CommandArgument.Type.POSITIVE_REAL_NUMBER));
                                hasError = true;
                                break argTypeValidator;
                            }
                            break;
                        case IP_V4:
                            if(!RegEx.POSITIVE_REAL_NUMBER.matches(value)) {
                                s.sendMessage(sc.argErrorMessages.get(CommandArgument.Type.IP_V4));
                                hasError = true;
                                break argTypeValidator;
                            }
                            break;
                    }
                }
                if(!hasError) {
                    // [vi] tham so dong da nhap phai co nhieu hon 0
                    if(values.size() > 0) {
                        // [vi] lay ra tham so dong cuoi cung
                        CommandArgument arg = new ArrayList<>(values.keySet())
                                .get(values.size() - 1);
                        // [vi] chay runnable cua tham so cuoi cung kem gia tri da nhap
                        arg.getRunnable().run(cmd, sc, s, a, values.get(arg));
                    } else {
                        s.sendMessage(sc.canNotFindCmdErrorMessage);
                    }
                }
            }
        }
    }

    /**
     * Sets the suggestion message.<br>
     * The command string (with colors) will put at the end of this message.
     * @param suggestMessage the suggestion message
     */
    public void setSuggestionMessage(String suggestMessage) {
        this.suggestionMessage = suggestMessage;
    }
}
