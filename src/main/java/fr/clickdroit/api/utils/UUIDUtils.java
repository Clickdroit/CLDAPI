package fr.clickdroit.api.utils;

import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class UUIDUtils {
    public static String getUuid(String name) {
        String url = "https://api.mojang.com/users/profiles/minecraft/" + name;
        try {
            String UUIDJson = IOUtils.toString(new URL(url));
            if (UUIDJson.isEmpty())
                return "invalid name";
            JSONObject UUIDObject = (JSONObject)JSONValue.parseWithException(UUIDJson);
            return UUIDObject.get("id").toString();
        } catch (IOException|org.json.simple.parser.ParseException e) {
            e.printStackTrace();
            return "error";
        }
    }

    public static String getName(String uuid) {
        String url = "https://api.mojang.com/user/profiles/" + uuid.replace("-", "") + "/names";
        try {
            String nameJson = IOUtils.toString(new URL(url));
            JSONArray nameValue = (JSONArray)JSONValue.parseWithException(nameJson);
            String playerSlot = nameValue.get(nameValue.size() - 1).toString();
            JSONObject nameObject = (JSONObject)JSONValue.parseWithException(playerSlot);
            return nameObject.get("name").toString();
        } catch (IOException|org.json.simple.parser.ParseException e) {
            e.printStackTrace();
            return "error";
        }
    }
}
