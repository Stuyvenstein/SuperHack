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
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditHost extends Activity {

	String block="";
	String fs="";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_host);
        Bundle bundle = getIntent().getExtras();
        final String entry = bundle.getString("host");
        block=bundle.getString("block");
        fs=bundle.getString("fs");
        String[] items = entry.split(",");
        String[] ipAddr = items[0].split("\\.");
        fillFields(ipAddr,items[1]);
        Button btn = (Button)findViewById(R.id.button1);
        btn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				if(ipIsValid()&&hostNameIsValid()){
					updateEntry(entry);
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
        getMenuInflater().inflate(R.menu.edit_host, menu);
        return true;
    }
    
    void fillFields(final String[] ip,String host) {
    	final EditText ip1,ip2,ip3,ip4,hn;
    	ip1=(EditText)findViewById(R.id.editText1);
    	ip2=(EditText)findViewById(R.id.editText2);
    	ip3=(EditText)findViewById(R.id.editText3);
    	ip4=(EditText)findViewById(R.id.editText4);
    	hn=(EditText)findViewById(R.id.editText5);
    	ip1.setText(ip[0]);
    	ip2.setText(ip[1]);
    	ip3.setText(ip[2]);
    	ip4.setText(ip[3]);
    	hn.setText(host);
    	
    	ip1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			public void onFocusChange(View v, boolean hasFocus) {
				EditText edt = (EditText)v;
				if(hasFocus){
					edt.selectAll();
				}
				if(edt.getText().toString().equals("")){
					edt.setText(ip[0]);
				}else if(edt.getText().toString().equals("0")){
					msgBox("The value of this field cannot be 0");
					edt.setText(ip[0]);
				}
			}
		});
    	
    	ip2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			public void onFocusChange(View v, boolean hasFocus) {
				EditText edt = (EditText)v;
				if(hasFocus){
					edt.selectAll();
				}
				if(edt.getText().toString().equals("")){
					edt.setText(ip[1]);
				}
			}
		});
    	
    	ip3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			public void onFocusChange(View v, boolean hasFocus) {
				EditText edt = (EditText)v;
				if(hasFocus){
					edt.selectAll();
				}
				if(edt.getText().toString().equals("")){
					edt.setText(ip[2]);
				}
			}
		});
    	
    	ip4.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			public void onFocusChange(View v, boolean hasFocus) {
				EditText edt = (EditText)v;
				if(hasFocus){
					edt.selectAll();
				}
				if(edt.getText().toString().equals("")){
					edt.setText(ip[3]);
				}else if(edt.getText().toString().equals("0")){
					msgBox("The value of this field cannot be 0");
					edt.setText(ip[3]);
				}
			}
		});
    	
    	hn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					EditText edt = (EditText)v;
					edt.selectAll();
				}
			}
		});
    	
    	ip1.requestFocus();
    	
    	ip1.addTextChangedListener(new TextWatcher(){
    		
    		public void afterTextChanged(Editable s) {
    			String in = s.toString();
    			if(!in.equals("")){
    				int val = Integer.parseInt(in);
    				if(!isValidRange(val)){
    					msgBox("Please enter a value less than 255");
    					ip1.setText("");
    				} else if(isValidRange(val)&&in.length()==3){
    					ip2.requestFocus();
    				}
    			}
              }

              public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

              public void onTextChanged(CharSequence s, int start, int before, int count) {}
    		
    	});
    	
    	ip2.addTextChangedListener(new TextWatcher(){
    		
    		public void afterTextChanged(Editable s) {
    			String in = s.toString();
    			if(!in.equals("")){
    				int val = Integer.parseInt(in);
    				if(!isValidRange(val)){
    					msgBox("Please enter a value less than 255");
    					ip2.setText("");
    				} else if(isValidRange(val)&&in.length()==3){
    					ip3.requestFocus();
    				}
    			}
              }

              public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

              public void onTextChanged(CharSequence s, int start, int before, int count) {}
    		
    	});
    	
    	ip3.addTextChangedListener(new TextWatcher(){
    		
    		public void afterTextChanged(Editable s) {
    			String in = s.toString();
    			if(!in.equals("")){
    				int val = Integer.parseInt(in);
    				if(!isValidRange(val)){
    					msgBox("Please enter a value less than 255");
    					ip3.setText("");
    				} else if(isValidRange(val)&&in.length()==3){
    					ip4.requestFocus();
    				}
    			}
              }

              public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

              public void onTextChanged(CharSequence s, int start, int before, int count) {}
    		
    	});
    	
    	ip4.addTextChangedListener(new TextWatcher(){
    		
    		public void afterTextChanged(Editable s) {
    			String in = s.toString();
    			if(!in.equals("")){
    				int val = Integer.parseInt(in);
    				if(!isValidRange(val)){
    					msgBox("Please enter a value less than 255");
    					ip4.setText("");
    				}else if(isValidRange(val)&&in.length()==3){
    					hn.requestFocus();
    				}
    			}
              }

              public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

              public void onTextChanged(CharSequence s, int start, int before, int count) {}
    		
    	});    	
    	
    }
    
    void msgBox(String message){
    	AlertDialog.Builder alert = new AlertDialog.Builder(this);
    	alert.setTitle("Alert");
    	alert.setMessage(message);
    	alert.setPositiveButton("Ok", null);
    	alert.create().show();
    }
    
    boolean isValidRange(int num){
    	boolean result=false;
    	if(num<=255){
    		result=true;
    	}
    	return result;
    }

    boolean ipIsValid(){
    	boolean result=false;
    	EditText ip1,ip2,ip3,ip4;
    	ip1=(EditText)findViewById(R.id.editText1);
    	ip2=(EditText)findViewById(R.id.editText2);
    	ip3=(EditText)findViewById(R.id.editText3);
    	ip4=(EditText)findViewById(R.id.editText4);
    	String v1,v2,v3,v4;
    	v1 = ip1.getText().toString();
    	v2 = ip2.getText().toString();
    	v3 = ip3.getText().toString();
    	v4 = ip4.getText().toString();
    	int val1,val4;
    	if(!v1.equals("")&&!v2.equals("")&&!v3.equals("")&&!v4.equals("")){
    		val1=Integer.parseInt(v1);
        	val4=Integer.parseInt(v4);
    		if(val1>0&&val4>0){
    			result=true;
    		}
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
    
    void updateEntry(String oldEntry){
    	String[] splitOld = oldEntry.split(",");
    	oldEntry = splitOld[0]+" "+splitOld[1];
    	EditText ip1,ip2,ip3,ip4,hn;
    	ip1=(EditText)findViewById(R.id.editText1);
    	ip2=(EditText)findViewById(R.id.editText2);
    	ip3=(EditText)findViewById(R.id.editText3);
    	ip4=(EditText)findViewById(R.id.editText4);
    	hn=(EditText)findViewById(R.id.editText5);
    	String newEntry=ip1.getText().toString()+"."+ip2.getText().toString()+"."+ip3.getText().toString()+"."+ip4.getText().toString()+" "+hn.getText().toString();
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
    	for(int i=0;i<entries.size();i++){
    		if(entries.get(i).contains(oldEntry)){
    			entries.set(i, newEntry);
    		}
    	}
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
    	Toast.makeText(this, "Host entry updated successfully.", Toast.LENGTH_LONG).show();
		finish();
    }

}
