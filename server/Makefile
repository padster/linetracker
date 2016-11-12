build:
	 mvn clean compile assembly:assembly

run:
	java -jar target/padster-linetracker-1.0-jar-with-dependencies.jar --port 8080

runcloud:
	java -jar target/padster-linetracker-1.0-jar-with-dependencies.jar --port 8080 --store cloud

runstore:
	gcloud beta emulators datastore start --data-dir=cloudstore --host-port=localhost:8765

# TODO: make deploy
