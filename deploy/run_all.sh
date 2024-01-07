#!/bin/bash

set -m

GOOGLE_APPLICATION_CREDENTIALS="./lts-sa.json" java -jar server.jar --port 8080 --client_uri 'https://linetracker-server-pwgmdoddsa-uw.a.run.app' --store datastore_gcp &serve -s client_src -l 8081 &
/usr/sbin/nginx -c /app/nginx.conf

# Wait for any process to exit
wait -n

# Exit with status of process that exited first
exit $?
