# Stay - 개인 프로젝트

## 프로젝트 소개 

 숙박 업소 중개 플랫폼으로 가입한 회원들 중 판매회원은 숙박업소 등록 및 객실을 추가하여 관리할 수 있고 일반회원은 예약가능한 날짜를 조회하여 예약 및 결제금액에 따른 등급과 마일리지 적립을 하고 사용할 수 있습니다. 

## 개발환경
language : Java 17

Framework: Springboot(3.2.1)

Database: JPA, MySQL

## ERD
![Stay_ERD review에 reservation id 추가, recomment에 comment id 추가 reservation에 userPoint 추가](https://github.com/BDCOOT/Stay/assets/94902010/892f07f1-8e12-4811-8fe5-62118773df7f)


## 구현
        //회원가입
    @PostMapping("/signup")
    public ResponseEntity<Object> signUp(@RequestBody User req)throws Exception  {
        Map<String, String> map = new HashMap<>();
        try {

            String shortUUID = generatedId.shortUUID();
            String hashPassword = bcrypt.HashPassword(req.getAppKey());
            req.setId(shortUUID);
            req.setAppKey(hashPassword);
            req.setGrade("bronze");
            req.setUserType("일반회원");
            req.setBanned(false);

            userService.createMileage(req.getUserId());
            userService.saveUser(req);
            map.put("result", "회원 가입 성공");
        } catch (Exception e) {
            map.put("error", e.toString());
        }
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
