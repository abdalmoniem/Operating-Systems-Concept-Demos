void setup() {
  // Open serial communications and wait for port to open:
  Serial.begin(115200);
  pinMode(2, INPUT);
  attachInterrupt(digitalPinToInterrupt(2), change_mode, RISING);
  pinMode(3, OUTPUT);
  pinMode(4, OUTPUT);
  pinMode(5, OUTPUT);
}

boolean mode = false;

void change_mode() {
  mode = !mode;
  delay(50);
}
void loop() {
  while(!mode) { //output
    digitalWrite(3, 0);
    digitalWrite(4, 1);
    digitalWrite(5, 0);
    int value = analogRead(A0);
    Serial.println(value);
    delay(400);
  }

  digitalWrite(4, 0);
  digitalWrite(5, 1);
  if(Serial.available() > 0) {
    String val = Serial.readString();
    analogWrite(3, val.toInt());
  }
}
