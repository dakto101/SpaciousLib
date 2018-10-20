package org.anhcraft.spaciouslib.mojang;

import org.anhcraft.spaciouslib.annotations.DataField;
import org.anhcraft.spaciouslib.annotations.Serializable;
import org.anhcraft.spaciouslib.builders.EqualsBuilder;
import org.anhcraft.spaciouslib.builders.HashCodeBuilder;

import java.util.UUID;

/**
 * Represents a player skin implementation.
 */
@Serializable
public class Skin {
    /**
     * Represents the default player skin.
     */
    public enum Default{
        // Steve
        STEVE("8667ba71-b85a-4004-af54-457a9734eed7"),
        // MHF_Alex
        ALEX("6ab43178-89fd-4905-97f6-0f67d9d76fd9");

        private UUID uuid;

        Default(String uuid) {
            this.uuid = UUID.fromString(uuid);
        }

        public Skin getSkin(){
            try {
                return SkinAPI.getSkin(this.uuid).getSkin();
            } catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @DataField
    private String value;
    @DataField
    private String signature;

    /**
     * Not recommended constructor, only be used during serialization processes
     */
    public Skin(){}

    /**
     * Creates a new Skin instance
     * @param value the value of the skin
     * @param signature the signature of the skin
     */
    public Skin(String value, String signature){
        this.value = value;
        this.signature = signature;
    }

    public String getValue() {
        return value;
    }

    public String getSignature() {
        return signature;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public boolean equals(Object o){
        if(o != null && o.getClass() == this.getClass()){
            Skin s = (Skin) o;
            return new EqualsBuilder()
                    .append(s.signature, this.signature)
                    .append(s.value, this.value)
                    .build();
        }
        return false;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder(15, 43)
                .append(this.signature)
                .append(this.value).build();
    }
}