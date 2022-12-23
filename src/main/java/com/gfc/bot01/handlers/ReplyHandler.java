package com.gfc.bot01.handlers;

import org.springframework.stereotype.Component;
import com.gfc.bot01.modules.object.BaseMessage;
import com.gfc.bot01.modules.object.StickerMessage;
import com.gfc.bot01.modules.object.TextMessage;

@Component
public class ReplyHandler {

  public BaseMessage[] textQuestionAnalysis(String question) {
    String answer = "看不懂~請在跟我說一次";
    if (question.matches("\\d+")) {
      answer = "你輸入的是數字";
    } else if (question.matches("^[A-Z]{2}+-{1}+[0-9]+")) {
      answer = "這是合約編號";
    } else {
      return new BaseMessage[] {new TextMessage(answer), new StickerMessage("11538", "51626532")};
    }
    return new BaseMessage[] {new TextMessage(answer)};
  }
}
