package DavexBase.mapper;

import DavexBase.entity.Fl;
import DavexBase.entity.Mpc;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FlMapper extends BaseMapper<Fl> {
}
