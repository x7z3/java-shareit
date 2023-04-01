@echo off
pushd %~dp0
echo compiling project
call mvn -B clean package
if %ERRORLEVEL% neq 0 goto error
echo clean docker old images
docker stop gateway-container server-container shareit-postgres
docker rm gateway-container server-container shareit-postgres
docker-compose rm -f
docker image rm -f server gateway
echo deploying new docker images
docker-compose up --build
:error
echo compilation error