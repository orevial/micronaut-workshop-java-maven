#FROM oracle/graalvm-ce:19.3.1-java8 as graalvm
FROM oracle/graalvm-ce:19.3.1-java11 as graalvm
RUN gu install native-image

COPY . /home/app/rental-calculator
WORKDIR /home/app/rental-calculator

RUN native-image --no-server --static -cp target/rental-calculator-*.jar

FROM scratch
EXPOSE 8080
COPY --from=graalvm /home/app/rental-calculator/rental-calculator /app/rental-calculator
ENTRYPOINT ["/app/rental-calculator", "-Djava.library.path=/app"]
