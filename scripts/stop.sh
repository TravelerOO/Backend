#!/bin/bash

ROOT_PATH="/home/ubuntu/spring-github-action"
STOP_LOG="$ROOT_PATH/stop.log"

echo "[$NOW] 이전에 실행 중인 서비스를 종료합니다." >> $STOP_LOG
SERVICE_PIDS=$(pgrep -f "java -jar")
for SERVICE_PID in $SERVICE_PIDS; do
  SERVICE_JAR=$(readlink -f /proc/$SERVICE_PID/exe | cut -d' ' -f1-)
  if [[ "$SERVICE_JAR" =~ .*\.jar$ ]]; then
    kill $SERVICE_PID
    echo "[$NOW] > 종료한 서비스: $SERVICE_JAR, PID: $SERVICE_PID" >> $STOP_LOG
  fi
done
