package cn.buk.api.wechat.work.dto;

import cn.buk.api.wechat.dto.WwExternalContact;
import cn.buk.api.wechat.dto.WxData;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 企业微信接受消息的格式
 * 用来保存企业微信推送过来的消息
 */
public class WorkWxData {

  /**
   * 企业微信CorpID
   */
  private String toUserName;

  /**
   * 成员UserID
   */
  private String fromUserName;
  /**
   * 消息创建时间（整型）
   */
  private Long createTime;
  /**
   * 消息类型，此值固定为：text, voice, video, location, event
   */
  private String msgType; //text,image,voice,event
  /**
   * 消息id，64位整型
   */
  private Long msgId;

  private String content; //text: 对应的内容

  /**
   * 语音媒体文件id，可以调用获取媒体文件接口拉取数据，仅三天内有效
   */
  private String mediaId;

  /**
   * 语音格式，如amr，speex等
   */
  private String mediaFormat;


  /**
   * 视频消息缩略图的媒体id，可以调用获取媒体文件接口拉取数据，仅三天内有效
   */
  private String thumbMediaId;


  /**
   * 事件类型:
   * subscribe(关注)
   * unsubscribe(取消关注),
   * enter_agent 进入应用事件
   * LOCATION 地理位置
   * taskcard_click 任务卡片事件推送
   */
  private String event;

  /**
   * taskcard_click事件：与发送任务卡片消息时指定的按钮btn:key值相同
   */
  private String eventKey;

  /**
   * 企业应用的id，整型。可在应用的设置页面查看
   */
  private int agentId; // 应用id
  private String encryptedMsg; // 加密的消息内容

  private String changeType; // 外部联系人 变更类型

  private double latitude; //地理位置纬度
  private double longitude; //地理位置经度
  private double precision; //地理位置精度

  /**
   * 地图缩放大小
   */
  private int scale;

  /**
   * 地理位置信息
   */
  private String locationInfo;

  /**
   * app类型，在企业微信固定返回wxwork，在微信不返回该字段
   */
  private String appType;

  private WwExternalContact externalContact; // 企业微信的外部联系人

  /**
   * 任务卡片消息的ID
   * taskcard_click事件：与发送任务卡片消息时指定的task_id相同
   */
  private String taskId;

  /**
   * 标题
   */
  private String title;
  /**
   * 描述
   */
  private String description;
  /**
   * 链接跳转的url
   */
  private String url;
  /**
   * 封面缩略图的url
   */
  private String picUrl;




  public static WorkWxData fromXml(final String xml) {
    WorkWxData result = new WorkWxData();
    Document document = null;

    try {
      document = DocumentHelper.parseText(xml);
    } catch (DocumentException ex) {
      ex.printStackTrace();
      return null;
    }

    Element root = document.getRootElement();

    for (Iterator i = root.elementIterator(); i.hasNext(); ) {
      Element element = (Element) i.next();

      final String elementName = element.getName();
      final String elementValue = element.getStringValue();
      result.setMap(elementName, elementValue);

      if (elementName.compareToIgnoreCase("ToUserName") == 0) {
        result.setToUserName(elementValue);
      } else if (elementName.compareToIgnoreCase("FromUserName") == 0) {
        result.setFromUserName(elementValue);
      } else if (elementName.compareToIgnoreCase("MsgType") == 0) {
        result.setMsgType(elementValue);
      } else if (elementName.compareToIgnoreCase("Content") == 0) {
        result.setContent(elementValue);
      } else if (elementName.compareToIgnoreCase("CreateTime") == 0) {
        result.setCreateTime(Long.parseLong(elementValue));

      } else if (elementName.compareToIgnoreCase("Event") == 0) {
        result.setEvent(elementValue);
      } else if (elementName.compareToIgnoreCase("EventKey") == 0) {
        result.setEventKey(elementValue);
      } else if (elementName.compareToIgnoreCase("AgentID") == 0) {
        String temp = elementValue.trim();
        if (temp.length() > 0) {
          result.setAgentId(Integer.parseInt(temp));
        }
      } else if (elementName.compareToIgnoreCase("Encrypt") == 0) {
        result.setEncryptedMsg(elementValue);

      } else if (elementName.compareToIgnoreCase("Latitude") == 0) {
        result.setLatitude(Double.parseDouble(elementValue));

      } else if (elementName.compareToIgnoreCase("Longitude") == 0) {
        result.setLongitude(Double.parseDouble(elementValue));

      } else if (elementName.compareToIgnoreCase("Precision") == 0) {
        result.setPrecision(Double.parseDouble(elementValue));

      } else if (elementName.compareToIgnoreCase("ChangeType") == 0) {
        result.setChangeType(elementValue);
      } else if (elementName.compareToIgnoreCase("Contact") == 0) {
        WwExternalContact contact = WwExternalContact.createByElement(element);
        result.setExternalContact(contact);
      } else if ("TaskId".equalsIgnoreCase(elementName)) {
        result.setTaskId(elementValue);
      } else if ("MsgId".equalsIgnoreCase(elementName)) {
        result.setMsgId(Long.parseLong(elementValue));
      } else if ("MediaId".equalsIgnoreCase(elementName)) {
        result.setMediaId(elementValue);
      } else if ("Format".equalsIgnoreCase(elementName)) {
        result.setMediaFormat(elementValue);
      } else if ("ThumbMediaId".equalsIgnoreCase(elementName)) {
        result.setThumbMediaId(elementValue);
      } else if ("Location_X".equalsIgnoreCase(elementName)) {
        result.setLatitude(Double.parseDouble(elementValue));
      } else if ("Location_Y".equalsIgnoreCase(elementName)) {
        result.setLongitude(Double.parseDouble(elementValue));
      } else if ("Scale".equalsIgnoreCase(elementName)) {
        result.setScale(Integer.parseInt(elementValue));
      } else if ("Label".equalsIgnoreCase(elementName)) {
        result.setLocationInfo(elementValue);
      } else if ("AppType".equalsIgnoreCase(elementName)) {
        result.setAppType(elementValue);
      } else if ("Title".equalsIgnoreCase(elementName)) {
        result.setTitle(elementValue);
      } else if ("Description".equalsIgnoreCase(elementName)) {
        result.setDescription(elementValue);
      } else if ("Url".equalsIgnoreCase(elementName)) {
        result.setUrl(elementValue);
      } else if ("PicUrl".equalsIgnoreCase(elementName)) {
        result.setPicUrl(elementValue);
      } else {
        System.out.println(elementName + ": " + elementValue);
      }
    }

    return result;
  }

