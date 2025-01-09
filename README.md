# 🌀 Spring MVC (인증)

# 1단계
___
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

# 2단계
___
## 로그인 리팩터링
#### HandlerMethodArgumentResolver
+ 컨트롤러 메서드 파라미터로 자동 주입

## 예약 생성 기능 변경
+ 예약 : ReservationRequest(요청 DTO)
    -> name이 있으면 name으로 Member 찾기
    -> name이 없으면 Cookie에 담긴 정보 활용

# 3단계
___
## 관리자 기능
+ admin 페이지 진입 (HandlerInterceptor 이용)
    -> 관리자 : 진입 가능
    -> 관리자 X : 401 코드 응답

# 🌀 Spring JPA (인증)

# 4단계
___
## JPA 전환
+ 엔티티 & 연관 관계 매핑
+ DAP -> JpaRepository를 상속받는 Repository로 대체

# 5단계
___
## 내 예약 목록 조회
+ 응답 DTO -> 예약 아이디, 테마, 날짜, 시간, 상태를 포함
## 예약 테이블 수정
+ 관리자 예약 (어드민 화면) : name을 string으로 전달
+ 사용자 예약 (예약 화면) : 로그인 정보를 이용해 Member ID 저장
## API
+ GET/reservation-mine : reservation-mine 페이지 응답
+ GET/reservations-mine : 내 예약 목록 조회

