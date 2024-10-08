# Allows you to run this app easily as a docker container.
# See README.md for more details.
#
# 1. Build the image with: docker build --no-cache -t test/vaadin-boot-example-maven:latest .
# 2. Run the image with: docker run --rm -ti -p8080:8080 -m256m test/vaadin-boot-example-maven
#
# Uses Docker Multi-stage builds: https://docs.docker.com/build/building/multi-stage/

# The "Build" stage. Copies the entire project into the container, into the /app/ folder, and builds it.
FROM eclipse-temurin:17 AS BUILD
RUN apt update && apt install unzip -y
COPY . /app/
WORKDIR /app/
RUN ./mvnw -C clean test package -Pproduction
WORKDIR /app/target/
RUN ls -la
RUN unzip *.zip -d app/
# At this point, we have the app (executable bash scrip plus a bunch of jars) in the
# /app/target/app/ folder.

# The "Run" stage. Start with a clean image, and copy over just the app itself, omitting gradle, npm and any intermediate build files.
FROM openjdk:21-bookworm
COPY --from=BUILD /app/target/app /app/
WORKDIR /app/
EXPOSE 8080
ENTRYPOINT ./bin/app
