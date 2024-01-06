#!/bin/bash

set -m

java -jar server.jar --port 8080 &
serve -s client_src -l 8081 &
/usr/sbin/nginx -c /app/nginx.conf

# Wait for any process to exit
wait -n

# Exit with status of process that exited first
exit $?
