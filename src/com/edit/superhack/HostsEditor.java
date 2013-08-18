package com.edit.superhack;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HostsEditor extends Activity {
String block="";
String fs="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hosts_editor);
        Bundle bundle = getIntent().getExtras();
        block=bundle.getString("fsBlock");
        fs=bundle.getString("fsType");
        ImageButton btn = (ImageButton)findViewById(R.id.imageButton1);
        btn.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				addHost();
			}
		});
    }

    @Override
    public void onResume(){
    	super.onResume();
    	makeHostsList();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hosts_editor, menu);
        return true;
    }
    
    void msgBox(String message){
    	AlertDialog.Builder alert = new AlertDialog.Builder(this);
    	alert.setTitle("Alert");
    	alert.setMessage(message);
    	alert.setPositiveButton("Ok", null);
    	alert.create().show();
    }
    
    void makeHostsList(){
    	LinearLayout parent = (LinearLayout)findViewById(R.id.parent);
    	parent.removeAllViews();
    	List<String> entries = new ArrayList<String>();
    	try{
    		Process process = Runtime.getRuntime().exec("cat /etc/hosts");
    		InputStream in = process.getInputStream();
    		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    		String line=null;
    		while((line=reader.readLine())!=null){
    			if(!line.equals("")&&isValidEntry(line)){
    				if(entries.size()<500){
    					entries.add(line);
    				}
    			}
    		}
    	}catch(Exception e){
    		msgBox(e.toString());
    	}
    	if(entries.size()>0){
    		
    		for(String s : entries){
    			makeListItem(s);
    		}
    	}
    }
    
    void makeListItem(String entry){
    	String[] items = entry.split(" ");
    	LinearLayout parent = (LinearLayout)findViewById(R.id.parent);
    	LayoutInflater li = LayoutInflater.from(this);
    	LinearLayout child = (LinearLayout)li.inflate(R.layout.host_entry_item, null,false);
    	TextView tv1 = (TextView)child.findViewById(R.id.textView1);
    	TextView tv2 = (TextView)child.findViewById(R.id.textView2);
    	ImageButton edit = (ImageButton)child.findViewById(R.id.imageButton1);
    	ImageButton dele = (ImageButton)child.findViewById(R.id.imageButton2);
    	ImageButton copy = (ImageButton)child.findViewById(R.id.imageButton3);
    	edit.setTag(items[0]+","+items[1]);
    	dele.setTag(items[0]+","+items[1]);
    	copy.setTag(items[0]+","+items[1]);
    	edit.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				String s = arg0.getTag().toString();
				Intent i = new Intent(HostsEditor.this,EditHost.class);
				i.putExtra("host", s);
				i.putExtra("fs", fs);
				i.putExtra("block", block);
				startActivity(i);
			}
		});
    	dele.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(final View arg0) {
				AlertDialog.Builder alert = new AlertDialog.Builder(HostsEditor.this);
				alert.setTitle("Delete host entry");
				alert.setMessage("Are you sure you want to delete this host entry?");
				alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface arg1, int arg2) {
						String entry = arg0.getTag().toString();
						removeEntry(entry,true);
						makeHostsList();
					}
				});
				alert.setNegativeButton("No", null);
				alert.create().show();
			}
		});
    	copy.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View arg0) {
				String entry = arg0.getTag().toString();
				copyHostItem(entry);
				makeHostsList();
			}
		});
    	tv1.setText(items[0]);
    	tv2.setText(items[1]);
    	parent.addView(child);
    }

    public boolean onOptionsItemSelected(MenuItem item){
    	switch (item.getItemId()) {
        case R.id.menu_add:
        	addHost();
          return true;
        case R.id.menu_refresh:
        	makeHostsList();
        	return true;
          default:
        	  return super.onOptionsItemSelected(item);
        }
    }
    
    void addHost(){
    	Intent i = new Intent(this,AddHost.class);
    	i.putExtra("fs", fs);
		i.putExtra("block", block);
    	startActivity(i);
    }
    
    void removeEntry(String entry,boolean first){
    	String[] params = entry.split(",");
    	entry = params[0]+" "+params[1];
    	List<String> hosts= new ArrayList<String>();
    	try{
    		Process proc = Runtime.getRuntime().exec("cat /etc/hosts");
    		BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
    		String line=null;
    		while((line=reader.readLine())!=null){
    			if(!line.equals("")&&line!=null){
    				if(line.contains(entry)&&first){
    					first=false;
    				}else if(line.contains(entry)&&!first){
    					hosts.add(line);
    				}else if(!line.contains(entry)){
    					hosts.add(line);
    				}
    			}
    		}
    	}catch(Exception e){
    		
    	}
    	if(hosts.size()>0){
    		try{
    			Process proc = Runtime.getRuntime().exec("su");
        		DataOutputStream os = new DataOutputStream(proc.getOutputStream());
        		os.writeBytes("mount -o rw,remount -t "+fs+" "+block+" /system\n");
        		os.writeBytes("echo '' > /etc/hosts\n");
        		for(String s : hosts){
        			os.writeBytes("echo '"+s+"' >> /etc/hosts\n");
        		}
        		os.writeBytes("mount -o ro,remount -t "+fs+" "+block+" /system\n");
        		os.writeBytes("exit\n");
        		os.flush();
        		os.close();
        		proc.waitFor();
    		}catch(Exception e){
    			
    		}
    	} else{
    		try{
    			Process proc = Runtime.getRuntime().exec("su");
        		DataOutputStream os = new DataOutputStream(proc.getOutputStream());
        		os.writeBytes("mount -o rw,remount -t "+fs+" "+block+" /system\n");
        		os.writeBytes("echo '' > /etc/hosts\n");
        		os.writeBytes("mount -o ro,remount -t "+fs+" "+block+" /system\n");
        		os.writeBytes("exit\n");
        		os.flush();
        		os.close();
        		proc.waitFor();
    		}catch(Exception e){
    			
    		}
    	}
    }
    
    boolean isValidEntry(String line){
    	boolean result=false;
    	if(line.contains(" ")){
    		int pos = line.indexOf(" ")+1;
    		if(line.length()>pos){
    			String[] items = line.split(" ");
    			if(items[0].contains(".")){
    				String[] ips = items[0].split("\\.");
    				if(ips.length==4){
    					result = true;
    				}else{
    					Toast.makeText(this, "Some invalid entries were found and was not added to the list.", Toast.LENGTH_LONG).show();
    				}
    			}
    		}
    	}
    	return result;
    }
    
    void copyHostItem(String entry){
    	List<String> allItems = new ArrayList<String>();
    	String[] items = entry.split(",");
    	String hEntryFromFile = items[0]+" "+items[1]+"_copy";
    	if(isValidEntry(hEntryFromFile)){
    		try{
        		Process proc = Runtime.getRuntime().exec("cat /etc/hosts");
        		BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        		String line=null;
        		while((line=reader.readLine())!=null){
        			if(!line.equals("")&&line!=null){
        				allItems.add(line);
        			}
        		}
        		allItems.add(hEntryFromFile);
        	}catch(Exception e){
        		
        	}
            try{
            		Process proc1 = Runtime.getRuntime().exec("su");
                    DataOutputStream os1 = new DataOutputStream(proc1.getOutputStream());
                    os1.writeBytes("mount -o rw,remount -t "+fs+" "+block+" /system\n");
                    os1.writeBytes("echo '' > /etc/hosts\n");
                    if(allItems.size()>0){
                    	for(String s1 : allItems){
                        	os1.writeBytes("echo '"+s1+"' >> /etc/hosts\n");
                        }
                    }
                    os1.writeBytes("mount -o ro,remount -t "+fs+" "+block+" /system\n");
                    os1.writeBytes("exit\n");
                    os1.flush();
                    os1.close();
                    proc1.waitFor();
            	}catch(Exception e){
            				
            	}
    	}
    	
    }
    
}
