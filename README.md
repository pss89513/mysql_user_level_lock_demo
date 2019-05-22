### MySQL 설치
[Docker](https://www.docker.com/) 설치 후
```
docker run --name mysql57 \
    -p 3306:3306 \
    -e MYSQL_ROOT_PASSWORD=root \
    -e MYSQL_ROOT_HOST='%' \
    --restart=unless-stopped \
    -d \
    mysql/mysql-server:5.7
```
MySQL 콘솔접속
```
docker exec -it mysql57 bash
```
데이터베이스 생성
```
create database user_lock
```