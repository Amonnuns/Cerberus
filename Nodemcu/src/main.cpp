#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <ConstantDefs.h>
#include <string>

WiFiClient nodemcuClient;
PubSubClient client(nodemcuClient);

void connect_wifi(){
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

char* convertBytetoString(byte* payload){
  char str[(sizeof payload) + 1];
  memcpy(str, payload, sizeof payload);
  str[sizeof payload] = 0; // Null termination.

  return str;
}

void callback(const char* topic, byte* payload, unsigned int length){
  Serial.println("Message received from topic:");
  Serial.print(topic);
  char* message = convertBytetoString(payload);
  Serial.println(message);
}


void reconnect(){
  Serial.println("Reconecting...");
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if (client.connect("Nodemcu", mqtt_user, mqtt_pass)) {
      Serial.println("connected");
      if(!client.subscribe(topic)){
    Serial.println("It was not possible to subscribe");
    }
    } else {
      Serial.print("Connection Failed, rc=");
      Serial.print(client.state());
      Serial.println("trying again in 5 seconds");
      delay(5000);
    }
  }
}

void setup() {
  Serial.begin(115200);
  connect_wifi();
  client.setServer(mqtt_server, 1883);
  client.setCallback(callback);
  client.connect("Nodemcu", mqtt_user, mqtt_pass);
  if(!client.subscribe(topic)){
    Serial.println("It was not possible to subscribe");
  }

}

void loop() {

  if(!client.connected()){
    reconnect();
  }
  client.loop();

}