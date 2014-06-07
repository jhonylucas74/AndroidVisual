package av.androidvisual;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


import java.io.OutputStreamWriter;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import av.androidvisual.R;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

	
	static EditText myedit;
	boolean edit = true;
	String nomeArquivo;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_main);
						
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    
		  nomeArquivo = extras.getString("nome_arquivo");		   			   
		}
		
	     myedit = (EditText) findViewById(R.id.editText1);			  
	     
	     Button option = (Button) findViewById(R.id.button_option);		
	     option.setOnClickListener(onSave);
	     
		// Button myEdit = (Button) findViewById(R.id.button1);
		/* myEdit.setOnClickListener(new OnClickListener() {
						
			public void onClick(View v) {
				
					 Editable editi = myedit.getText();
					 myedit.setText("");
					 transformToHtml(editi.toString(),true);
											
			}
		});	*/ 		
	     
	     myedit.setOnKeyListener(new View.OnKeyListener() {
	    	 @Override 
	    	 public boolean onKey(View v, int keyCode, KeyEvent event) {
	                // TODO Auto-generated method stub
	                if (keyCode==KeyEvent.KEYCODE_ENTER ||
	                    keyCode== KeyEvent.KEYCODE_SPACE ||
	                    keyCode== KeyEvent.KEYCODE_APOSTROPHE) { 
	                     
	                	 int position = myedit.getSelectionStart();
	                	 
	                	 Editable editi = myedit.getText();	               
						 myedit.setText("");
						 transformToHtml(editi.toString(),true);
						 myedit.setSelection(position);
						 	                	
	               }	                	                	         
	              
					return false;
					
	    	 } 
	    	 
	    	
	     
	     
	     });	     	 
		 
		 
	 
	     
		 Button execute = (Button) findViewById(R.id.button2);
		 execute.setOnClickListener(new OnClickListener() {
						
			public void onClick(View v) {
				
				 Intent i = new Intent(MainActivity.this,ConsoleActivity.class);
				 startActivityForResult(i, 1);				 
			}
		});		 			
		 
			    
		    
		/*	
		 try{
		    File myFile = new File("/sdcard/" + nomeArquivo);
		    FileInputStream fis = new FileInputStream(myFile);

		    byte[] dataArray = new byte[fis.available()];
		    while (fis.read(dataArray) != -1) {
		    	textoinical = new String(dataArray);
		    }
		    fis.close();
		   

		   } catch (FileNotFoundException e) {

		    e.printStackTrace();
		   } catch (IOException e) {

		    e.printStackTrace();
		   }*/

					
		    String textoinical= readFromFile();	
			transformToHtml(textoinical,true);			
			
	}

	

	
	private String readFromFile() {
        
        String ret = "";
         
        try {
        	File arquivo = new File("/sdcard/VisualAndroid/" + nomeArquivo) ;
           // InputStream inputStream = openFileInput(arquivo);
            FileInputStream inputStream = new FileInputStream (arquivo);
             
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                 
                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString+"\n");
                }
                 
                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
           
        } catch (IOException e) {
           
        }
 
        return ret;
    }
	
    
    private OnClickListener onSave = new OnClickListener() {
    	 
        public void onClick(View arg0) {
            
        	try{
		        
        		//Create Folder
        		  File folder = new File(Environment.getExternalStorageDirectory().toString()+"/VisualAndroid");
        		  folder.mkdirs();
        		          		  
        		  

        		  
            String data = myedit.getText().toString();
        		
        	
        	File myFile = new File("/sdcard/VisualAndroid/" +nomeArquivo);
            myFile.createNewFile();
            
            
            try {               
                FileOutputStream fOut = new FileOutputStream(myFile);
                OutputStreamWriter myOutWriter = 
                                        new OutputStreamWriter(fOut);
                myOutWriter.append(data);
                myOutWriter.close();
                fOut.close();
                
                Toast.makeText(getApplicationContext(),"Arquivo salvo", 
     				   Toast.LENGTH_LONG).show();
           
            } catch (Exception e) {
                
            }
            
        	}catch (Exception e) {
                
            }
            
        	
            
        }  
    };
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu); 
		return true;
	}
	
	public void transformToHtml(String text, boolean lineBreak){
		
		InputStream is = new ByteArrayInputStream(text.getBytes());		 	
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line="";
				
		String temp;		
		// primeira parte: verificando linha Algoritmo
		try {
			while(( line = br.readLine()) != null ){	
						
			temp= line;
			
			/*
			 *  Colocando de vermelho todo que estiver entre aspas
			 */
			
			ArrayList<String> listaSentenca = new ArrayList<String>();
		    int inicio=-1;
		    int fim= -1;
			
	        for( int i = 0; i<temp.length();i++){			       
	        	
	      	        	
	        	//Encontrando o fim
	        	if (fim == -1 && inicio != -1 && temp.charAt(i) == '"' ){												
					
					fim = i;					
				}
	        	
	          	//Encontrando o inicio
	        	if (fim == -1 && inicio == -1 && temp.charAt(i) == '"' ){												
					
					inicio= i;					
				}
	        	
	        	if ( inicio !=-1 && fim != -1){	      
	        		
	        		 String entreAspas= temp.substring(inicio, fim+1);
	        		 listaSentenca.add(entreAspas);
	        		 	        			        		 
	        		 inicio=-1;
	        	     fim= -1;
	        	}
			
												
			}
				
			for(String s: listaSentenca){
				
				temp = temp.replaceAll(s,"<font color = \"#FF0000\" >"+s+"</font>");
				// Toast.makeText(getApplicationContext(), temp,
	  		    	//	   Toast.LENGTH_LONG).show();
			}
													
			/* 
			 * Partindo do principio que existe três tipos de linha:
			 * Linha de atribuição de valor , que contem o caracete < 
			 * Linha de comentário que contem duas barras //
			 * Linha que contem algumas palavra reservada  Exe: escreval, algoritmo, leia,para. 
			 * */
			if(line.contains("<")){
				
			fim = line.indexOf("<")+1;
			
			String proces= line.substring(0,fim-1);
			
			if(proces.length()>0){
			transformToHtml(proces,false);
			}
			
			temp= line.substring(fim-1,fim);							
			myedit.append(temp);
			
			if(fim<= line.length()){
			temp= line.substring(fim,line.length());
			
			//Irá passar tudo o que estiver depois do <- para ser processado de novo pelo método
			if(temp.length()>0){
			transformToHtml(temp,false);
			}
			
			
			if(lineBreak == true){
			myedit.append("\n");
			}
			
			}
			
			 					
			} else 	if(temp.contains("//")){
			
		    //Fazer isso para a linha que conter comentário.			
			temp= "<font color = \"#1ead57\">"+line+"</font>";
			Spanned sp = Html.fromHtml(temp+"<br>") ;
			myedit.append(sp);  
			
			}else{
			temp = temp.replaceAll("fimalgoritmo","<font color = \"#2b2bff\" ><u>fimalgoritmo</u></font>");		
			temp = temp.replaceAll("algoritmo","<font color = \"#2b2bff\" ><u>algoritmo</u></font>");
			temp = temp.replaceAll("var","<font color = \"#2b2bff\" ><u>var</u></font>");
			temp = temp.replaceAll("inicio", "<font color = \"#2b2bff\" ><u>inicio</u></font>");
			//tipos de variáveis
			temp = temp.replaceAll("inteiro", "<font color = \"#800000\" >inteiro</font>");
			temp = temp.replaceAll("real", "<font color = \"#800000\" >real</font>");
			temp = temp.replaceAll("caractere", "<font color = \"#800000\" >caractere</font>");
			temp = temp.replaceAll("logico", "<font color = \"#800000\" >logico</font>");
			//palavras reservadas
			temp = temp.replaceAll("escreval", "<font color = \"#6336bc\" >escreval</font>");
			temp = temp.replaceAll("escreva", "<font color = \"#6336bc\" >escreva</font>");
			temp = temp.replaceAll("senao", "<font color = \"#6336bc\" >senao</font>");
			temp = temp.replaceAll("se ", "<font color = \"#6336bc\" >se </font>");			
			temp = temp.replaceAll("entao", "<font color = \"#6336bc\" >entao</font>");
			temp = temp.replaceAll("fimse", "<font color = \"#6336bc\" >fimse</font>");
			temp = temp.replaceAll("leia", "<font color = \"#6336bc\" >leia</font>");
			temp = temp.replaceAll("fimpara", "<font color = \"#6336bc\" >fimpara</font>");
			temp = temp.replaceAll("para", "<font color = \"#6336bc\" >para</font>");
			temp = temp.replaceAll(" de ", "<font color = \"#6336bc\" > de </font>");
			temp = temp.replaceAll("ate", "<font color = \"#6336bc\" >ate</font>");
			temp = temp.replaceAll("faca", "<font color = \"#6336bc\" >faca</font>");
			temp = temp.replaceAll(" passo ", "<font color = \"#6336bc\" > passo </font>");			
			temp = temp.replaceAll("enquanto", "<font color = \"#6336bc\" >enquanto</font>");
			temp = temp.replaceAll("repita", "<font color = \"#6336bc\" >repita</font>");
			temp = temp.replaceAll("ate", "<font color = \"#6336bc\" >ate</font>");
			temp = temp.replaceAll("escolha", "<font color = \"#6336bc\" >escolha</font>");
			temp = temp.replaceAll("fimescolha", "<font color = \"#6336bc\" >fimescolha</font>");
			temp = temp.replaceAll("interrompa", "<font color = \"#6336bc\" >interrompa</font>");
			
			if(lineBreak== true){
				temp+="<br>";
			}
			
			Spanned sp= Html.fromHtml(temp);
			
			myedit.append(sp);  
			}
						
			
			}
			
		} catch (IOException e) { }
		
					
	}

	
	private void writeToFile(String data) {
	    try {
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_PRIVATE));
	        outputStreamWriter.write(data);
	        outputStreamWriter.close();
	    }
	    catch (IOException e) {
	        Log.e("Exception", "File write failed: " + e.toString());
	    }
	   
	}

	

}
