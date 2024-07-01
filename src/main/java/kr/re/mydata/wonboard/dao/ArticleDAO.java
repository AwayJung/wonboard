package kr.re.mydata.wonboard.dao;

import kr.re.mydata.wonboard.model.db.Article;
import kr.re.mydata.wonboard.model.response.v2.DeleteV2Resp;
import kr.re.mydata.wonboard.model.response.v2.DetailV2Resp;
import kr.re.mydata.wonboard.model.response.v2.ListV2Resp;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleDAO {
    int postArticle(Article article);

    List<ListV2Resp> getList();

    DetailV2Resp getDetail(int id);

    int update(int id, Article article);

    int delete(int id);

    DeleteV2Resp getDeleteDetail(int id);
}
