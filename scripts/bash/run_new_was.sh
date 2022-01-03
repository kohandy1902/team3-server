#!/usr/bin/env bash

CURRENT_PORT=$(cat /home/ec2-user/service_url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0
REPOSITORY=/home/ec2-user/team3-server

echo "> cp ${REPOSITORY}/deploy/*.jar ${REPOSITORY}/"

cp ${REPOSITORY}/deploy/*.jar ${REPOSITORY}/

echo "> deploy new application"

JAR_NAME=$(ls -tr ${REPOSITORY}/*.jar | tail -n 1)

echo "> JAR NAME: ${JAR_NAME}"

echo "> give +x to ${JAR_NAME}"

chmod +x "${JAR_NAME}"

echo "> Current port of running WAS is ${CURRENT_PORT}"

if [ "${CURRENT_PORT}" -eq 8081 ]; then
    TARGET_PORT=8082
elif [ "${CURRENT_PORT}" -eq 8082 ]; then
    TARGET_PORT=8081
else
    echo "> NO WAS is connected to nginx"
fi

TARGET_PID=$(lsof -Fp -i TCP:${TARGET_PORT} | grep -Po 'p[0-9]+' | grep -Po '[0-9]+')

if [ ! -z "${TARGET_PID}" ]; then
    echo "> Kill WAS running at ${TARGET_PORT}."
    sudo kill ${TARGET_PID}
fi

nohup java -jar -Dserver.port="${TARGET_PORT}" -Dspring.profiles.active=dev "${JAR_NAME}" \
> $REPOSITORY/nohup.out 2>&1 &

echo "> Now new WAS runs at ${TARGET_PORT}"
exit 0
