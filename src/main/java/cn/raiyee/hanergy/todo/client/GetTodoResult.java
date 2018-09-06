package cn.raiyee.hanergy.todo.client;

import lombok.Data;

import java.util.List;

@Data
public class GetTodoResult {
    private int pageCount;
    private int pageno;
    private int count;

    private List<TodoItem> docs;
}
