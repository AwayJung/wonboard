package kr.re.mydata.wonboard.model.db;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Attach {
    private String path;                             // 파일 경로
    private String realName;                      // 원본 파일명
    private int postId;                               // 게시글 번호(fk)
    private LocalDateTime regDt;               // 등록일
    private String Name;                          // 저장 파일명
    private int id;                                    // 파일 번호(pk)
}
