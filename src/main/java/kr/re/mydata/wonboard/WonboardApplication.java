package kr.re.mydata.wonboard;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;


@SpringBootApplication
@MapperScan(basePackages = "kr.re.mydata.wonboard.dao")
@Configuration
public class WonboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(WonboardApplication.class, args);
	}

}
