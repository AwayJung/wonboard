package kr.re.mydata.wonboard.dao;


import kr.re.mydata.wonboard.model.db.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.userdetails.UserDetails;

@Mapper
public interface UserDAO {

    int createUser(@Param("user") User user) throws Exception;

    User getUserByEmail(@Param(value = "loginEmail") String loginEmail);

    int updateRefreshToken(@Param("loginEmail") String loginEmail, @Param("refreshToken") String refreshToken);

    void storeRefreshToken(@Param("loginEmail") String loginEmail, @Param("newRefreshToken") String newRefreshToken);

    String getStoredRefreshToken(@Param("loginEmail") String loginEmail);

}