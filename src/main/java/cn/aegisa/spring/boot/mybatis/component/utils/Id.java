package cn.aegisa.spring.boot.mybatis.component.utils;

import lombok.Data;

@Data
public class Id implements java.io.Serializable {
    private static final long serialVersionUID = -8428061463671815363L;

    private Integer minId;

    private Integer maxId;
    public static Id empty() {
        Id id = new Id();
        id.setMinId(0);
        id.setMaxId(0);
        return id;
    }

}
