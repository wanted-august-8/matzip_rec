#"deploy.sh"                                                                                                                                                                                                      8,31          All
#!/bin/bash

REPOSITORY="/home/ec2-user/app"  # 애플리케이션이 있는 경로
PORT=8080  # 사용할 포트 번호
LOG_FILE="/home/ec2-user/app/deploy.sh.log"  # 로그 파일 경로

# 로그 파일에 기록하도록 exec를 사용하여 STDOUT 및 STDERR를 리디렉션합니다.
exec > >(tee -a $LOG_FILE) 2>&1

# 8080 포트에 서비스가 있는지 확인하고, 있다면 종료합니다.
echo "> Checking for application running on port: $PORT"
CURRENT_PID=$(sudo lsof -t -i :$PORT)

if [ -n "$CURRENT_PID" ]; then
  echo "> Application running on port $PORT with PID: $CURRENT_PID"
  echo "> Stopping application with PID: $CURRENT_PID"
  sudo kill -15 $CURRENT_PID
  sleep 5  # 애플리케이션이 종료될 때까지 대기
else
  echo "> No application is running on port $PORT"
fi

# 최신 JAR 파일을 찾아 실행합니다.
JAR_NAME=$(ls -tr $REPOSITORY/*SNAPSHOT.jar | tail -n 1)

echo "> JAR NAME: $JAR_NAME"

echo "> Adding execute permission to $JAR_NAME"
chmod +x $JAR_NAME

echo "> Running $JAR_NAME"
# logback.xml 파일의 경로를 설정하여 애플리케이션을 백그라운드에서 실행합니다.
LOGBACK_CONFIG_FILE="/home/ec2-user/app/logback.xml"
nohup java -jar -Duser.timezone=Asia/Seoul -Dlogging.config=file:$LOGBACK_CONFIG_FILE $JAR_NAME >> $REPOSITORY/nohup.out 2>&1 &

# 실행 후 애플리케이션의 PID를 출력합니다.
NEW_PID=$(pgrep -f $JAR_NAME)
echo "> Currently running matzip application PID: $NEW_PID"
