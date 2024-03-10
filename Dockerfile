# 어느 이미지에서 시작할 것인지.
FROM openjdk:17
# 작업 디렉토리 지정. 해당 디렉토리가 없으면 새로 생성한다.
WORKDIR /app
# 빌드 명령 중간에 호스트의 파일 또는 폴더를 이미지에 가져오는 것.
COPY build/libs/app.jar /app
COPY application.properties /app
# 외부 Tomcat을 사용할 경우, 구동 시 추가할 실행옵션이 필요할 경우 JAVA_OPS 또는 TOMCAT_OPS 환경변수에 추가해 놓으면 실행 시 자동으로 참조한다.
ENV JAVA_OPS=""
# 새로운 레이어에서 명령어를 실행하고 새로운 이미지를 생성한다.
RUN test -f "application.properties" && \ export SPRING_CONFIG_LOCATION = "file:/app/application.properties" || true
                          ## 컨테이너를 생성 및 실행할 때 사용할 명령어 (CMD: 컨테이너를 생성할 때만 실행된다.)
CMD ["java", "-jar", "app.jar"]

