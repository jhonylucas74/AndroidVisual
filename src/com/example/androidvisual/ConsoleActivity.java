package com.example.androidvisual;

import com.parserVisual.TradutorLinhas;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

public class ConsoleActivity extends Activity {

	public static TextView textConsole;
	public static EditText textin;
	public static Button buttonok;
	public static ScrollView scroller;
	TradutorLinhas tradutor;
	Thread novat;// thread do tradutor.
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_console);
	
		textConsole = (TextView) findViewById(R.id.textoviewConsole);
		
	    textin =(EditText) findViewById(R.id.entradaUser);
	    textin.setEnabled(false);
	    

	    scroller =(ScrollView) findViewById(R.id.scrooler);
	    
	    
	    buttonok =(Button) findViewById(R.id.button2); 
	    buttonok.setEnabled(false);	    
	    buttonok.setOnClickListener(new OnClickListener() {
	   
        public void onClick(View v) {        	
        	try{
        	 tradutor.setComfirmaEntrada(true);
        	 textin.setEnabled(false);        	 
        	 buttonok.setEnabled(false);
        	 
        	// Fazendo o keyboard sumir
     		InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
     	    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        	
        	 getWindow().getDecorView().getRootView().clearFocus();
        	 
        	}catch(Exception e) {}
        }
        });		 			
	    
	    
	    runThread();
						
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.console, menu);
		return true;
	}

	private void runThread() {

		/*ConsoleActivity.this.runOnUiThread(new Runnable() {

	            @Override
	            public void run() {*/
	            	tradutor = new TradutorLinhas(MainActivity.myedit.getText().toString());
	            	novat = new Thread(tradutor);
	        		novat.start();

	         /*   }
	        });*/
	}
	

    public void onDestroy()
    {    	
       
        System.out.println("fimfimfimfimfimfim");
        tradutor.stop=true;
        novat=null;
        super.onDestroy();
    }
	
}
