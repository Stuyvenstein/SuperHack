package com.edit.superhack;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class SuperHack extends Activity {
	String fsBlock="";
	String fsType="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.super_hack);
        if(!rooted()){
        	AlertDialog.Builder alert = new AlertDialog.Builder(this);
        	alert.setTitle("Not rooted");
        	alert.setCancelable(false);
        	alert.setMessage("This app only works on rooted devices.");
        	alert.setPositiveButton("Close", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface arg0, int arg1) {
					finish();
				}
			});
        	alert.create().show();
        }
        Button adb,hosts;
        adb=(Button)findViewById(R.id.button1);
        hosts=(Button)findViewById(R.id.button2);
        
        adb.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				Intent i = new Intent(SuperHack.this,ADBviaTCP.class);
				startActivity(i);
			}
		});
        
        hosts.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				Intent i = new Intent(SuperHack.this,HostsEditor.class);
				i.putExtra("fsBlock", fsBlock);
				i.putExtra("fsType", fsType);
				startActivity(i);
			}
		});
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.super_hack, menu);
        return true;
    }
    
    boolean rooted(){
    	boolean result = false;
    	String block="";
    	String fs="";
    	try{
    		boolean found=false;
    		Process proc = Runtime.getRuntime().exec("cat /proc/mounts");
    		BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
    		String line=null;
    		while((line=reader.readLine())!=null&&found==false){
    			if(isSystemMount(line)){
    				block=getBlock(line);
    				fs=getFs(line);
    				found=true;
    			}
    		}
    	}catch(Exception e){
    		
    	}
    	try{
    		Process proc = Runtime.getRuntime().exec("su");
    		DataOutputStream os = new DataOutputStream(proc.getOutputStream());
    		os.writeBytes("mount -o rw,remount -t "+fs+" "+block+" /system\n");
    		os.writeBytes("echo 'rooted' > /system/etc/supercheck.txt\n");
    		os.writeBytes("mount -o ro,remount -t "+fs+" "+block+" /system\n");
    		os.writeBytes("exit\n");
    		os.flush();
    		os.close();
    		proc.waitFor();
    	}catch(Exception e){
    		
    	}
    	try{
    		Process proc = Runtime.getRuntime().exec(new String[]{"su","-c","cat /system/etc/supercheck.txt"}); //"cat /system/etc/supercheck.txt"
    	
    		BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
    		String line=null;
    		while((line=reader.readLine())!=null){
    			if(line.equals("rooted")){
    				result=true;
    				
    			}
    		}
    		
    		if(result==true){
    			deleterFile(block,fs);
    		}
    		
    	} catch(Exception e){
    		
    	}
    	return result;
    }
    
    boolean isSystemMount(String line){
    	boolean result = false;
    	if(line.contains(" /system ")){
    		result=true;
    	}
    	return result;
    }
    
    String getBlock(String line){
    	String block="";
    	int pos = line.lastIndexOf(" /system ");
    	block=line.substring(0,pos);
    	fsBlock=block;
    	return block;
    }
    
    String getFs(String line){
    	String fs="";
    	String[] split = line.split(" /system ");
    	int pos = split[1].indexOf(" ");
    	fs = split[1].substring(0,pos);
    	fsType=fs;
    	return fs;
    }
    
    void deleterFile(String block,String fs){
    	try{
    		Process proc = Runtime.getRuntime().exec("su");
    		DataOutputStream os = new DataOutputStream(proc.getOutputStream());
    		os.writeBytes("mount -o rw,remount -t "+fs+" "+block+" /system\n");
    		os.writeBytes("rm /system/etc/supercheck.txt\n");
    		os.writeBytes("mount -o ro,remount -t "+fs+" "+block+" /system\n");
    		os.writeBytes("exit\n");
    		os.flush();
    		os.close();
    		proc.waitFor();
    	}catch(Exception e){
    		
    	}
    }
    
}
