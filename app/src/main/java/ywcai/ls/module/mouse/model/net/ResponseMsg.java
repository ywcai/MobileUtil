package ywcai.ls.module.mouse.model.net;
import org.json.JSONException;
import org.json.JSONObject;

import ywcai.ls.module.mouse.lsenum.ByteType;
import ywcai.ls.module.mouse.lsenum.JsonHead;


public class ResponseMsg {
    private int dataType;
    private byte[] payLoad;
    public ResponseMsg(byte[] msg)
    {
        analyzeMsg(msg);
    }
    private void analyzeMsg(byte[] msg)
    {
        dataType=msg[0];
        payLoad = new byte[msg.length - 1];
        System.arraycopy(msg, 1, payLoad, 0, payLoad.length);
    }
    public byte[] getResponseByte()
    {
        if(dataType!= ByteType.BYTE_BYTE.getHead())
        {
            return null;
        }
        else
        {
            return payLoad;
        }
    }
    public String getResponseType() {
        String responseType="UNKNOW";
        JSONObject jsonObject=getJsonObject();
        if(jsonObject==null)
        {
            return responseType;
        }
        else
        {
            try {
                responseType= jsonObject.getString(JsonHead.JSON_TYPE.getStr());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return responseType ;
        }
    }
    public String getResponseContent() {
        String responseContent="UNKNOW";
        JSONObject jsonObject=getJsonObject();
        if(jsonObject==null)
        {
            return responseContent;
        }
        else
        {
            try {
                responseContent= jsonObject.getString(JsonHead.JSON_CONTENT.getStr());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return responseContent ;
        }
    }
    private JSONObject getJsonObject()
    {
        if(dataType!= ByteType.BYTE_JSON.getHead()) {
            return null;
        }
        JSONObject jsonObject ;
        try {
            String res = new String(payLoad,"utf-8");
            jsonObject = new JSONObject(res);
        } catch (Exception e) {
            jsonObject=null;
            e.printStackTrace();
        }
        return jsonObject;
    }
}
