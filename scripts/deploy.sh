#"deploy.sh"                                                                                                                                                                                                      8,31          All
#!/bin/bash

REPOSITORY="/home/ec2-user/app"  # 애플리케이션이 있는 경로
LOG_FILE="/home/ec2-user/app/deploy.sh.log"  # 로그 파일 경로

# Function to prepend current timestamp to each line of log output
timestamp() {
    while IFS= read -r line; do
        echo "$(date +'%Y-%m-%d %H:%M:%S') $line"
    done
}

# 로그 파일에 기록하도록 exec를 사용하여 STDOUT 및 STDERR를 리디렉션합니다.
exec > >(timestamp | tee -a "$LOG_FILE") 2>&1

echo "> ======================================================="

echo $USER

# PATH 환경 변수에 JDK 경로 추가
export PATH=$PATH:/usr/bin/java

# Java 프로세스를 확인하고, 있다면 종료합니다.
echo "> Checking for running Java processes"
JAVA_PIDS=$(pgrep -f 'java.*')

if [ -n "$JAVA_PIDS" ]; then
  echo "> Found Java processes with PIDs: $JAVA_PIDS"
  for PID in $JAVA_PIDS; do
    echo "> Stopping Java process with PID: $PID"
    sudo kill -15 $PID
  done
  sleep 5  # 애플리케이션이 종료될 때까지 대기
else
  echo "> No Java processes found"
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

if [ -n "$NEW_PID" ]; then
  echo "> Currently running matzip application PID: $NEW_PID"
else
  echo "> Application failed to start."
  exit 1
fi

echo "> ======================================================="

