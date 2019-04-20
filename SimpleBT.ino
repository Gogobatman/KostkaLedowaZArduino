char data = 0;                //Variable for storing received data
void setup() 
{
Serial.begin(9600);         //Sets the data rate in bits per second (baud) for serial data transmission
pinMode(13, OUTPUT);        //Sets digital pin 13 as output pin
}
void loop()
{
if(Serial.available() > 0)  // Send data only when you receive data:
{

data = Serial.read();      //Read the incoming data and store it into variable data
Serial.print(data);        //Print Value inside data in Serial monitor
Serial.print("\n"); 
//New line         
 if (data == 'f') {          
  digitalWrite(LED_BUILTIN, HIGH);   // turn the LED on (HIGH is the voltage level)
  delay(1000);                       // wait for a second
  digitalWrite(LED_BUILTIN, LOW);    // turn the LED off by making the voltage LOW
  delay(1000);  
 }
}
}

//TO DZIALA NA NP PORTACH 2 I 3 DLA TERMINALA W ARDUINO I DLA RX I TX DLA APKI BLUETOOTH ALE NIE MOJEJ
//APLIKACJA TEST DZIALA!!!
