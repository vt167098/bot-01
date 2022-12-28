package com.gfc.bot01;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.gfc.bot01.model.Cntm007b;
import com.gfc.bot01.modules.http.OKHttpExecuter;
import com.gfc.bot01.modules.object.BaseMessage;
import com.gfc.bot01.modules.object.TextMessage;
import com.google.gson.Gson;

@SpringBootTest
class Bot01ApplicationTests {

  @Test
  void contextLoads() {}

  @Test
  void getComputeExceptionTest() {
    String url = "http://192.6.1.48:3017/api/task/suspend/cntm007b";
    url = "http://192.6.1.48:3012/redis/proctr/A";

    Map<String, String> header = new HashMap<>();
    header.put("id", "2429");
    header.put("pwd", "9");
    OKHttpExecuter executer = new OKHttpExecuter();
    String result = executer.sendGetIntranet(url, header, null);
    if (result.matches("^\\[+.+\\]$")) {
      String[] res = result.replace("[{", "").replace("}]", "").split("\\},\\{");
      List<BaseMessage> list = new ArrayList<BaseMessage>();
      Gson gson = new Gson();
      for (String c : res) {
        Cntm007b c007b = gson.fromJson("{" + c + "}", Cntm007b.class);
        list.add(new TextMessage(String.format("%s-%s-%s-%s", c007b.getItem(), c007b.getCtNo(),
            c007b.getSpecItem(), c007b.getChrValue())));
      }
      BaseMessage[] msg =  (BaseMessage[]) list.toArray();
    }
    Assertions.assertEquals("桃園分公司", executer.sendGetIntranet(url, header, null));
  }

  @Test
  void regularMatchTest() {
    Pattern patt = Pattern.compile("Exception=|pro_ctr=");
    String test = "Exception=compute";

    Assertions.assertEquals(true, patt.matcher(test).find(0));
    Assertions.assertEquals(true, test.matches(".+compute$"));

    test = "pro_ctr=A";

    Assertions.assertEquals(true, patt.matcher(test).find(0));
    Assertions.assertEquals("A", test.replace("pro_ctr=", ""));
  }
}
