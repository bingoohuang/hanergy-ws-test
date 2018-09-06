package cn.raiyee.hanergy.todo;

import cn.raiyee.hanergy.WsClient;
import cn.raiyee.hanergy.todo.client.*;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import lombok.SneakyThrows;
import lombok.val;

import java.io.File;

public class TodoClient {

    public static void sendTo() {
        val service = WsClient.proxy(ISysNotifyTodoWebService.class, "http://ekp13.hanergy.com/sys/webservice/sysNotifyTodoWebService");
        // 请在此处添加业务代码
        val context = new NotifyTodoSendContext();

        context.setAppName("raiyee-test-app");
        context.setModelName("raiyee-test-model");
        context.setModelId("raiyee-test-model-001");
        context.setSubject("测试待办webservice~~~");
        context.setLink("http://www.baidu.com/");
        context.setType(2);
        context.setKey("sinaNews");
        context.setModelId("123456789");
        context.setTargets("{\"Id\":\"yanglin03\"}");
        context.setCreateTime("2018-09-06 11:03:05");

        NotifyTodoAppResult result = null;
        try {
            result = service.sendTodo(context);
        } catch (Exception_Exception e) {
            e.printStackTrace();
        }
        if (result != null) {
            if (result.getReturnState() != 2)
                System.out.println(result.getMessage());
        }
    }

    @SneakyThrows
    public static GetTodoResult getTodo() {
        val service = WsClient.proxy(ISysNotifyTodoWebService.class, "http://ekp13.hanergy.com/sys/webservice/sysNotifyTodoWebService");
        val context = new NotifyTodoGetContext();

        val sysOrg = new SysOrg();
        sysOrg.setLoginName("yanglin03");
        context.setTargets(sysOrg.createTargets());
        try {
            val result = service.getTodo(context);
            if (result.getReturnState() == 2) { // 2:表示操作成功
                Files.asCharSink(new File("todos.json"), Charsets.UTF_8).write(result.getMessage());
                return JSON.parseObject(result.getMessage(), GetTodoResult.class);
            }

            throw new RuntimeException("失败" + result);

        } catch (Exception_Exception e) {
            throw new RuntimeException(e);
        }


    }

    public static void main(String[] args) {
        getTodo();
    }
}
