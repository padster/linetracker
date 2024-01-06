FORCE: ;

run_local:
	REACT_APP_SERVER='http://localhost:8080' npm run start

run:
	npm start

build: FORCE
	npm run build

deploy:
	CLOUDSDK_CORE_PROJECT=linetracking gsutil -m cp -r build gs://linetracking_static/2023_08_18
