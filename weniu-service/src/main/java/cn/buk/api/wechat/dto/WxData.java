package cn.buk.api.wechat.dto;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 微信公众号推送过来的消息
 */
public class WxData {

  private String toUserName;
  private String fromUserName;
  private Long createTime;
  private Long msgId;
  private String msgType; //text,image,voice,event

  private String content; //text: 对应的内容

  private String picUrl;
  private String mediaId;

  private String event; //LOCATION-地理位置 enter_agent-进入应用事件
  private String eventKey;

  private String status;


  public static WxData fromXml(final String xml) {
    WxData result = new WxData();
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

      //获得row元素的所有属性列表
//            List elementList = element.attributes();
//            for (Iterator iter1 = elementList.iterator(); iter1.hasNext(); ) {
//                //将每个属性转化为一个抽象属性，然后获取其名字和值
//                AbstractAttribute aa = (AbstractAttribute) iter1.next();
//                logger.info("Name: " + aa.getName() + "; Value: " + aa.getValue() + ".");
//            }

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

      } else if (elementName.compareToIgnoreCase("MsgId") == 0) {
        result.setMsgId(Long.parseLong(elementValue));
      } else if (elementName.compareToIgnoreCase("Event") == 0) {
        result.setEvent(elementValue);
      } else if (elementName.compareToIgnoreCase("EventKey") == 0) {
        result.setEventKey(elementValue);
      } else if (elementName.compareToIgnoreCase("Status") == 0) {
        result.setStatus(elementValue);
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
      if (aClass == Integer.class || aClass == Long.class) {
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

  public long getMsgId() {
    return msgId;
  }

  public void setMsgId(long msgId) {
    this.msgId = msgId;
  }

  public String getPicUrl() {
    return picUrl;
  }

  public void setPicUrl(String picUrl) {
    this.picUrl = picUrl;
  }

  public String getMediaId() {
    return mediaId;
  }

  public void setMediaId(String mediaId) {
    this.mediaId = mediaId;
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

  public void setEventKey(String eventKey) {
    this.eventKey = eventKey;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

}
