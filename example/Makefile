.DEFAULT: _all

_all: up sleep kafka-topics influxdb-setup sleep kafka-connect-register

clean:
	@docker-compose down -v

up:
	@docker-compose up -d

sleep:
	@sleep 10

influxdb-setup:
	docker-compose exec influxdb influx setup --force --org connect --bucket connect --retention 0 --token connect --username connect --password connect1234

kafka-topics:
	@docker-compose exec kafka kafka-topics --zookeeper=zookeeper:2181 --create --topic influxdb-poc-config  --partitions 1 --replication-factor 1 --if-not-exists
	@docker-compose exec kafka kafka-topics --zookeeper=zookeeper:2181 --create --topic influxdb-poc-offsets --partitions 1 --replication-factor 1 --if-not-exists
	@docker-compose exec kafka kafka-topics --zookeeper=zookeeper:2181 --create --topic influxdb-poc-status  --partitions 1 --replication-factor 1 --if-not-exists
	@docker-compose exec kafka kafka-topics --zookeeper=zookeeper:2181 --create --topic points  --partitions 1 --replication-factor 1 --if-not-exists

kafka-connect-register:
	@curl -X POST http://localhost:8083/connectors \
    	-H 'Content-Type:application/json' \
    	-H 'Accept:application/json' \
    	-d @./kafka-connect-influxdb.json

write-to-kafka:
	@docker-compose exec kafka bash -c "kafka-console-producer --broker-list kafka:9092 --topic points < /data/sample-points.txt"
