# - para testes
# docker run --name mongodb --hostname mongodb -d -p 27017:27017 mongo

# leandrojesuseng
# - criar a imagem docker
# docker build -t leandrojesuseng/spring-boot-crud-onlyrest-mongodb .
# - executar o container, spring boot configurado na 8080, mas 9003 para o host
# docker run -p 9003:8080 --name crud --link mongodb leandrojesuseng/spring-boot-crud-onlyrest-mongodb

# docker tag leandrojesuseng/spring-boot-crud-onlyrest-mongodb leandrojesuseng/spring-boot-crud-onlyrest-mongodb 
# docker push leandrojesuseng/spring-boot-crud-onlyrest-mongodb:latest

FROM openjdk:11
RUN addgroup spring 
RUN adduser --ingroup spring spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
USER spring:spring
ENTRYPOINT ["java", "-jar", "/app.jar", "> saida.log", "2> errors.txt", "< /dev/null"]