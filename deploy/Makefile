build_image:
	rm server.jar ||:
	cp ../server/target/server.jar .
	rm -rf secret.key ||:
	cp ../server/secret.key .
	rm -rf lts-sa.json ||:
	cp ../server/gcp-conf/lts-sa.json .
	rm -rf client_src/ ||:
	cp -R ../client/build/ client_src/
	docker build -t linetracker-server .

run_image:
	docker run -p 127.0.0.1:8888:80 linetracker-server

kill_image:
	docker ps -lq | xargs docker kill

reload_nginx:
	sudo cp nginx.conf /usr/share/nginx/
	sudo /usr/sbin/nginx -t -c nginx.conf

deploy_image:
	docker tag linetracker-server us-west1-docker.pkg.dev/useful-theory-217216/useit-today-servers/linetracker-server:staging
	docker push us-west1-docker.pkg.dev/useful-theory-217216/useit-today-servers/linetracker-server:staging
