#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <LiquidCrystal_I2C.h>
#include <ConstantDefs.h>
#include <string>

WiFiClient nodemcuClient;
PubSubClient client(nodemcuClient);
LiquidCrystal_I2C lcd(endereco, colunas, linhas);

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

char* convertBytetoString(byte* payload, unsigned int length, char* message){
  
  for (auto i = 0; i < length; i++) {
    message[i] = (char)payload[i];
  }
  message[length] = 0;
  return message;
}
void writeMessage(char* message){
  lcd.clear();
  lcd.home();
  char str[16+1];
  strncpy(str,message, 16);
  str[16]=0;
  lcd.print(str);
  lcd.setCursor(0,1);
  strncpy(str,message+16, 16);
  str[16]=0;
  lcd.print(str);
}

void callback(const char* topic, byte* payload, unsigned int length){
  char message[length + 1];
  char* message_return;

  message_return = convertBytetoString(payload, length, message);
  writeMessage(message);
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
  lcd.init();
  lcd.backlight();
  lcd.clear();
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