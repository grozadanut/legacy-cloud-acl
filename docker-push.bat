set VERSION=0.0.2
docker build -t grozadanut/legacy-cloud-acl:latest -t grozadanut/legacy-cloud-acl:%VERSION% --target production .
docker push grozadanut/legacy-cloud-acl:%VERSION%
docker push grozadanut/legacy-cloud-acl:latest
PAUSE