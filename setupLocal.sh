cd eureka-server
./gradlew.bat bootRun &
cd ..
cd gateway
./gradlew.bat bootRun &
cd ..
cd auth-service
./gradlew.bat bootRun &
cd ..
cd food-service
./gradlew.bat bootRun &
cd ..
cd home-service
./gradlew.bat bootRun &
cd ..
cd movie-service
./gradlew.bat bootRun &
cd ..
cd reserve-service
./gradlew.bat bootRun
