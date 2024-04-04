# Stay - 개인 프로젝트

## 프로젝트 소개 

&nbsp;숙박 업소 중개 플랫폼으로 가입한 회원들 중 판매회원은 숙박업소 등록 및 객실을 추가하여 관리할 수 있고

일반회원은 예약가능한 날짜를 조회하여 예약 및 결제금액에 따른 등급과 마일리지 적립을 하고 사용할 수 있습니다. 

관리자는 숙소 ban 및 일반회원의 예약 취소, 악성 리뷰 삭제를 할 수 있습니다. 숙소 ban이 되면 일반회원이 객실을 예약하고자할 때 조회되지 않으며 예약이 불가능합니다.

## 개발 기간 및 환경
개발 기간 : 2024년 2월

language : Java 17

Framework: Springboot(3.2.1)

Database: JPA, MySQL

## ERD
![Stay_ERD review에 reservation id 추가, recomment에 comment id 추가 reservation에 userPoint 추가](https://github.com/BDCOOT/Stay/assets/94902010/2092e442-3f5f-49b8-b69f-e19c37ecfd2d)



## 구현
 ![image](https://github.com/BDCOOT/Stay/assets/94902010/e66ceed4-24ae-44a8-8009-560926559ddf)


 예약시 등급 별 적립(브론즈 1%, 실버 2%, 골드 3%, 플레티넘 4%, 다이아몬드 5%)
 ![image](https://github.com/BDCOOT/Stay/assets/94902010/6c6f622d-b692-4d4e-967c-3bdd3741f003)

 예약 가능한 방 조회
 ![image](https://github.com/BDCOOT/Stay/assets/94902010/00e5afd5-8bb5-43e6-ab54-f9fd769b6571)
 ![image](https://github.com/BDCOOT/Stay/assets/94902010/21b977b7-4929-43c7-a951-e9f68075e495)

 숙소 별 매출액 조회
 ![image](https://github.com/BDCOOT/Stay/assets/94902010/bfded140-ab1b-4d02-9325-adf44420a286)



 리뷰 작성(aws S3 내 이미지 삽입)
 ![image](https://github.com/BDCOOT/Stay/assets/94902010/6c95b797-7ab0-4178-ad65-4c5bf64ad859)
![image](https://github.com/BDCOOT/Stay/assets/94902010/17e0931f-0523-46e2-b4ea-a29333975fd6)


