import processing.serial.*;
import java.io.InputStreamReader;

Serial port;  // Create object from Serial class
int val;      // Data received from the serial port
int[] values;
float zoom;

void setup() {
  size(700, 500);
  surface.setResizable(true);
  System.out.println(args);
  //port = new Serial(this, Serial.list()[0], 115200);
  port = new Serial(this, args[0], 9600);
  values = new int[width];
  zoom = 1.0f;

  smooth();
}

int getY(int val) {
  int x = (int)(height - val / 1200.0f * (height - 1));
  x = x == height ? height -10 : x;
  return x;
}

int getValue() {
  int value = -1;
  while (port.available() >= 3) {
    if (port.read() == 0xff) {
      value = (port.read() << 8) | (port.read());
    }
  }
  return value;
}

void pushValue(int value) {
  for (int i=0; i<width-1; i++)
    values[i] = values[i+1];
  values[width-1] = value;
}

void drawLines() {
  stroke(0);

  int displayWidth = (int) (width / zoom);

  int k = values.length - displayWidth;

  int x0 = 0;
  int y0 = getY(values[k]);
  for (int i=1; i<displayWidth; i++) {
    k++;
    int x1 = (int) (i * (width-1) / (displayWidth-1));
    int y1 = getY(values[k]);
    line(x0, y0, x1, y1);
    x0 = x1;
    y0 = y1;
  }
}

void drawGrid() {
  stroke(255, 0, 0);
  strokeWeight(4);
  line(0, height/2, width, height/2);

  PFont Title;
  Title = createFont("Georgia", 32);
  textFont(Title);
  textAlign(CENTER, CENTER);
  fill(0);
  text("Oscilloscope", width/2, 15);
}

void draw() {
  background(255);
  drawGrid();
  val = getValue();
  PFont Title;
  Title = createFont("Georgia", 20);
  textFont(Title);
  textAlign(CENTER, CENTER);

  if (val != -1) {
    float volts = map(val, 0, 1023, 0, 5);
    fill(0);
    text(String.format("Voltage: %.3f Volts", volts), 100, 30);
    pushValue(val);
  }
  drawLines();
}