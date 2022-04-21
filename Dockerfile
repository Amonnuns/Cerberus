FROM rabbitmq:3.10.0-rc.3-management-alpine

RUN rabbitmq-plugins enable --offline rabbitmq_mqtt

EXPOSE 5672
EXPOSE 15672
