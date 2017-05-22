package ywcai.ls.module.mouse.model.net;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import ywcai.ls.module.mouse.lsenum.JsonHead;


/**
 * Created by Administrator on 2016-11-25.
 */

public class MakeJson {

    public byte[] getByte(String type, String content) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(JsonHead.JSON_TYPE.getStr(), type);
            jsonObject.put(JsonHead.JSON_CONTENT.getStr(), content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        byte[] jsonData = null;
        try {
            jsonData = (jsonObject.toString()).getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return jsonData;
    }
}
