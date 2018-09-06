package cn.raiyee.hanergy.todo;

import cn.raiyee.hanergy.todo.client.GetTodoResult;
import com.alibaba.fastjson.JSON;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodoController {
    @GetMapping("/getTodos") @SneakyThrows
    public GetTodoResult getTodos() {
        @Cleanup val is = new ClassPathResource("todos.json").getInputStream(); // for demo
        return JSON.parseObject(is, GetTodoResult.class);
    }
}
