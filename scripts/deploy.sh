#!/bin/bash

REPOSITORY=/home/ec2-user/

cd $REPOSITORY/team3-server/

echo "> Git Pull"

git pull origin develop

echo "> Build"

./gradlew bootJar

echo "> Build file copy"

cp build/libs/*.jar $REPOSITORY/

echo "> Application pid check"

CURRENT_PID=$(pgrep -f instagram)

echo "$CURRENT_PID"

if [ -z $CURRENT_PID ]; then
        echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
        echo "> kill -2 $CURRENT_PID"
        kill -2 $CURRENT_PID
        sleep 5
fi

echo "> 새 애플리케이션 배포"

JAR_NAME=$(ls $REPOSITORY | grep 'instagram' | tail -n 1)

echo "> JAR Name: $JAR_NAME"

nohup java -jar -Dspring.profiles.active=dev $REPOSITORY/$JAR_NAME &
