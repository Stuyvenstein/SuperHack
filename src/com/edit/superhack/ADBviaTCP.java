package com.edit.superhack;

import java.io.DataOutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class ADBviaTCP extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adb_via_tcp);
        RadioButton rb1,rb2;
        rb1=(RadioButton)findViewById(R.id.radio0);
        rb2=(RadioButton)findViewById(R.id.radio1);
        rb1.setId(1);
        rb2.setId(2);
        rb1.setChecked(true);
        RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroup1);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				refreshView();
			}
		});
        Button setBtn = (Button)findViewById(R.id.button1);
        setBtn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				setADBPort();
			}
		});
        
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	refreshView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.adb_via_tcp, menu);
        return true;
    }
    
    void refreshView(){
    	RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroup1);
    	EditText portIn = (EditText)findViewById(R.id.editText1);
    	int rbId = rg.getCheckedRadioButtonId();
    	if(rbId==1){
    		portIn.setEnabled(true);
    	} else if(rbId==2){
    		portIn.setEnabled(false);
    	}
    }
    
    void setADBPort(){
    	RadioGroup rg = (RadioGroup)findViewById(R.id.radioGroup1);
    	int rbId = rg.getCheckedRadioButtonId();
    	if(rbId==1){
    		EditText portIn = (EditText)findViewById(R.id.editText1);
    		if(!portIn.getText().toString().equals("")){
    			int portVal = Integer.parseInt(portIn.getText().toString());
    			if(portVal<5000){
    				msgBox("Please enter a port number no less than 5000");
    			} else{
    				try{
    	    			Process proc = Runtime.getRuntime().exec("su");
    	    			DataOutputStream os = new DataOutputStream(proc.getOutputStream());
    	    			os.writeBytes("setprop service.adb.tcp.port "+portVal+"\n");
    	    			os.writeBytes("stop adbd\n");
    	    			os.writeBytes("start adbd\n");
    	    			os.writeBytes("exit\n");
    	    			os.flush();
    	    			os.close();
    	    			proc.waitFor();
    	    			makeAlert(portVal);
    	    		}catch(Exception e){
    	    			msgBox("Exception: "+e.toString());
    	    		}
    			}
    		} else {
    			msgBox("Please enter a port number");
    		}
    	} else if(rbId==2){
    		try{
    			Process proc = Runtime.getRuntime().exec("su");
    			DataOutputStream os = new DataOutputStream(proc.getOutputStream());
    			os.writeBytes("setprop service.adb.tcp.port -1\n");
    			os.writeBytes("stop adbd\n");
    			os.writeBytes("start adbd\n");
    			os.writeBytes("exit\n");
    			os.flush();
    			os.close();
    			proc.waitFor();
    			makeAlert(0);
    		}catch(Exception e){
    			msgBox("Exception: "+e.toString());
    		}
    	}
    }
    
    void msgBox(String message){
    	AlertDialog.Builder alert = new AlertDialog.Builder(this);
    	alert.setTitle("Alert");
    	alert.setMessage(message);
    	alert.setPositiveButton("Ok", null);
    	alert.create().show();
    }
    
    void makeAlert(int num){
    	String msg = "";
    	if(num==0){
    		msg="ADB set to run via USB";
    	} else{
    		msg="ADB set to run via TCP/IP using port: "+num;
    	}
    	Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
		finish();
    }
    
}
