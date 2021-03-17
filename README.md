# Assignment Server

---

## 분석
- 요구 상황 : [docs/plan.md](docs/plan.md)

### 개발
- 작성 중..

### 성능
- 작성 중..

### 트랜잭션
- 작성 중..

---
## 멀티 프로젝트
### Common
- Path : [./assignment-common](./assignment-common)
- 공통으로 사용할 모듈 분리
- DB 관련 Entity 포함


### API 
- Path : [./assignment-api](./assignment-api) 
- 머니 뿌리기, 받기, 조회 API
- API 문서는 spring rest docs로 작성 되었습니다.
- [API document](https://plzhans.github.io/test-assignment-kakaopay/assignment-api-document.html)

---

## 서버 구성

### Database H2
- 기본 설정은 메모리 데이터베이스(H2)를 사용합니다.
- 재시작 한 경우 테스트한 데이터는 초기 상태로 리셋 됩니다.
- h2 구성은 아래를 참조
  - 스키마 : [schema.sql](./assignment-common/src/main/resources/db/h2/schema.sql)
  - 데이터 : [data.sql](./assignment-common/src/main/resources/db/h2/data.sql)

### Database Mysql

mysql 테스트를 원한다면 docker를 이용하여 간단히 가능 합니다.
- **h2 db를 사용할 경우 무시해도 됩니다.**
- 자세한 내용은 [docker-compose.yml](./docker-compose.yml) 를 참조 해주세요.
  ```
  docker-compose up
  ```
- 이후 최초 1회 기본 스키마 및 테스트 데이터가 필요 합니다.
  - 스키마 : [schema.sql](./assignment-common/src/main/resources/db/mysql/schema.sql)
  - 데이터 : [data.sql](./assignment-common/src/main/resources/db/mysql/data.sql)
- 기본 구성은 mysql port 13306을 사용하고 docker 설정에서 수정 가능합니다.
    ```yaml
    version: "3.7"
    services:
      db:
        image: mysql:5.7
        container_name: mysql-assignment-test
        ports:
          - "13306:3306"
        environment:
          - MYSQL_ALLOW_EMPTY_PASSWORD=true
          - MYSQL_DATABASE=assignment
          - MYSQL_USER=assignment
          - MYSQL_PASSWORD=assignment
          - TZ=Asia/Seoul
        command:
          - --character-set-server=utf8mb4
          - --collation-server=utf8mb4_unicode_ci
    ```
---

## 빌드

### 전체 빌드
```
gradlew build
```

### 테스트 실행
기본 실행 : h2 db 사용
```
java -jar -Dfile.encoding=UTF-8 assignment-api/build/libs/assignment-api-0.0.1.jar
```

mysql 사용하여 시작 : spring.profiles.active=mysql
```
java -jar -Dfile.encoding=UTF-8 -Dspring.profiles.active=mysql assignment-api/build/libs/assignment-api-0.0.1.jar
```
