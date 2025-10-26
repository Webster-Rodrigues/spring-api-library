#build
FROM maven:3.8.8-amazoncorretto-21-al2023 AS build

#Cria pasta
WORKDIR /build

#Joga o cód fonte para a pasta build, criada anteriormente. Primeiro parâmetro é a pasta atual o segundo é a pasta build. orgin e target
COPY . .

#Roda mvn. Cria a apsta target dentro da build
RUN mvn clean package -DskipTests

#run
FROM amazoncorretto:21.0.5

WORKDIR /app


COPY --from=build ./build/target/*.jar ./libraryapi.jar

#Export port

#Porta da aplicação
EXPOSE 8080

#Porta do actuator
EXPOSE 9090

ENV DATASOURCE_URL=''
ENV DATASOURCE_USERNAME=''
ENV DATASOURCE_PASSWORD=''

ENV GOOGLE_CLIENT_SECRET=''
ENV GOOGLE_CLIENT_ID=''

ENV SPRING_PROFILES_ACTIVE='production'
ENV TZ='America/Sao_Paulo'

ENTRYPOINT java -jar libraryapi.jar

