const int mode_select_btn = 2;
const int brightness_led = 3;
const int graphing_mode_led = 4;
const int brightness_mode_led = 5;
char mode = 0;

//Debouncing Time in Milliseconds
long debouncing_time = 300;
volatile unsigned long last_millis;

void setup() {
  Serial.begin(115200);
  pinMode(mode_select_btn, INPUT_PULLUP);
  attachInterrupt(digitalPinToInterrupt(mode_select_btn),
                  debounce_interrupt, FALLING);
  pinMode(brightness_led, OUTPUT);
  pinMode(graphing_mode_led, OUTPUT);
  pinMode(brightness_mode_led, OUTPUT);

digitalWrite(brightness_led, 0);        //turn off brightness led
    digitalWrite(graphing_mode_led, 0);     //turn off graphing mode led
    digitalWrite(brightness_mode_led, 1);   //turn on brightness mode led
}

void debounce_interrupt() {
  if ((long)(millis() - last_millis) >= debouncing_time) {
    change_mode();
    last_millis = millis();
  }
}

void change_mode() {
  mode = mode == 3 ? 0 : mode + 1;
  if (mode == 0) {
    digitalWrite(brightness_led, 0);        //turn off brightness led
    digitalWrite(graphing_mode_led, 0);     //turn off graphing mode led
    digitalWrite(brightness_mode_led, 1);   //turn on brightness mode led
  } else if (mode == 1) {
    digitalWrite(brightness_led, 0);        //turn off brightness led
    digitalWrite(graphing_mode_led, 1);     //turn on graphing mode led
    digitalWrite(brightness_mode_led, 0);   //turn off brightness mode led
  } else if (mode == 2) {
    digitalWrite(brightness_led, 0);        //turn off brightness led
    digitalWrite(graphing_mode_led, 1);     //turn on graphing mode led
    digitalWrite(brightness_mode_led, 1);   //turn on brightness mode led
  }
}

void loop() {
  if (mode == 0) { //input mode activated
    if (Serial.available() > 0) {
      int brightness = Serial.parseInt();
      analogWrite(brightness_led, brightness);
    }
  } else if (mode == 1) { //slow output mode activated
    int val = analogRead(A0);
    Serial.println(val);
    
    delay(400);
  } else if (mode == 2) { //fast output mode activated
    int val = analogRead(A0);
    Serial.write(0xff);
    Serial.write((val >> 8) & 0xff);
    Serial.write(val & 0xff);
  }
}
