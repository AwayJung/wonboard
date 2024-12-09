# Self Introduction Board

자기소개와 포트폴리오를 관리하는 게시판 서비스입니다.

## 기술 스택
### Backend
- Java 17
- Spring Boot 3.2.6
- Spring Security
- MyBatis 3.0.3
- MySQL
- JWT (JJWT 0.9.1)
- Lombok
- Model Mapper 3.2.0

### Frontend (별도 저장소)
- Vue.js
- Nuxt.js

## 주요 기능
- 사용자 관리
  - JWT 기반 인증
  - 회원가입/로그인
- 게시판 기능
  - 게시글 CRUD
  - 파일 첨부 기능
- 포트폴리오 관리
  - 자기소개 페이지
  - 기술 스택 관리
  - 프로젝트 이력 관리

## 실행 방법

### 요구사항
- Java 17
- MySQL 8.0
- Maven

### 데이터베이스 설정
```sql
CREATE DATABASE wonboard;
CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin1234';
GRANT ALL PRIVILEGES ON wonboard.* TO 'admin'@'localhost';
