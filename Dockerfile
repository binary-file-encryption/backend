FROM openjdk:17
COPY ./build/libs/binary-file-encryption-0.0.1-SNAPSHOT.jar binary-file-encryption.jar
ENTRYPOINT ["java", "-jar", "binary-file-encryption.jar"]