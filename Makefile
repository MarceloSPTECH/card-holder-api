local-env-create:
	docker-compose -f docker-compose.yml up -d
	sleep 3
	docker cp database/ddl.sql postgres-card-holder:/var/lib/postgresql/data
	docker exec postgres-card-holder psql -h localhost -U admin -d postgres -a -f ./var/lib/postgresql/data/ddl.sql

local-env-destroy:
	docker-compose -f docker-compose.yml down