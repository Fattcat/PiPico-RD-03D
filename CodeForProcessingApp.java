import processing.serial.*;

Serial radarPort;
float[] angle = new float[3];
float[] distance = new float[3];
float[] speed = new float[3];

color[] targetColor = { color(255,0,0), color(0,128,255), color(180,0,255) }; // red, blue, purple

int radarRadius;
PFont radarFont;

void setup() {
  size(1600, 900);
  radarRadius = height - 150;
  radarFont = createFont("Arial", 16);
  textFont(radarFont);
  radarPort = new Serial(this, "COM14", 115200); // !!! HERE CHANGE PORT !!!
  radarPort.bufferUntil('\n');
  
  for (int i=0;i<3;i++) {
    angle[i]=0;
    distance[i]=0;
    speed[i]=0;
  }
}

void draw() {
  background(0);
  translate(width/2, height - 70);
  drawRadarDisplay();
  // Draw all three targets
  for (int i=0;i<3;i++) {
    displayTarget(angle[i], distance[i], speed[i], targetColor[i], i);
  }
}

void serialEvent(Serial port) {
  String in = port.readStringUntil('\n');
  if(in != null){
    in = trim(in);
    String[] values = split(in, ',');
    if(values.length == 9){
      for (int i=0; i<3; i++) {
        angle[i] = float(values[i*3]);
        distance[i] = float(values[i*3+1]);
        speed[i] = float(values[i*3+2]);
      }
    }
  }
}

void drawRadarDisplay(){
  stroke(0, 255, 0);
  strokeWeight(1);
  noFill();
  for(int r = radarRadius/4; r <= radarRadius; r += radarRadius/4) {
    arc(0, 0, r*2, r*2, radians(-150), radians(-30));
  }
  int[] angles = {-60, -45, -30, -15, 0, 15, 30, 45, 60};
  for(int i = 0; i < angles.length; i++){
    float currentAngle = angles[i];
    float rad = radians(currentAngle - 90);
    float x = radarRadius * cos(rad);
    float y = radarRadius * sin(rad);
    strokeWeight(1);
    stroke(0, 255, 0);
    line(0, 0, x, y);
    fill(0, 255, 0);
    noStroke();
    textAlign(CENTER, CENTER);
    textSize(14);
    float tx = (radarRadius + 20) * cos(rad);
    float ty = (radarRadius + 20) * sin(rad);
    text(currentAngle + "°", tx, ty);
  }
  stroke(0,255,0);
  line(0,0,0,-radarRadius);
}

void displayTarget(float angleDeg, float distanceMM, float spd, color dotColor, int index){
  if(distanceMM <= 8000){
    float scaledDist = map(distanceMM, 0, 8000, 0, radarRadius);
    float rad = radians(angleDeg - 90);
    float x = scaledDist * cos(rad);
    float y = scaledDist * sin(rad);
    fill(dotColor);
    noStroke();
    ellipse(x, y, 14, 14);
    
    // Display info for each target at separate vertical offsets
    fill(dotColor);
    textSize(16);
    textAlign(LEFT, CENTER);
    float yOffset = -radarRadius - 50 + 55*index;
    float spacing = 18;
    text("Target " + (index+1), -width/2 + 20, yOffset-10);
    fill(255);
    text("Angle: " + nf(angleDeg,1,1) + "°",    -width/2 + 40, yOffset + 0*spacing);
    text("Distance: " + nf(distanceMM/1000.0,1,2) + " m", -width/2 + 40, yOffset + 1*spacing);
    text("Speed: " + nf(spd,1,2) + " cm/s",      -width/2 + 40, yOffset + 2*spacing);
  }
}
