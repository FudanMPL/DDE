package DavexBase.entity;

import DavexBase.info.Parameter;
import DavexBase.serializer.ParameterListTypeHandler;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@TableName(autoResultMap = true)
public class Fl {

    @TableId(type = IdType.ASSIGN_UUID)
    private String uid;
    private String name;
    @TableField(typeHandler = ParameterListTypeHandler.class)
    private List<Parameter> runtimeParameters;
    private String centerId;
    @JsonIgnore
    private String path;
}
