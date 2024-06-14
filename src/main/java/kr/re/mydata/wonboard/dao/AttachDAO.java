package kr.re.mydata.wonboard.dao;

import kr.re.mydata.wonboard.model.db.Attach;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AttachDAO {
    void postAttach(Attach attach);
}
