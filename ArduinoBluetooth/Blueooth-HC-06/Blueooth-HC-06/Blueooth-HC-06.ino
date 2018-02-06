String cadena;  // incoming data
int  LED = 13;      // LED pin
 
void setup() {
  Serial.begin(9600); // initialization
  pinMode(LED, OUTPUT);
}
 
void loop() {
  if (Serial.available() > 0) {  // if the data came
    cadena = Serial.readString(); // read byte


    String pin, valor;
    for (int i = 0; i < cadena.length(); i++) {
      if (cadena.substring(i, i+1) == ":") {
        pin = cadena.substring(0, i);
        valor= cadena.substring(i+1);
        break;
      }
    }

    if(pin == "13") {
      if(valor=="1"){
        digitalWrite(LED, HIGH); 
        Serial.println("Led ON");
      }else if(valor=="0"){
         digitalWrite(LED, LOW);  
         Serial.println("Led OFF");
      }
    }
  }
}
