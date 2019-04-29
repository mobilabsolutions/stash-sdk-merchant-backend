FROM adoptopenjdk/openjdk11:jdk-11.0.2.9
ENV JAVA_OPTS="-Xmx256m"
ADD target/payment-merchant-*.jar executable.jar
ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar executable.jar