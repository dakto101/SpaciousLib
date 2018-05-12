package org.anhcraft.spaciouslib.effects;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class VortexEffect extends Effect {
    private int vortexLineAmount;
    private double vortexLineLength;

    public VortexEffect(Location location) {
        super(location);
        vortexLineAmount = 3;
        vortexLineLength = 1;
    }

    @Override
    public void spawn() {
        double dis = 360 / vortexLineAmount;
        for(double i = 0; i < 360; i+= dis){
            double rad = Math.toRadians(i);
            double x = Math.cos(rad) * vortexLineLength;
            double z = Math.sin(rad) * vortexLineLength;
            Location loc = location.clone().add(rotate(new Vector(x, 0, z)));
            double max = i + 180;
            double part = safeDivide(360 * (vortexLineAmount/2), particleAmount / vortexLineAmount);
            for(double r = 0; r < 360 * (vortexLineAmount/2); r+= part){
                double radr = Math.toRadians(r);
                double x_ = Math.cos(radr) * vortexLineLength;
                double z_ = Math.sin(radr) * vortexLineLength;
                if(i <= r && r <= max){
                    spawnParticle(loc.clone().add(rotate(new Vector(x_,0, z_))));
                }
            }
        }
    }

    public int getVortexLineAmount() {
        return vortexLineAmount;
    }

    public void setVortexLineAmount(int vortexLineAmount) {
        this.vortexLineAmount = vortexLineAmount;
    }

    public double getVortexLineLength() {
        return vortexLineLength;
    }

    public void setVortexLineLength(double vortexLineLength) {
        this.vortexLineLength = vortexLineLength;
    }
}
