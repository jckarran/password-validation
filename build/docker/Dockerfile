FROM openjdk:8
LABEL maintainer=jckss
WORKDIR /app
COPY libs libs/
COPY resources resources/
COPY classes classes/
ENTRYPOINT ["java", "-cp", "/app/resources:/app/classes:/app/libs/*", "password.validation.Main"]
EXPOSE 7000
