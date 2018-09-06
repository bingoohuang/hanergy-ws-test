package cn.raiyee.hanergy.todo.client;

import lombok.Data;

@Data
public class TodoItem {
    private String id;
    private String subject;
    private int type;
    private String key;
    private String param1;
    private String param2;
    private String appName;
    private String modelName;
    private String moduleName;
    private String modelId;
    private String createTime;
    private String creator;
    private String creatorName;
    private String link;
}
