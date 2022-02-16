DOCKER_COMPOSE_FILES = -f docker-compose.yml
DOCKER_COMPOSE = docker-compose ${DOCKER_COMPOSE_FILES}

run:
	${DOCKER_COMPOSE} up -d

stop:
	${DOCKER_COMPOSE} down

run-hw: DOCKER_COMPOSE_FILES += -f docker-compose.hello-world.yml
run-hw:
	${DOCKER_COMPOSE} up --build -d

stop-hw: DOCKER_COMPOSE_FILES += -f docker-compose.hello-world.yml
stop-hw:
	${DOCKER_COMPOSE} down

open-web:
	@open http://localhost:15672