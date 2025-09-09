docker command to create container with postgresql
docker run -d --name postgres-container -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=sdawra2183alkjfs -e POSTGRES_DB=postgres -p 5432:5432 postgres:latest
