package com.sorta.fsn.core.security.user;

import java.io.Serializable;
import java.util.Date;

/**
 * 令牌接口，用于用户登录后的重要信息，包括以下内容：
 *
 * <ul>
 *     <li>uid          ：用户的唯一标识</li>
 *     <li>aid          : 用户账号唯一标识</li>
 *     <li>clientId     ：用户登录时所使用终端的唯一标识</li>
 *     <li>clientType   ：用户登录时所使用的终端类别</li>
 *     <li>createdTime  ：Token生成的时间戳</li>
 * </ul>
 *
 * User: Jim
 * Date: 16-8-28
 * Time: 下午10:46
 */
public class Token implements Serializable{
    private static final long serialVersionUID = -6602465878134234541L;

    public enum ClientType{
        UNKNOWN(0),MOBILE(1),BROWSER(2),WECHAT(3),TBOX(4),AVN(5);

        protected int code;
        ClientType(int code){
            this.code = code;
        }
        public int getCode(){
            return code;
        }
        public static ClientType parse(int typeCode){
            ClientType _clientType = UNKNOWN;
            switch (typeCode){
                case 0:_clientType = UNKNOWN;break;
                case 1:_clientType = MOBILE;break;
                case 2:_clientType = BROWSER;break;
                case 3:_clientType = WECHAT;break;
                case 4:_clientType = TBOX;break;
                case 5:_clientType = AVN;break;
            }
            return _clientType;
        }
    }

    protected String uid;
    protected String aid;
    protected String clientId;
    protected ClientType clientType;
    protected long createdTime;
    protected String cipherString;

    public Token(){

    }

    public Token(String uid, String aid, String clientId, ClientType clientType){
        this.uid = uid;
        this.aid = aid;
        this.clientId = clientId;
        this.clientType = clientType;
        createdTime = new Date().getTime();
    }

    public String getAid() {
        return aid;
    }

    public String getUid() {
        return uid;
    }

    public String getClientId() {
        return clientId;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public String toCipherString(){return cipherString;};

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("uid : ").append(uid).append(",").
        append("aid : ").append(aid).append(",").
        append("clientId : ").append(clientId).append(",").
        append("clientType : ").append(clientType.name()).append(",").
        append("create Date :").append(createdTime);
        return sb.toString();
    }
}
