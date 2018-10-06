package org.anhcraft.spaciouslib.utils;

import org.anhcraft.spaciouslib.builders.EqualsBuilder;
import org.anhcraft.spaciouslib.builders.HashCodeBuilder;
import org.anhcraft.spaciouslib.serialization.DataField;
import org.anhcraft.spaciouslib.serialization.Serializable;

@Serializable
public class Cooldown {
    @DataField
    private long current;

    public Cooldown(){
        this.current = System.currentTimeMillis();
    }

    public Cooldown(long current){
        this.current = current;
    }

    public Cooldown reset(){
        this.current = System.currentTimeMillis();
        return this;
    }

    public boolean isTimeout(double seconds){
        return !((System.currentTimeMillis() - this.current) < (seconds * 1000d));
    }

    public double timeLeft(double seconds){
        return seconds - ((System.currentTimeMillis()-this.current)/1000d);
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass() == this.getClass()){
            Cooldown c = (Cooldown) o;
            return new EqualsBuilder()
                    .append(c.current, this.current)
                    .build();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(15, 43)
                .append(this.current).build();
    }
}
