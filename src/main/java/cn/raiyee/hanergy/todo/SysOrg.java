package cn.raiyee.hanergy.todo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Data
public class SysOrg {
    private String id; // EKP系统组织架构唯一标识
    private String personNo; // EKP系统组织架构个人编号
    private String deptNo; // EKP系统组织架构部门编号
    private String postNo; // EKP系统组织架构岗位编号
    private String groupNo; // EKP系统组织架构常用群组编号
    private String loginName;    // EKP系统组织架构个人登录名
    private String keyword; // EKP系统组织架构关键字
    private String ldapDN; // 和LDAP集成时LDAP中DN值

    @SneakyThrows
    public String createTargets() {
        Map<String, String> m = Maps.newLinkedHashMap();
        for (val f : SysOrg.class.getDeclaredFields()) {
            putIfPresent(m, StringUtils.capitalize(f.getName()), (String) f.get(this));
        }

        if (m.isEmpty()) throw new RuntimeException("No any targets specified");
        if (m.size() == 1) return JSON.toJSONString(m);

        val jsonArray = new JSONArray();
        for (val e : m.entrySet()) {
            jsonArray.add(ImmutableMap.of(e.getKey(), e.getValue()));
        }
        return jsonArray.toJSONString();
    }

    private void putIfPresent(Map<String, String> m, String name, String val) {
        if (StringUtils.isNotEmpty(val)) m.put(name, val);
    }
}
