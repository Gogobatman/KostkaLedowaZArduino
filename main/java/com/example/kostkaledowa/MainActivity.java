package com.example.kostkaledowa;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.UUID;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

    TextView t1;

    String address = null , name=null;

    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    Set<BluetoothDevice> pairedDevices;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    final int[][][] idArray = {
            {
                    {R.id.radioButton111, R.id.radioButton211, R.id.radioButton311, R.id.radioButton411},
                    {R.id.radioButton121, R.id.radioButton221, R.id.radioButton321, R.id.radioButton421},
                    {R.id.radioButton131, R.id.radioButton231, R.id.radioButton331, R.id.radioButton431},
                    {R.id.radioButton141, R.id.radioButton241, R.id.radioButton341, R.id.radioButton441},
            },
            {
                    {R.id.radioButton112, R.id.radioButton212, R.id.radioButton312, R.id.radioButton412},
                    {R.id.radioButton122, R.id.radioButton222, R.id.radioButton322, R.id.radioButton422},
                    {R.id.radioButton132, R.id.radioButton232, R.id.radioButton332, R.id.radioButton432},
                    {R.id.radioButton142, R.id.radioButton242, R.id.radioButton342, R.id.radioButton442},
            },
            {
                    {R.id.radioButton113, R.id.radioButton213, R.id.radioButton313, R.id.radioButton413},
                    {R.id.radioButton123, R.id.radioButton223, R.id.radioButton323, R.id.radioButton423},
                    {R.id.radioButton133, R.id.radioButton233, R.id.radioButton333, R.id.radioButton433},
                    {R.id.radioButton143, R.id.radioButton243, R.id.radioButton343, R.id.radioButton443},
            },
            {
                    {R.id.radioButton114, R.id.radioButton214, R.id.radioButton314, R.id.radioButton414},
                    {R.id.radioButton124, R.id.radioButton224, R.id.radioButton324, R.id.radioButton424},
                    {R.id.radioButton134, R.id.radioButton234, R.id.radioButton334, R.id.radioButton434},
                    {R.id.radioButton144, R.id.radioButton244, R.id.radioButton344, R.id.radioButton444},
            }
    };
    final String buttonCode[][][] = {
            {
                    {"r", "g", "b", "o"},
                    {"q", "q", "q", "q"},
                    {"q", "q", "q", "q"},
                    {"q", "q", "q", "q"},
            },
            {
                    {"q", "q", "q", "q"},
                    {"q", "q", "q", "q"},
                    {"q", "q", "q", "q"},
                    {"q", "q", "q", "q"},
            },
            {
                    {"q", "q", "q", "q"},
                    {"q", "q", "q", "q"},
                    {"q", "q", "q", "q"},
                    {"q", "q", "q", "q"},
            },
            {
                    {"q", "q", "q", "q"},
                    {"q", "q", "q", "q"},
                    {"q", "q", "q", "q"},
                    {"q", "q", "q", "q"},
            }
    };
    static Button radiobutton[][][] = new Button[10][10][10];
    char matrix[][][] = new char[10][10][10];
    boolean win=false;
    boolean connected=false;

    public void declareButtons() {
        for (int z = 0; z < 4; z++) {
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 4; x++) {
                    radiobutton[z][y][x] = (Button) findViewById(idArray[z][y][x]);
                    radiobutton[z][y][x].setBackgroundColor(Color.parseColor("#D3D3D3"));
                }
            }
        }
    }
    public void cleanMatrix() {
        for (int z = 0; z < 4; z++) {
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 4; x++) {
                    matrix[z][y][x] = ' ';
                }
            }
        }
    }

    public void checkIfWin(char znak) {
        for (int z = 0; z < 4; z++) {
            for (int y = 0; y < 4; y++) {
                for (int x = 0; x < 4; x++) {
                    if (((matrix[z][y][x] == znak) && (matrix[z][y][x + 1] == znak) &&
                            (matrix[z][y][x + 2] == znak) && (matrix[z][y][x + 3] == znak)) ||
                            ((matrix[z][y][x] == znak) && (matrix[z][y + 1][x] == znak) &&
                                    (matrix[z][y + 2][x] == znak) && (matrix[z][y + 3][x] == znak)) ||
                            ((matrix[z][y][x] == znak) && (matrix[z + 1][y][x] == znak) &&
                                    (matrix[z + 2][y][x] == znak) && (matrix[z + 3][y][x] == znak)) ||
                            (((matrix[z][y][x] == znak) && (matrix[z + 1][y + 1][x + 1] == znak) &&
                                    (matrix[z + 2][y + 2][x + 2] == znak) && (matrix[z + 3][y + 3][x + 3] == znak))) ||
                            (((matrix[z][y][x + 3] == znak) && (matrix[z + 1][y + 1][x + 2] == znak) &&
                                    (matrix[z + 2][y + 2][x + 1] == znak) && (matrix[z + 3][y + 3][x] == znak))) ||
                            (((matrix[z][y + 3][x] == znak) && (matrix[z + 1][y + 2][x + 1] == znak) &&
                                    (matrix[z + 2][y + 1][x + 2] == znak) && (matrix[z + 3][y][x + 3] == znak))) ||
                            (((matrix[z + 3][y][x] == znak) && (matrix[z + 2][y + 1][x + 1] == znak) &&
                                    (matrix[z + 1][y + 2][x + 2] == znak) && (matrix[z][y + 3][x + 3] == znak)))) {
                        win = true;
                        //textView.setText("wygrales " + znak);
                    }

                }
            }
        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        declareButtons();
        cleanMatrix();
        try {setw();} catch (Exception e) {}
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setw() throws IOException
    {
        t1=(TextView)findViewById(R.id.textView1);
        bluetooth_connect_device();



        radiobutton[0][0][0]=(Button)findViewById(idArray[0][0][0]);

        radiobutton[0][0][0].setOnTouchListener(new View.OnTouchListener()
        {   @Override
        public boolean onTouch(View v, MotionEvent event){
                led_on_off(buttonCode[0][0][0]);
                radiobutton[0][0][0].setBackgroundColor(Color.RED);
            return true;}
        });

        radiobutton[0][0][1]=(Button)findViewById(idArray[0][0][1]);

        radiobutton[0][0][1].setOnTouchListener(new View.OnTouchListener()
        {   @Override
        public boolean onTouch(View v, MotionEvent event){
            led_on_off(buttonCode[0][0][1]);
            radiobutton[0][0][1].setBackgroundColor(Color.RED);
            return true;}
        });

        radiobutton[0][0][2]=(Button)findViewById(idArray[0][0][2]);

        radiobutton[0][0][2].setOnTouchListener(new View.OnTouchListener()
        {   @Override
        public boolean onTouch(View v, MotionEvent event){
            led_on_off(buttonCode[0][0][2]);
            radiobutton[0][0][2].setBackgroundColor(Color.RED);
            return true;}
        });

    }


    private void bluetooth_connect_device() throws IOException
    {
        try
        {
            myBluetooth = BluetoothAdapter.getDefaultAdapter();
            address = myBluetooth.getAddress();
            pairedDevices = myBluetooth.getBondedDevices();
            if (pairedDevices.size()>0)
            {
                for(BluetoothDevice bt : pairedDevices)
                {
                    address=bt.getAddress().toString();name = bt.getName().toString();
                    Toast.makeText(getApplicationContext(),"Connected", Toast.LENGTH_SHORT).show();

                }
            }

        }
        catch(Exception we){}
        myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
        BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
        btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
        btSocket.connect();
        try { t1.setText("BT Name: "+name+"\nBT Address: "+address); }
        catch(Exception e){}
    }

    @Override
    public void onClick(View v)
    {
        try
        {

        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

    private void led_on_off(String i)
    {
        try
        {
            if (btSocket!=null)
            {

                btSocket.getOutputStream().write(i.toString().getBytes());
            }

        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();

        }

    }

}
