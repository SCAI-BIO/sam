FROM docker.io/library/openjdk:alpine

LABEL org.opencontainers.image.authors="marc.jacobs@scai.fraunhofer.de"

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-jar", "/usr/share/sam/sam-api.jar" ,  "--its.enable=true", "--its.concepts=/usr/share/sam/terminologies/key2aimed-concepts.tsv", "--its.terminologies=/usr/share/sam/terminologies/key2aimed-terminologies.tsv"]
EXPOSE 8080/tcp
ENV JAVA_TOOL_OPTIONS="$JAVA_TOOL_OPTIONS -Xmx16g"
ENV JAVA_TOOL_OPTIONS="$JAVA_TOOL_OPTIONS -XX:+UnlockExperimentalVMOptions"
ENV JAVA_TOOL_OPTIONS="$JAVA_TOOL_OPTIONS -XX:+UseCGroupMemoryLimitForHeap"
ENV JAVA_TOOL_OPTIONS="$JAVA_TOOL_OPTIONS -XX:-UseParNewGC"
ENV JAVA_TOOL_OPTIONS="$JAVA_TOOL_OPTIONS -XX:-UseConcMarkSweepGC"
ENV JAVA_TOOL_OPTIONS="$JAVA_TOOL_OPTIONS -XX:+UseCondCardMark"
ENV JAVA_TOOL_OPTIONS="$JAVA_TOOL_OPTIONS -XX:MaxGCPauseMillis=200"
ENV JAVA_TOOL_OPTIONS="$JAVA_TOOL_OPTIONS -XX:+UseG1GC"
ENV JAVA_TOOL_OPTIONS="$JAVA_TOOL_OPTIONS -XX:GCPauseIntervalMillis=1000"
ENV JAVA_TOOL_OPTIONS="$JAVA_TOOL_OPTIONS -XX:InitiatingHeapOccupancyPercent=35"
ENV JAVA_TOOL_OPTIONS="$JAVA_TOOL_OPTIONS -XX:+PrintCommandLineFlags"
ARG JAR_FILE
ADD sam-1.2-SNAPSHOT.jar /usr/share/sam/sam.jar
COPY configurations/ /usr/share/sam/configurations/
COPY terminologies/ /usr/share/sam/terminologies/
RUN ls -la /usr/share/sam/terminologies/*
RUN ls -la /usr/share/sam/configurations/*
RUN apk add --no-cache tzdata
RUN cp /usr/share/zoneinfo/Europe/Brussels /etc/localtime
RUN echo "Europe/Brussels" >  /etc/timezone
