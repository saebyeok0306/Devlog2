FROM gradle:8.14.3-jdk17

WORKDIR /app

RUN apt-get update && apt-get install -y tzdata

# 타임존 설정
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ADD build.gradle .
RUN #gradle build -x test --parallel --continue > /dev/null 2>&1 || true

COPY . .
RUN gradle build -x test

WORKDIR /app/build/libs

ENTRYPOINT ["java", "-jar", "sitemap-service.jar", "--spring.profiles.active=local"]