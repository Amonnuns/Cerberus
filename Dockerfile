FROM rabbitmq:3.10-rc-alpine

RUN rabbitmq-plugins enable --offline rabbitmq_mqtt rabbitmq_federation_management

EXPOSE 5672
EXPOSE 15672
