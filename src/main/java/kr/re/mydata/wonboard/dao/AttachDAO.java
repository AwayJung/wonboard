package kr.re.mydata.wonboard.dao;

import kr.re.mydata.wonboard.model.db.Attach;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AttachDAO {
    void postAttach(Attach attach);

    Attach getAttach(@Param("postId") int postId);

    void deleteAttach(@Param("postId") int postId);
}
