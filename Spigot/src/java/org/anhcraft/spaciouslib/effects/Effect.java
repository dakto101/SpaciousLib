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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getAngleX() {
        return angleX;
    }

    public void setAngleX(double angleX) {
        this.angleX = angleX;
    }

    public double getAngleY() {
        return angleY;
    }

    public void setAngleY(double angleY) {
        this.angleY = angleY;
    }

    public double getAngleZ() {
        return angleZ;
    }

    public void setAngleZ(double angleZ) {
        this.angleZ = angleZ;
    }

    public List<UUID> getViewers() {
        return viewers;
    }

    public void setViewers(List<UUID> viewers) {
        this.viewers = viewers;
    }

    public void addViewer(UUID viewer) {
        this.viewers.add(viewer);
    }

    public void addNearbyViewers(double radius) {
        for(Entity entity : location.getWorld().getNearbyEntities(location, radius, radius, radius).stream().filter(entity -> entity instanceof Player).collect(Collectors.toList())){
            addViewer(entity.getUniqueId());
        }
    }

    public void removeViewer(UUID viewer) {
        this.viewers.remove(viewer);
    }

    public Particle.Type getParticleType() {
        return particleType;
    }

    public void setParticleType(Particle.Type particleType) {
        this.particleType = particleType;
    }

    public double getParticleAmount() {
        return particleAmount;
    }

    public void setParticleAmount(double particleAmount) {
        this.particleAmount = particleAmount;
    }

    public boolean isParticleLongDistance() {
        return particleLongDistance;
    }

    public void setParticleLongDistance(boolean particleDistance) {
        this.particleLongDistance = particleDistance;
    }

    public int getParticleCount() {
        return particleCount;
    }

    public void setParticleCount(int particleCount) {
        this.particleCount = particleCount;
    }

    public float getParticleOffsetX() {
        return particleOffsetX;
    }

    public void setParticleOffsetX(float particleOffsetX) {
        this.particleOffsetX = particleOffsetX;
    }

    public float getParticleOffsetY() {
        return particleOffsetY;
    }

    public void setParticleOffsetY(float particleOffsetY) {
        this.particleOffsetY = particleOffsetY;
    }

    public float getParticleOffsetZ() {
        return particleOffsetZ;
    }

    public void setParticleOffsetZ(float particleOffsetZ) {
        this.particleOffsetZ = particleOffsetZ;
    }

    public float getParticleSpeed() {
        return particleSpeed;
    }

    public void setParticleSpeed(float particleSpeed) {
        this.particleSpeed = particleSpeed;
    }

    public Material getParticleMaterial() {
        return particleMaterial;
    }

    public void setParticleMaterial(Material particleMaterial) {
        this.particleMaterial = particleMaterial;
    }

    public int getParticleData() {
        return particleData;
    }

    public void setParticleData(int particleData) {
        this.particleData = particleData;
    }

    public Color getParticleColor() {
        return particleColor;
    }

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

    public abstract void spawn();

    protected double safeDivide(double dividend, double divisor){
        if(dividend != 0){
            return dividend / divisor;
        }
        return 0;
    }

    protected void spawnParticle(Location loc){
        List<Player> players = new ArrayList<>();
        for(UUID uuid : viewers){
            players.add(Bukkit.getServer().getPlayer(uuid));
        }
        Particle.create(particleType, loc, particleCount, particleOffsetX, particleOffsetY, particleOffsetZ, particleLongDistance, particleSpeed, particleMaterial, particleData).sendPlayers(players);
    }

    protected void spawnParticle(Location loc, Color color){
        List<Player> players = new ArrayList<>();
        for(UUID uuid : viewers){
            players.add(Bukkit.getServer().getPlayer(uuid));
        }
        Particle.create(particleType, loc, color).sendPlayers(players);
    }

    protected Vector rotate(Vector vec){
        return VectorUtils.rotateAroundAxisZ(VectorUtils.rotateAroundAxisY(VectorUtils.rotateAroundAxisX(vec, angleX), angleY), angleZ);
    }
}