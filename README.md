# 🌀 Spring MVC (인증)

## 로그인
#### 로그인 페이지
   + 이메일, 비밀번호 입력
#### 로그인 요청
   + 이메일, 비밀번호 -> 멤버 조회
   + 조회한 멤버로 토큰 발급
   + Cookie를 만들어 응답
#### 인증 정보 조회
   + Cookie -> 토큰 정보 추출
   + 멤버를 찾아서 응답

로그인 관련 API
+ GET/login : 로그인 페이지 호출
+ POST/login : 로그인 요청
+ GET/login/check : 인증 정보 조회

## 로그인 리팩터링
#### HandlerMethodArgumentResolver
+ 컨트롤러 메서드 파라미터로 자동 주입

## 예약 생성 기능 변경
+ 예약 : ReservationRequest(요청 DTO)
    -> name이 있으면 name으로 Member 찾기
    -> name이 없으면 Cookie에 담긴 정보 활용
