package org.anhcraft.spaciouslib.effects;

import org.anhcraft.spaciouslib.protocol.Particle;
import org.anhcraft.spaciouslib.utils.VectorUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public abstract class Effect {
    protected Location location;
    protected double angleX;
    protected double angleY;
    protected double angleZ;
    protected List<UUID> viewers;
    protected Particle.Type particleType;
    protected double particleAmount;
    protected boolean particleLongDistance;
    protected int particleCount;
    protected float particleOffsetX;
    protected float particleOffsetY;
    protected float particleOffsetZ;
    protected float particleSpeed;
    protected Material particleMaterial;
    protected int particleData;
    protected Color particleColor;

    /**
     * Creates a new Effect instance
     * @param location a location
     */
    public Effect(Location location){
        this.location = location;
        this.angleX = 0;
        this.angleY = 0;
        this.angleZ = 0;
        this.viewers = new ArrayList<>();
        this.particleType = Particle.Type.CLOUD;
        this.particleAmount = 300;
        this.particleLongDistance = true;
        this.particleCount = 1;
        this.particleOffsetX = 0;
        this.particleOffsetY = 0;
        this.particleOffsetZ = 0;
        this.particleSpeed = 0;
        this.particleMaterial = Material.AIR;
        this.particleData = 0;
        this.particleColor = Color.WHITE;
    }

    /**
     * Gets the location which this effect will spawn at
     * @return the location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the location which this effect will spawn at
     * @param location a location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Gets the angle of rotation in x-axis
     * @return the angle
     */
    public double getAngleX() {
        return angleX;
    }

    /**
     * Sets the angle of rotation in x-axis
     * @param angleX the angle
     */
    public void setAngleX(double angleX) {
        this.angleX = angleX;
    }

    /**
     * Gets the angle of rotation in y-axis
     * @return the angle
     */
    public double getAngleY() {
        return angleY;
    }

    /**
     * Sets the angle of rotation in y-axis
     * @param angleY the angle
     */
    public void setAngleY(double angleY) {
        this.angleY = angleY;
    }

    /**
     * Gets the angle of rotation in z-axis
     * @return the angle
     */
    public double getAngleZ() {
        return angleZ;
    }

    /**
     * Sets the angle of rotation in z-axis
     * @param angleZ the angle
     */
    public void setAngleZ(double angleZ) {
        this.angleZ = angleZ;
    }

    /**
     * Gets all viewers
     * @return a list contains unique ids of viewers
     */
    public List<UUID> getViewers() {
        return viewers;
    }

    /**
     * Sets the viewers who you want to show this effect to
     * @param viewers a list contains unique ids of viewers
     */
    public void setViewers(List<UUID> viewers) {
        this.viewers = viewers;
    }

    /**
     * Adds a viewer who you want to show this effect to
     * @param viewer the unique id of a viewer
     */
    public void addViewer(UUID viewer) {
        this.viewers.add(viewer);
    }

    /**
     * Adds all viewers who are near the spawning location
     * @param radius the radius
     */
    public void addNearbyViewers(double radius) {
        this.viewers.addAll(location.getWorld().getNearbyEntities(location, radius, radius, radius).stream().filter(entity -> entity instanceof Player).map(Entity::getUniqueId).collect(Collectors.toList()));
    }

    /**
     * Removes a player
     * @param viewer the unique id of a player
     */
    public void removeViewer(UUID viewer) {
        this.viewers.remove(viewer);
    }

    /**
     * Gets the particle type of this effect
     * @return the type of particle
     */
    public Particle.Type getParticleType() {
        return particleType;
    }

    /**
     * Sets the particle type for this effect
     * @param particleType the type of particle
     */
    public void setParticleType(Particle.Type particleType) {
        this.particleType = particleType;
    }

    /**
     * Gets the allowed amount of particles
     * @return the amount
     */
    public double getParticleAmount() {
        return particleAmount;
    }

    /**
     * Sets the allowed amount of particles
     * @param particleAmount the amount
     */
    public void setParticleAmount(double particleAmount) {
        this.particleAmount = particleAmount;
    }

    /**
     * Is this particle can be viewed from long distance
     * @return true if yes
     */
    public boolean isParticleLongDistance() {
        return particleLongDistance;
    }

    /**
     * Allows or disallows this particle can be viewed from long distance
     * @param particleDistance true if allows
     */
    public void setParticleLongDistance(boolean particleDistance) {
        this.particleLongDistance = particleDistance;
    }

    /**
     * Gets the particle count
     * @return the particle count
     */
    public int getParticleCount() {
        return particleCount;
    }

    /**
     * Sets the particle count
     * @param particleCount particle count
     */
    public void setParticleCount(int particleCount) {
        this.particleCount = particleCount;
    }

    /**
     * Gets the offset in x-axis
     * @return the offset
     */
    public float getParticleOffsetX() {
        return particleOffsetX;
    }

    /**
     * Sets the offset in x-axis
     * @param particleOffsetX the offset
     */
    public void setParticleOffsetX(float particleOffsetX) {
        this.particleOffsetX = particleOffsetX;
    }

    /**
     * Gets the offset in y-axis
     * @return the offset
     */
    public float getParticleOffsetY() {
        return particleOffsetY;
    }

    /**
     * Sets the offset in y-axis
     * @param particleOffsetY the offset
     */
    public void setParticleOffsetY(float particleOffsetY) {
        this.particleOffsetY = particleOffsetY;
    }

    /**
     * Gets the offset in z-axis
     * @return the offset
     */
    public float getParticleOffsetZ() {
        return particleOffsetZ;
    }

    /**
     * Sets the offset in z-axis
     * @param particleOffsetZ the offset
     */
    public void setParticleOffsetZ(float particleOffsetZ) {
        this.particleOffsetZ = particleOffsetZ;
    }

    /**
     * Gets 'the speed' of the particle
     * @return 'the speed'
     */
    public float getParticleSpeed() {
        return particleSpeed;
    }

    /**
     * Sets 'the speed' for the particle
     * @param particleSpeed 'the speed'
     */
    public void setParticleSpeed(float particleSpeed) {
        this.particleSpeed = particleSpeed;
    }

    /**
     * Gets the material of the particle
     * @return the material
     */
    public Material getParticleMaterial() {
        return particleMaterial;
    }

    /**
     * Sets the material for the particle
     * @param particleMaterial the material
     */
    public void setParticleMaterial(Material particleMaterial) {
        this.particleMaterial = particleMaterial;
    }

    /**
     * Gets the data of the particle
     * @return the data
     */
    public int getParticleData() {
        return particleData;
    }

    /**
     * Sets the data for the particle
     * @param particleData the data
     */
    public void setParticleData(int particleData) {
        this.particleData = particleData;
    }

    /**
     * Gets the color of this particle
     * @return the color
     */
    public Color getParticleColor() {
        return particleColor;
    }

    /**
     * Sets the color for this particle
     * @param particleColor the color
     */
    public void setParticleColor(Color particleColor) {
        this.particleColor = particleColor;
        particleOffsetX = (float) particleColor.getRed() / 255;
        particleOffsetY = (float) particleColor.getGreen() / 255;
        particleOffsetZ = (float) particleColor.getBlue() / 255;

        if (particleOffsetX < 0) {
            particleOffsetX = 0;
        }
        if (particleOffsetY < 0) {
            particleOffsetY = 0;
        }
        if (particleOffsetZ < 0) {
            particleOffsetZ = 0;
        }

        setParticleCount(0);
        setParticleSpeed(1);
        setParticleType(Particle.Type.SPELL_MOB);
    }

    /**
     * Spawns this effect
     */
    public abstract void spawn();

    protected double safeDivide(double dividend, double divisor){
        if(dividend != 0){
            return dividend / divisor;
        }
        return 0;
    }

    protected void spawnParticle(Location loc){
        Particle.create(particleType, loc, particleCount, particleOffsetX, particleOffsetY, particleOffsetZ, particleLongDistance, particleSpeed, particleMaterial, particleData).sendPlayers(viewers.stream().map(uuid -> Bukkit.getServer().getPlayer(uuid)).collect(Collectors.toList()));
    }

    protected void spawnParticle(Location loc, Color color){
        Particle.create(particleType, loc, color).sendPlayers(viewers.stream().map(uuid -> Bukkit.getServer().getPlayer(uuid)).collect(Collectors.toList()));
    }

    protected Vector rotate(Vector vec){
        return VectorUtils.rotateAroundAxisZ(VectorUtils.rotateAroundAxisY(VectorUtils.rotateAroundAxisX(vec, angleX), angleY), angleZ);
    }
}