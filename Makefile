DOCKER_COMPOSE_FILES = -f docker-compose.yml
DOCKER_COMPOSE = docker-compose ${DOCKER_COMPOSE_FILES}

run:
	${DOCKER_COMPOSE} up -d

stop:
	${DOCKER_COMPOSE} down

.PHONY: run stop

run-hw: DOCKER_COMPOSE_FILES += -f docker-compose.hello-world.yml
run-hw:
	${DOCKER_COMPOSE} up --build -d

stop-hw: DOCKER_COMPOSE_FILES += -f docker-compose.hello-world.yml
stop-hw:
	${DOCKER_COMPOSE} down

.PHONY: run-hw stop-hw

run-wq: DOCKER_COMPOSE_FILES += -f docker-compose.work-queues.yml
run-wq:
	${DOCKER_COMPOSE} up --build -d

stop-wq: DOCKER_COMPOSE_FILES += -f docker-compose.work-queues.yml
stop-wq:
	${DOCKER_COMPOSE} down

.PHONY: run-wq stop-wq

run-ps: DOCKER_COMPOSE_FILES += -f docker-compose.publish-subscribe.yml
run-ps:
	${DOCKER_COMPOSE} up --build -d

stop-ps: DOCKER_COMPOSE_FILES += -f docker-compose.publish-subscribe.yml
stop-ps:
	${DOCKER_COMPOSE} down

.PHONY: run-ps stop-ps

run-r: DOCKER_COMPOSE_FILES += -f docker-compose.routing.yml
run-r:
	${DOCKER_COMPOSE} up --build -d

stop-r: DOCKER_COMPOSE_FILES += -f docker-compose.routing.yml
stop-r:
	${DOCKER_COMPOSE} down

.PHONY: run-r stop-r

run-t: DOCKER_COMPOSE_FILES += -f docker-compose.topics.yml
run-t:
	${DOCKER_COMPOSE} up --build -d

stop-t: DOCKER_COMPOSE_FILES += -f docker-compose.topics.yml
stop-t:
	${DOCKER_COMPOSE} down

.PHONY: run-t stop-t

run-rpc: DOCKER_COMPOSE_FILES += -f docker-compose.rpc.yml
run-rpc:
	${DOCKER_COMPOSE} up --build -d --scale producer=3

stop-rpc: DOCKER_COMPOSE_FILES += -f docker-compose.rpc.yml
stop-rpc:
	${DOCKER_COMPOSE} down

.PHONY: run-rpc stop-rpc
open-web:
	@open http://localhost:15672

.PHONY: open-web