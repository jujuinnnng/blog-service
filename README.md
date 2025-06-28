# 📝 Blog Service

OAuth2 기반 로그인, 게시글 CRUD, JWT 인증을 포함한 개인 블로그 서비스입니다.  
Spring Boot, JPA, MySQL, Spring Security, GitHub Actions 등을 활용해 개발되었으며,  
AWS Elastic Beanstalk를 통해 배포되었습니다.

<br>

## 🛠️ 기술 스택

- **Language**: Java 17  
- **Backend**: Spring Boot 3, Spring Security, Spring Data JPA  
- **Database**: MySQL (AWS RDS)  
- **Authentication**: Google OAuth 2.0 + JWT  
- **CI/CD**: GitHub Actions + AWS Elastic Beanstalk  
- **Build Tool**: Gradle  
- **Etc**: H2 (테스트용), Lombok  
- **Test**: JUnit 5, Spring Boot Test

<br>

## 🚀 주요 기능

- 🔐 **Google OAuth2 로그인/로그아웃**
- 🧾 **JWT 기반 인증 및 인가 처리**
- 📰 **게시글 등록 / 수정 / 삭제 / 조회 (CRUD)**
- 🔄 **GitHub Actions를 통한 CI 자동화**
- 🌐 **AWS Elastic Beanstalk를 통한 배포 자동화**

<br>

## 🧪 테스트

- `@SpringBootTest` 기반 단위 테스트 및 통합 테스트 수행
- 게시글 관련 주요 기능에 대한 테스트 포함

<br>

## 🖥️ 배포 및 🔐 보안 관리

- AWS Elastic Beanstalk 환경 구성 완료
- `application.yml`, `application-test.yml`은 보안을 위해 `.gitignore` 처리
  > ※ 배포용 `application.yml`은 별도로 환경변수 등을 통해 관리
- Google OAuth Client ID 및 Secret은 GitHub Secrets를 통해 관리


