build:
	 mvn clean compile assembly:assembly

run_memorystore: build
	java -jar target/padster-linetracker-1.0-jar-with-dependencies.jar --port 8080

local_datastore:
	gcloud beta emulators datastore start --project linetracking --data-dir cloudstore --host-port localhost:8765

local_datastore_init:
	gcloud beta emulators datastore env-init --data-dir cloudstore

run_datastore_local: build
	DATASTORE_DATASET=linetracking \
	DATASTORE_EMULATOR_HOST=localhost:8765 \
	DATASTORE_PROJECT_ID=linetracking \
	java -jar target/padster-linetracker-1.0-jar-with-dependencies.jar --port 8080 --store datastore_local

run_datastore_gcp: build
	GOOGLE_APPLICATION_CREDENTIALS="./gcp-conf/linetracking-a359c122bb11.json" java -jar target/padster-linetracker-1.0-jar-with-dependencies.jar --port 8080 --store datastore_gcp

# TODO: make deploy
