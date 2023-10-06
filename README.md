# Todo List 백엔드 개발

- Spring을 이용한 TodoList 구현
- SpringBoot 2.7.16 버전에서 SpringBoot 3.1.4 버전으로 변경하였습니다.
- 2.7.16 버전 링크(기본 기능까지 구현)
  [GitHub - KitStu17/todo](https://github.com/KitStu17/todo)
- 버전 변경에 따른 이슈 해결 과정
  - WebSecurityConfig 관련
    [WebSecurityConfig 버전 변경에 따른 대처](https://brash-draw-d7b.notion.site/WebSecurityConfig-66c276e28b274244812277d378b6b901?pvs=4)
  - spring-boot-starter-mail 관련
    [spring-boot-starter-mail 버전 변경에 따른 대처](https://brash-draw-d7b.notion.site/spring-boot-starter-mail-4383680e2b594f5f8b0107b459e4924d?pvs=4)

## 기본 기능 구현 사항

- backend 소스 코드 예제 완료
- Git, GitHub Commit을 이용한 버전관리 및 Push, Pull 사용

## 추가 기능 구현 사항

1. 일괄 삭제를 위한 DELETE “/todo/deleteList” api 추가
2. 이메일 인증 코드 전송을 위한 POST “/mail/send” api 추가
3. 인증 코드 확인을 위한 POST “/mail/check” api 추가
4. 회원정보 수정 기능을 위한 PUT “/auth/updateUser” api 추가(이름, 비밀번호, 이메일 변경 가능)
5. token을 통해 회원 정보를 조회하는 GET “/auth/getUser” api 추가
6. H2 DB → Maria DB로 DB 변경
