package org.anhcraft.spaciouslib.mojang;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.anhcraft.spaciouslib.utils.CommonUtils;
import org.anhcraft.spaciouslib.utils.Group;
import org.apache.commons.io.IOUtils;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * A class contains useful Mojang APIs
 */
public class MojangAPI {
    /**
     * Gets the current unique id of the given player username
     * @param user the username of the player
     * @return the unique id of that player
     */
    public static Group<String, UUID> getUUID(String user) throws Exception {
        String url = "https://api.mojang.com/users/profiles/minecraft/"+user;
        String json = IOUtils.toString(new URL(url), StandardCharsets.UTF_8);
        JsonObject obj = new Gson().fromJson(json, new TypeToken<JsonObject>(){}.getType());
        return new Group<>(obj.get("name").getAsString(),
                CommonUtils.getUUIDWithoutDashes(obj.get("id").getAsString()));
    }

    /**
     * Gets the unique id of the given player username at a specified time.<br>
     * The time must be the UNIX timestamp (milliseconds/1000)
     * @param user the username of the player
     * @return the unique id of that player at that time
     */
    public static Group<String, UUID> getUUID(String user, long timestamp) throws Exception {
        String url = "https://api.mojang.com/users/profiles/minecraft/"+user
                +"/?at="+Long.toString(timestamp);
        String json = IOUtils.toString(new URL(url), StandardCharsets.UTF_8);
        JsonObject obj = new Gson().fromJson(json, new TypeToken<JsonObject>(){}.getType());
        return new Group<>(obj.get("name").getAsString(),
                CommonUtils.getUUIDWithoutDashes(obj.get("id").getAsString()));
    }

    /**
     * Gets the skin data of the given player unique id.<br>
     * Warning: The limit request is 1 request/minute<br>
     * <b>Please use SkinAPI instead</b>
     * @param player the unique id of the player
     * @return a group of two string objects. The first is the skin value, and the second is the skin signature
     */
    public static Group<String, String> getSkin(UUID player) throws Exception {
        String url = "https://sessionserver.mojang.com/session/minecraft/profile/"+player.toString().replace("-", "")+"?unsigned=false";
        String json = IOUtils.toString(new URL(url), StandardCharsets.UTF_8);
        JsonObject obj = new Gson().fromJson(json, new TypeToken<JsonObject>(){}.getType());
        if(obj.has("properties")){
            JsonArray properties = obj.get("properties").getAsJsonArray();
            if(0 < properties.size()){
                JsonObject property = properties.get(0).getAsJsonObject();
                if(property.get("name").getAsString().equals("textures")){
                    return new Group<>(property.get("value").getAsString(),
                            property.get("signature").getAsString());
                }
            }
        }
        return null;
    }
}