  private final Map<String, Object> map = new HashMap<>();

  public void setMap(String key, Object value) {
    if (key != null && value != null) {
      map.put(key, value);
    }
  }

  public Object get(String key) {
    return this.map.get(key);
  }

  public String toXml() {
    //数据为空时不能转化为xml格式
    if (0 == this.map.size()) {
      return null;
    }
    String xml = "<xml>";
    for (Map.Entry<String, Object> entry : map.entrySet()) {
      if (entry.getValue() == null) {
        return null;
      }

      Class aClass = entry.getValue().getClass();
      if ( aClass == Integer.class || aClass == Long.class) {
        xml += "<" + entry.getKey() + ">" + entry.getValue() + "</" + entry.getKey() + ">";
      } else if (aClass == String.class) {
        xml += "<" + entry.getKey() + ">" + "<![CDATA[" + entry.getValue() + "]]></" + entry.getKey() + ">";
      } else {
        return null;
      }
    }

    xml += "</xml>";
    return xml;
  }

  public String getToUserName() {
    return toUserName;
  }

  public void setToUserName(String toUserName) {
    this.toUserName = toUserName;
  }

  public String getFromUserName() {
    return fromUserName;
  }

  public void setFromUserName(String fromUserName) {
    this.fromUserName = fromUserName;
  }

  public Long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(long createTime) {
    this.createTime = createTime;
  }

  public String getMsgType() {
    return msgType;
  }

  public void setMsgType(String msgType) {
    this.msgType = msgType;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }


  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public String getEventKey() {
    return eventKey;
  }


  public int getAgentId() {
    return agentId;
  }

  public void setAgentId(int agentId) {
    this.agentId = agentId;
  }

  public String getEncryptedMsg() {
    return encryptedMsg;
  }

  public void setEncryptedMsg(String encryptedMsg) {
    this.encryptedMsg = encryptedMsg;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public double getPrecision() {
    return precision;
  }

  public void setPrecision(double precision) {
    this.precision = precision;
  }

  public String getChangeType() {
    return changeType;
  }

  public void setChangeType(String changeType) {
    this.changeType = changeType;
  }

  public WwExternalContact getExternalContact() {
    return externalContact;
  }

  public void setExternalContact(WwExternalContact externalContact) {
    this.externalContact = externalContact;
  }

  public String getTaskId() {
    return taskId;
  }

  public void setTaskId(String taskId) {
    this.taskId = taskId;
  }

  public void setEventKey(String eventKey) {
    this.eventKey = eventKey;
  }

  public long getMsgId() {
    return msgId == null ? 0 : msgId;
  }

  public void setMsgId(Long msgId) {
    this.msgId = msgId;
  }

  public String getMediaId() {
    return mediaId;
  }

  public void setMediaId(String mediaId) {
    this.mediaId = mediaId;
  }

  public String getMediaFormat() {
    return mediaFormat;
  }

  public void setMediaFormat(String mediaFormat) {
    this.mediaFormat = mediaFormat;
  }

  public String getThumbMediaId() {
    return thumbMediaId;
  }

  public void setThumbMediaId(String thumbMediaId) {
    this.thumbMediaId = thumbMediaId;
  }

  public int getScale() {
    return scale;
  }

  public void setScale(int scale) {
    this.scale = scale;
  }

  public String getLocationInfo() {
    return locationInfo;
  }

  public void setLocationInfo(String locationInfo) {
    this.locationInfo = locationInfo;
  }

  public String getAppType() {
    return appType;
  }

  public void setAppType(String appType) {
    this.appType = appType;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getPicUrl() {
    return picUrl;
  }

  public void setPicUrl(String picUrl) {
    this.picUrl = picUrl;
  }

  public Map<String, Object> getMap() {
    return map;
  }
}
