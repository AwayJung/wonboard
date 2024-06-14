package kr.re.mydata.wonboard.dao;

import kr.re.mydata.wonboard.model.db.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleDAO {
    int postArticle(Article article);

    List<Article> getArticleList();

    Article getArticle(int id);

    int updateArticle(int id, Article article);

    int deleteArticle(int id);
}
