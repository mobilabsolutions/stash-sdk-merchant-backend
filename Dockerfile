FROM adoptopenjdk/openjdk11:jre
ENV JAVA_OPTS="-Xmx256m"
ADD target/payment-merchant-*.jar executable.jar
ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar executable.jar
