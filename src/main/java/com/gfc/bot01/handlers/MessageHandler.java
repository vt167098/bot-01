package com.gfc.bot01.handlers;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.gfc.bot01.listener.MessageListener;
import com.gfc.bot01.modules.api.ReplyMessage;
import com.gfc.bot01.modules.http.LineConnector;
import com.gfc.bot01.modules.object.BaseMessage;
import com.gfc.bot01.modules.object.StickerMessage;
import com.gfc.bot01.modules.object.TextMessage;

@Component
public class MessageHandler {

  @Value("${line.bot.channel-token}")
  private String LINE_TOKEN;

  @Autowired
  private ReplyHandler replyHandler;

  public void doAction(JSONObject event) {
    doAction(this.messageListener, event);
  }

  public void doAction(MessageListener messageListener, JSONObject event) {
    switch (event.getJSONObject("message").getString("type")) {
      case "text":
        messageListener.text(event.getString("replyToken"),
            event.getJSONObject("message").getString("text"));
        break;
      case "image":
        messageListener.image(event.getString("replyToken"),
            event.getJSONObject("message").getString("id"));
        break;
      case "video":
        messageListener.video(event.getString("replyToken"),
            event.getJSONObject("message").getString("id"));
        break;
      case "audio":
        messageListener.audio(event.getString("replyToken"),
            event.getJSONObject("message").getString("id"),
            event.getJSONObject("message").getLong("duration"));
        break;
      case "file":
        messageListener.file(event.getString("replyToken"),
            event.getJSONObject("message").getString("id"),
            event.getJSONObject("message").getString("fileName"),
            event.getJSONObject("message").getLong("fileSize"));
        break;
      case "location":
        messageListener.location(event.getString("replyToken"),
            event.getJSONObject("message").getString("title"),
            event.getJSONObject("message").getString("address"),
            event.getJSONObject("message").getDouble("latitude"),
            event.getJSONObject("message").getDouble("longitude"));
        break;
      case "sticker":
        messageListener.sticker(event.getString("replyToken"),
            event.getJSONObject("message").getString("packageId"),
            event.getJSONObject("message").getString("stickerId"));
        break;
    }
  }

  private MessageListener messageListener = new MessageListener() {

    @Override
    public void video(String replyToken, String id) {
      // TODO Auto-generated method stub
      System.out.printf("%s\t%s\n", replyToken, String.format("video: %s", id));
    }

    @Override
    public void text(String replyToken, String text) {
      // TODO Auto-generated method stub
      System.out.printf("%s\t%s\n", replyToken, String.format("text: %s", text));
      LineConnector.getInstance().replyMessage(LINE_TOKEN,
          new ReplyMessage(replyToken, replyHandler.textQuestionAnalysis(text)));
    }

    @Override
    public void sticker(String replyToken, String packageId, String stickerId) {
      // TODO Auto-generated method stub
      System.out.printf("%s\t%s\n", replyToken,
          String.format("sticker: %s(%s)", packageId, stickerId));
      LineConnector.getInstance().replyMessage(LINE_TOKEN,
          new ReplyMessage(replyToken, new BaseMessage[] {new TextMessage("看不懂~請在跟我說一次"),
              new StickerMessage("11538", "51626532")}));
    }

    @Override
    public void location(String replyToken, String title, String address, double latitude,
        double longitude) {
      // TODO Auto-generated method stub
      System.out.printf("%s\t%s\n", replyToken, String
          .format("location: %s, %s, title: %s, address: %s", latitude, longitude, title, address));
    }

    @Override
    public void image(String replyToken, String id) {
      // TODO Auto-generated method stub
      System.out.printf("%s\t%s\n", replyToken, String.format("image: %s", id));
    }

    @Override
    public void file(String replyToken, String id, String fileName, long fileSize) {
      // TODO Auto-generated method stub
      System.out.printf("%s\t%s\n", replyToken,
          String.format("file: %s, id: %s, size: %s", fileName, id, fileSize));
    }

    @Override
    public void audio(String replyToken, String id, long duration) {
      // TODO Auto-generated method stub
      System.out.printf("%s\t%s\n", replyToken,
          String.format("audio file: %s, time: %s", id, duration));
    }
  };
}
