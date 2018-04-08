package org.anhcraft.spaciouslib.mojang;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a player skin implementation.
 */
public class Skin {
    /**
     * Represents the defualt player skin.
     */
    public enum Default{
        // https://mineskin.org/16928
        STEVE(new Skin("eyJ0aW1lc3RhbXAiOjE0OTkyNzkxNDAxNjQsInByb2ZpbGVJZCI6Ijg2NjdiYTcxYjg1YTQwMDRhZjU0NDU3YTk3MzR" +
                "lZWQ3IiwicHJvZmlsZU5hbWUiOiJTdGV2ZSIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJ" +
                "TS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDU2ZWVjMWMyMTY5Y" +
                "zhjNjBhN2FlNDM2YWJjZDJkYzU0MTdkNTZmOGFkZWY4NGYxMTM0M2RjMTE4OGZlMTM4In0sIkNBUEUiOnsidXJsIjoiaHR0cDovL3Rle" +
                "HR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9iNzY3ZDQ4MzI1ZWE1MzI0NTYxNDA2YjhjODJhYmJkNGUyNzU1ZjExMTUzY2Q4NW" +
                "FiMDU0NWNjMiJ9fX0=", "eRUQCLPW+4UgFQPScuWu0EcMqMSlqSzOvbsdlz2FD6tri3Vmc7qZDZbqaveyuyxaKHZajiYlS3LsD2sqejwPQRB2rM" +
                "+mLtv0nroffUAkO9BhT6IZLXRa+Sl0+BrIn37Yc7i6w1EnbKw3z4FH6b39mXUM9Td1MmyXkmqj3wlr4e6lauon9BTNtRUFrF4t2YKkbfXkci7mwz1oDN325aCrX97GfZgutt" +
                "PjsMkmoWG2lR0qiPG9aLd412m2H25NDJmajR0DsszRf5SFD8E9VjOWX3tglFiCErEurRTdQtL7b0ntqM68W6LmAbYhM2cPjEquP" +
                "R/WlPVFx26x3XJIliNh5bJFtL1HKChY7tt3Rvfo5Tt9dXLO+5/oJH8uQE6Qic5G5/FyVGyeXlP+wVW" +
                "Bs4tAFF32c2tkm9KuBdGoTdh7TCxMountcU8GMEF9mMomGfQPjiWIE2hZJ15qko5yP4LMT/Af4n40yZgmouigCt8t" +
                "1kQ/ia0hb38tGS07FFUqPmOpw6MtrjGZSxThH/1FabqGll6oobc31hvGCxOrjkS4mBvsYW7uRwI4aL4H2" +
                "wbbPgo7BvLbclxPkWe5/SgzodP1xUy1df+C325yCWn0q1um+j+PUrlru6KHwqfDkr18wrK5XE8t+q7DECBO9wjjxddy68SgitL1VC0snLr" +
                "q1uUAHj8=")),
        // https://mineskin.org/7474
        ALEX(new Skin("eyJ0aW1lc3RhbXAiOjE0ODczNDY1NTM0NTIsInByb2ZpbGVJZCI6ImIwZDRiMjhiYzFkNzQ4ODlhZjBlOD" + "Y2MWNlZTk2YWFiIiwicHJvZmlsZU5hbWUiOiJZZWxlaGEiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOns" +
                "iU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzhlNDEzYmYwZGRiYjMyNmNj"+"NTZjOTM4MTE4YTgyZmI2Y2QzZTE0ZjRjOGIzZjRkOTE0OWI0MzdjYTQxZGEwOWEifX19", "WMl9k/8zib8REui1/g8uQLZIeFboYlFHiYL3UzTvIqAeoLgUGoOgZ3FV6pNFSIOq95hZFCinN13CEMfCX6+Kw" +
                "t0G3JzhAA5cUD09LUffVvAWT2qv3LM7EtlgqIzjMz4+K/NEDqL6M564AaP" +
                "viemg0qVdKY8kpW8DSGoNMmxLpX9evM0YxqI1SMAT+rqLW4QyGzp0pqu7sUMFc1RMSnSpGlx/pjwoDLQv" +
                "sZQczpdq9NDeuyECD3QNwjR+Gjz6wTiDjvZY7TZDXk6b7M1u0TkLQkjjT8dSUkW99yPuIi9KhXLHUdpKaj" +
                "JPo7b00Z3vfEYvQNl0RR6LN0seG2wY20Zbpu8nU2Vgd1a76VlpVPlVj6k/icyYdquEcHIT28YNv78Ho8Wf3wQHFJEZmInoHaVjZE6PlMduisJ8bWtrQ93aIZup0eRWUldg0CtVDqJmwQywzQ" +
                "oG5/MUw7KYZiGsQPWbOc/Dh+rkTgw5EI15SxyVWJBl6fn4Fv" +
                "x+BE9Js7h440jRM9eH3Tfq2pSYf+Y3Kpa+kbnPHC4x6JsZH2aaCLK6giECR6NFy7UCPj5AxNb8UCXphZaojUcodQs1l" +
                "ZRV0TUz9ssZKQ63t2itJpn/HzelC5eo4EIp0raR4hr6dgpOYOPmvW77E4DYh9wCynNMfdQxkoXrz" +
                "3r26/T1bfYbgk0iz8o="));

        private Skin skin;

        Default(Skin skin) {
            this.skin = skin;
        }

        public Skin getSkin(){
            return this.skin;
        }
    }

    private String value;
    private String signature;

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
        return new HashCodeBuilder(14, 31)
                .append(this.signature)
                .append(this.value).toHashCode();
    }
}