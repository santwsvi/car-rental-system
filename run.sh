#!/bin/zsh
export JAVA_HOME=/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home
export PATH=$JAVA_HOME/bin:$PATH
lsof -ti:8080 | xargs kill -9 2>/dev/null
sleep 1
cd /Users/victor.gabriel/Documents/Pessoal/car-rental-system
./mvnw clean compile -q 2>&1
exec ./mvnw mn:run 2>&1 | tee /tmp/car-rental.log


