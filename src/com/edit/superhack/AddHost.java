package com.edit.superhack;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddHost extends Activity {

	String block="";
	String fs="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_host);
		Bundle bundle = getIntent().getExtras();
        block=bundle.getString("block");
        fs=bundle.getString("fs");
        setupViews();
        Button btn = (Button)findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				if(ipIsValid()&&hostNameIsValid()){
					addEntry();
				}else if(!ipIsValid()){
					msgBox("The IP address is invalid. Please correct it.");
				}else if(!hostNameIsValid()){
					msgBox("The hostname is invalid. Please correct it.");
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_host, menu);
		return true;
	}

	void setupViews(){
		
		final EditText[] textFields = new EditText[5];
		textFields[0]=(EditText)findViewById(R.id.editText1);
		textFields[1]=(EditText)findViewById(R.id.editText2);
		textFields[2]=(EditText)findViewById(R.id.editText3);
		textFields[3]=(EditText)findViewById(R.id.editText4);
		textFields[4]=(EditText)findViewById(R.id.editText5);
		
		for(int i=0;i<4;i++){
			final int ni = i;
			textFields[i].addTextChangedListener(new TextWatcher(){
	    		
	    		public void afterTextChanged(Editable s) {
	    			String in = s.toString();
	    			if(!in.equals("")){
	    				int val = Integer.parseInt(in);
	    				if(!isValidRange(val)){
	    					msgBox("Please enter a value less than 255");
	    					textFields[ni].setText("");
	    				} else if(isValidRange(val)&&in.length()==3){
	    					if(ni<4){
	    						textFields[ni+1].requestFocus();
	    					}
	    				}
	    			}
	              }

	              public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

	              public void onTextChanged(CharSequence s, int start, int before, int count) {}
	    		
	    	});
			textFields[i].setOnKeyListener(new View.OnKeyListener() {
				
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if(ni!=0){
						EditText edt = (EditText)v;
						String txt = edt.getText().toString();
						if((keyCode==KeyEvent.KEYCODE_DEL)&&(event.getAction()==KeyEvent.ACTION_UP)&&txt.equals("")){
							textFields[ni-1].requestFocus();
							textFields[ni-1].selectAll();
						}
					}
					return false;
				}
			});

		}

	}
	
	boolean isValidRange(int num){
    	boolean result=false;
    	if(num<=255){
    		result=true;
    	}
    	return result;
    }
	
	void msgBox(String message){
    	AlertDialog.Builder alert = new AlertDialog.Builder(this);
    	alert.setTitle("Alert");
    	alert.setMessage(message);
    	alert.setPositiveButton("Ok", null);
    	alert.create().show();
    }
	
	boolean ipIsValid(){
    	boolean result=false;
    	
    	EditText[] ipFields = new EditText[4];
    	ipFields[0]=(EditText)findViewById(R.id.editText1);
    	ipFields[1]=(EditText)findViewById(R.id.editText2);
    	ipFields[2]=(EditText)findViewById(R.id.editText3);
    	ipFields[3]=(EditText)findViewById(R.id.editText4);
    	String[] texts = new String[4];
    	for(int i=0;i<4;i++){
    		texts[i]=ipFields[i].getText().toString();
    	}
    	boolean[] flags = new boolean[4];
    	for(boolean flag:flags){
    		flag=false;
    		if(flag){
				
			}
    	}
    	for(int i=0;i<4;i++){
    		if(!texts[i].equals("")&&isValidRange(Integer.parseInt(texts[i]))){
    			if(i==0||i==3){
    				if((Integer.parseInt(texts[i]))>0){
    					flags[i]=true;
    				}
    			}else{
    				flags[i]=true;
    			}
    		}
    	}
    	int trueFlags=0;
    	for(boolean flag:flags){
    		if(flag==true){
    			trueFlags++;
    		}
    	}
    	if(trueFlags==4){
    		result=true;
    	}
    	return result;
    }
	
	boolean hostNameIsValid(){
    	boolean result=false;
    	EditText hn=(EditText)findViewById(R.id.editText5);
    	String hName = hn.getText().toString();
    	String[] alpha = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
    	String[] alphaUpper = new String[26];
    	String[] others = {"0","1","2","3","4","5","6","7","8","9","-",".","_"};
    	for(int i=0;i<alpha.length;i++){
    		alphaUpper[i]=alpha[i].toUpperCase(Locale.ENGLISH);
    	}
    	List<String> all = new ArrayList<String>();
    	all.addAll(Arrays.asList(alpha));
    	all.addAll(Arrays.asList(alphaUpper));
    	all.addAll(Arrays.asList(others));
    	if(!hName.equals("")){
    		boolean[] flags = new boolean[hName.length()];
    		for(boolean flag : flags){
    			flag=false;
    			if(flag){
    				
    			}
    		}
    		for(int i=0;i<hName.length();i++){
        		String curChar = hName.substring(i,i+1);
        		if(all.contains(curChar)){
        			flags[i]=true;
        		}
        		//Toast.makeText(getApplicationContext(), curChar, Toast.LENGTH_SHORT).show();
        	}
    		int trueFlags = 0;
    		for(boolean flag : flags){
    			if(flag==true){
    				trueFlags++;
    			}
    		}
    		if(trueFlags==hName.length()){
    			result=true;
    		}
    	}
    	return result;
    }
	
	void addEntry(){
		EditText[] ipFields = new EditText[5];
    	ipFields[0]=(EditText)findViewById(R.id.editText1);
    	ipFields[1]=(EditText)findViewById(R.id.editText2);
    	ipFields[2]=(EditText)findViewById(R.id.editText3);
    	ipFields[3]=(EditText)findViewById(R.id.editText4);
    	ipFields[4]=(EditText)findViewById(R.id.editText5);
    	String entry="";
    	String ip = "";
    	for(int i=0;i<4;i++){
    		if(i<3){
    			ip+=ipFields[i].getText().toString()+".";
    		}else{
    			ip+=ipFields[i].getText().toString();
    		}
    	}
    	entry=ip+" "+ipFields[4].getText().toString();
    	List<String> entries = new ArrayList<String>();
    	try{
    		Process proc = Runtime.getRuntime().exec("cat /etc/hosts");
    		BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
    		String line=null;
    		while((line=reader.readLine())!=null){
    			if(!line.equals("")){
    				entries.add(line);
    			}
    		}
    	}catch(Exception e){
    		
    	}
    	entries.add(entry);
    	try{
			Process proc = Runtime.getRuntime().exec("su");
    		DataOutputStream os = new DataOutputStream(proc.getOutputStream());
    		os.writeBytes("mount -o rw,remount -t "+fs+" "+block+" /system\n");
    		os.writeBytes("echo '' > /etc/hosts\n");
    		for(String s : entries){
    			os.writeBytes("echo '"+s+"' >> /etc/hosts\n");
    		}
    		os.writeBytes("mount -o ro,remount -t "+fs+" "+block+" /system\n");
    		os.writeBytes("exit\n");
    		os.flush();
    		os.close();
    		proc.waitFor();
		}catch(Exception e){
			
		}
    	Toast.makeText(this, "Host entry added successfully.", Toast.LENGTH_LONG).show();
		finish();
	}
	
}
