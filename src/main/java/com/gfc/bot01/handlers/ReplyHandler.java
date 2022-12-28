package com.gfc.bot01.handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import com.gfc.bot01.model.Cntm007b;
import com.gfc.bot01.modules.http.OKHttpExecuter;
import com.gfc.bot01.modules.object.BaseMessage;
import com.gfc.bot01.modules.object.StickerMessage;
import com.gfc.bot01.modules.object.TextMessage;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@Component
public class ReplyHandler {

  public BaseMessage[] textQuestionAnalysis(String question) {
    String answer = "看不懂~請在跟我說一次";
    OKHttpExecuter executer = new OKHttpExecuter();
    Map<String, String> header = new HashMap<>();
    Pattern patt = Pattern.compile("中心");

    if (question.matches("\\d+")) {
      answer = "你輸入的是數字";
    } else if (question.matches("^[A-Z]{2}+-{1}+[0-9]+")) {
      answer = "這是合約編號";
    } else if (question.equals("售審單展算")) {
      header.put("x-csrf-token", "id=2429;pwd=9");
      answer = executer.sendGetIntranet("http://192.6.1.48:3017/api/task/suspend/cntm007b", header,
          null);
      System.out.println(String.format("patt: %s", answer));

      if (answer.matches("^\\[+.+\\]$")) {
        return resultToBaseMessages(answer);
      } else {
        answer = "查無此定義的異常狀況";
      }
    } else if (question.equals("展算異常重跑")) {  
      header.put("x-csrf-token", "id=2429;pwd=9");
      executer.sendPostByJson("http://192.6.1.48:3017/api/task/rerunsuspend/cntm007b", header, "{'action':'reset'}'", new Callback() {
        
        @Override
        public void onResponse(Call call, Response response) throws IOException {
          System.out.println("展算異常重新進入排程成功");
          String answer = response.body().string();
          System.out.println(answer);
          BaseMessage[] msg = resultToBaseMessages(answer);
        }
        
        @Override
        public void onFailure(Call call, IOException e) {
          System.err.println("展算異常重新進入排程失敗");
          e.printStackTrace();
        }
      });
      answer = "展算異常重新進入排程中";
    } else if (patt.matcher(question).find(0)) {
      header.put("x-csrf-token", "id=2429;pwd=9");
      answer = executer.sendGetIntranet(
          String.format("http://192.6.1.48:3012/redis/proctr/%s", question.replace("中心", "")),
          header, null);
      // System.out.println(String.format("patt2: %s", answer));
    } else {
      return new BaseMessage[] {new TextMessage(answer), new StickerMessage("11538", "51626532")};
    }
    return new BaseMessage[] {new TextMessage(answer)};
  }
  
  private BaseMessage[] resultToBaseMessages(String answer) {
    String[] result = answer.replace("[{", "").replace("}]", "").split("\\},\\{");
    List<BaseMessage> list = new ArrayList<>();
    Gson gson = new Gson();
    for (String c : result) {
      Cntm007b c007b = gson.fromJson("{" + c + "}", Cntm007b.class);
      list.add(new TextMessage(String.format("%s-%s-%s-%s", c007b.getItem(), c007b.getCtNo(),
          c007b.getSpecItem(), c007b.getChrValue())));
    }
    BaseMessage[] msg = new BaseMessage[list.size()];
    int index = 0;
    for (BaseMessage i : list) {
      msg[index] = i;
      index++;
    }
    return msg;
  }
}
