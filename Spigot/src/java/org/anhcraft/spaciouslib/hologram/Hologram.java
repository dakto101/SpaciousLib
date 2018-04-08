package org.anhcraft.spaciouslib.hologram;

import org.anhcraft.spaciouslib.utils.Chat;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a hologram implementation.
 */
public class Hologram {
    private LinkedList<String> lines;
    private String id;
    private Location location;
    private double lineSpacing;
    private List<Player> viewers;

    /**
     * Creates a new Hologram instance
     * @param id the id of the hologram
     * @param location the location of the hologram
     */
    public Hologram(String id, Location location){
        this.lines = new LinkedList<>();
        this.id = id;
        this.location = location;
        this.lineSpacing = 0.25;
        this.viewers = new ArrayList<>();
    }

    /**
     * Creates a new Hologram instance
     * @param id the id of the hologram
     * @param location the location of the hologram
     * @param lineSpacing the spacing between lines
     */
    public Hologram(String id, Location location, double lineSpacing){
        this.lines = new LinkedList<>();
        this.id = id;
        this.location = location;
        this.lineSpacing = lineSpacing;
        this.viewers = new ArrayList<>();
    }

    /**
     * Creates a new Hologram instance
     * @param id the id of the hologram
     * @param location the location of the hologram
     * @param lineSpacing the spacing between lines
     * @param lines the array of lines
     */
    public Hologram(String id, Location location, double lineSpacing, String... lines){
        this.id = id;
        this.location = location;
        this.lineSpacing = lineSpacing;
        this.viewers = new ArrayList<>();
        addLines(lines);
    }

    /**
     * Adds the given viewer
     * @param player the viewer
     */
    public void addViewer(Player player){
        this.viewers.add(player);
    }

    /**
     * Removes the given viewer
     * @param player the viewer
     */
    public void removeViewer(Player player){
        this.viewers.remove(player);
    }

    /**
     * Gets all viewers
     * @return list of viewers
     */
    public List<Player> getViewers(){
        return this.viewers;
    }

    /**
     * Sets the new viewers
     * @param viewers list of viewers
     */
    public void setViewers(List<Player> viewers){
        this.viewers = viewers;
    }

    /**
     * Adds a new line to that hologram
     * @param content the content of the line
     */
    public void addLine(String content){
        this.lines.add(Chat.color(content));
    }

    /**
     * Adds new lines to that holgoram
     * @param content array of line
     */
    public void addLines(String... content){
        for(String cont : content){
            addLine(cont);
        }
    }

    /**
     * Removes a specific line of that hologram
     * @param index the index of the line
     */
    public void removeLine(int index){
        this.lines.remove(index);
    }

    /**
     * Gets the lines amount of that hologram
     * @return the amount
     */
    public int getLineAmount(){
        return this.lines.size();
    }

    /**
     * Gets the ID of that hologram
     * @return the ID in string
     */
    public String getID(){
        return this.id;
    }

    /**
     * Gets the line spacing of that hologram
     * @return the line spacing
     */
    public double getLineSpacing(){
        return this.lineSpacing;
    }

    /**
     * Gets the location of that hologram
     * @return the Location object
     */
    public Location getLocation(){
        return this.location;
    }

    /**
     * Sets a new location for that hologram
     * @param location the Location object
     */
    public void setLocation(Location location){
        this.location = location;
    }

    /**
     * Gets all lines of that hologram
     * @return a linked list of lines
     */
    public LinkedList<String> getLines(){
        return this.lines;
    }

    /**
     * Sets new lines for that hologram
     * @param lines a linked list of lines
     */
    public void setLines(LinkedList<String> lines){
        this.lines = lines;
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass() == this.getClass()){
            Hologram h = (Hologram) o;
            return new EqualsBuilder()
                    .append(h.id, this.id)
                    .append(h.lines, this.lines)
                    .append(h.location, this.location)
                    .append(h.lineSpacing, this.lineSpacing)
                    .append(h.viewers, this.viewers)
                    .build();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(9, 16)
                .append(this.id).append(this.lineSpacing).append(this.viewers)
                .append(this.lines).append(this.location).toHashCode();
    }
}
