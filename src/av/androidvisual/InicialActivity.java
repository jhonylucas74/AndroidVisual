package av.androidvisual;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import av.androidvisual.R;

public class InicialActivity extends Activity {

    List listaTags = new ArrayList();		
    Adaptador adaptador = null;
    View layout;
    View layoutCont;
    EditText nomeArquivo;
   
    // nome do arquivo para ser deletado
    String nomeDelete;
    
   // Animation
    Animation slide;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_inicial);
									
		
		// load the animation
        slide = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slideup);  
		
		ListView lista = (ListView) findViewById(R.id.listatags);
		lista.setOnItemClickListener(onListClick);
		
		lista.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                    int position, long arg3) {
            	
            	Arquivo r = (Arquivo) listaTags.get(position);
            	nomeDelete= r.getNome();
            	
            	showDialog(1);
            	
            	return true;
            }

        });
		
		
        adaptador = new Adaptador();
        lista.setAdapter(adaptador);
        
        nomeArquivo = (EditText) findViewById(R.id.nomeDoArquivo);
        
        Button salvar = (Button) findViewById(R.id.button_optionInicial);
        salvar.setOnClickListener(onSave);
        
        Button buttonCancelar = (Button) findViewById(R.id.buttonCancelar);
        buttonCancelar.setOnClickListener(onCancel);
        
        Button buttonComfirmar = (Button) findViewById(R.id.buttonComfirmar);
        buttonComfirmar.setOnClickListener(onComfirm);
        
        layout= (View) findViewById(R.id.layoutArquivo);
        layout.setVisibility(View.GONE);
      
        layoutCont=  (View) findViewById(R.id.layoutcont);
        
                
        atualizarLista();
       
       
	}
	
	protected final Dialog onCreateDialog(final int id) {
	    Dialog dialog = null;
	    switch (id) {
	    case 1:
	        AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage(
	                "Deseja deletar esse arquivo?")
	                .setCancelable(false)
	                .setPositiveButton("Sim",
	                        new DialogInterface.OnClickListener() {
	                            public void onClick(DialogInterface dialog,
	                                    int id) {
	                                //to perform on ok
	                            	File file = new File("/sdcard/VisualAndroid/"+nomeDelete);
	                            	boolean deleted = file.delete();

	                            	try {
	                        			Thread.sleep(100);
	                        		} catch (InterruptedException e) {
	                        			// TODO Auto-generated catch block
	                        			e.printStackTrace();
	                        		}
	                            	
	                            	atualizarLista();	                            	
	                            }
	                        })
	                .setNegativeButton("Cancelar",
	                        new DialogInterface.OnClickListener() {
	                            public void onClick(DialogInterface dialog,
	                                    int id) {

	                                //dialog.cancel();
	                            }
	                        });
	        AlertDialog alert = builder.create();
	        dialog = alert;
	        break;

	    default:

	    }
	    return dialog;
	}

    private OnItemClickListener onListClick = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
        	
            Arquivo r = (Arquivo) listaTags.get(position);
          
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtra("nome_arquivo",r.getNome());
            startActivity(i);
            
        }
    };
    
    private OnClickListener onComfirm = new OnClickListener() {
   	 
        public void onClick(View arg0) {
            
        	layout.setVisibility(layout.GONE);

        	 String nome=null;
        	try{
        		 
        		// Fazendo o keyboard sumir
        		InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        	    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        		
        		//Create Folder
        		  File folder = new File(Environment.getExternalStorageDirectory().toString()+"/VisualAndroid");
        		  folder.mkdirs();
        		  
        		  nome= nomeArquivo.getText().toString();
        		  nomeArquivo.setText("");
        		  
        		  if(nome.length()<0){
        			  return;
        		  }
        		  
        		  SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy ");
        		  Date date = new Date();
        		  System.out.println();
        		
              	String data ="algoritmo \""+nome+"\" \n"
    					+ "// Seção de Declarações \n"
    					+ "// Data : "+dateFormat.format(date)+"\n"
    					+ "var \n \n"
    					+ "inicio  \n"
    					+ "// Seção de Comandos \n"
    					+ "fimalgoritmo";	
        		
        	File myFile = new File("/sdcard/VisualAndroid/" +nome+".txt");
            myFile.createNewFile();
            
            
            try {               
                FileOutputStream fOut = new FileOutputStream(myFile);
                OutputStreamWriter myOutWriter = 
                                        new OutputStreamWriter(fOut);
                myOutWriter.append(data);
                myOutWriter.close();
                fOut.close();
           
            } catch (Exception e) {
                
            }
            
            
        	}catch(Exception e){}
            
          
        	atualizarLista();         	 
         	 
         	 // iniciando nova Acitivity mandando o arquivo criado como "parametro" .
        	 Intent i = new Intent(getApplicationContext(), MainActivity.class);
             i.putExtra("nome_arquivo",nome+".txt");
             startActivity(i);
        	
        	
      
            
        }
    };
	
    
    private OnClickListener onCancel = new OnClickListener() {
   	 
        public void onClick(View arg0) {
            
        	layout.setVisibility(layout.GONE);        	      
            
        }
    };
    
    private OnClickListener onSave = new OnClickListener() {
    	 
        public void onClick(View arg0) {
            
        	
        	layout.startAnimation(slide);
        	layout.setVisibility(View.VISIBLE);
        	            
        }
    };
	
	
	 class Adaptador extends ArrayAdapter {
	        Adaptador() {
	            super(InicialActivity.this, R.layout.linha,
	            		listaTags);
	        }
	 
	 @Override
	 public View getView(int position, View convertView, ViewGroup parent) {

         View linha = convertView;
         Armazenador armazenador = null;

         if (linha == null) {
             LayoutInflater inflater = getLayoutInflater();
             linha = inflater.inflate(R.layout.linha, parent, false);
             armazenador = new Armazenador(linha);
             linha.setTag(armazenador);
         } else {
             armazenador = (Armazenador) linha.getTag();
         }

         armazenador.popularFormulario((Arquivo) listaTags.get(position));

         return linha;
     }
}
 
    static class Armazenador {
    	
    private TextView nome = null;
    private TextView data = null;
    
    Armazenador(View linha) {
        nome = (TextView) linha.findViewById(R.id.titulo);
        data = (TextView) linha.findViewById(R.id.titulo2);
        
    }

    void popularFormulario(Arquivo r) {
        nome.setText(r.getNome().replaceAll(".txt",""));        
        data.setText(r.getData());  
    }
}
	 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inicial, menu);
		return true;
	}

	public void atualizarLista(){
		
		 try{
			 adaptador.clear();
			 
			 File folder = new File(Environment.getExternalStorageDirectory().toString()+"/VisualAndroid");			 
			 
			 criarExemplos();
			  
			 File childfile[] = folder.listFiles();
			 for (File file2 : childfile) {
			    
			Arquivo as = new Arquivo();
	        as.setNome(file2.getName().toString());
	        
	        
	        String textoArquivo = readFromFile(file2.getName().toString());
	        
	        // Data : 04-05-2014
	        String data = "Data desconhecida";
	        if(textoArquivo.contains("Data")){
	        data =  textoArquivo.substring(textoArquivo.indexOf(":")+1,textoArquivo.indexOf(":")+12);	  
	        as.setData("Criado em "+data);
	        }else{
	        as.setData(data);
	        }
	        
	        adaptador.add(as);
			}
	       
	       }catch(Exception e){}
	}
	
	
	
	public void criarExemplos(){
		
		 File folder = new File(Environment.getExternalStorageDirectory().toString()+"/VisualAndroid");	
		 
		if (!folder.exists()){
			folder.mkdirs();	
		}else{
			return;
		}
		
		File file = new File(Environment.getExternalStorageDirectory().toString()+"/VisualAndroid/Alô mundo.txt");
	
		if(!file.exists()){
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy ");
  		  Date date = new Date();
  		  System.out.println();
  		
	    	String data ="algoritmo \""+"Alô mundo"+"\" \n"
					+ "// Exemplo utilizando o escreval \n"
					+ "// Data : "+dateFormat.format(date)+"\n"
					+ "var \n \n"
					+ "inicio  \n"
					+ "//Fazendo o algoritmo imprimir um alô mundo \n"
					+ "escreval(\"Alô mundo! \") \n"
					+ "fimalgoritmo";	
	
     	 File myFile = new File("/sdcard/VisualAndroid/" +"Alô mundo"+".txt");
	        
	         try {
	        	 myFile.createNewFile();
	             FileOutputStream fOut = new FileOutputStream(myFile);
	             OutputStreamWriter myOutWriter = 
	                                     new OutputStreamWriter(fOut);
	             myOutWriter.append(data);
	             myOutWriter.close();
	             fOut.close();
	        
	         } catch (Exception e) { }
			
		}
		
		file = new File(Environment.getExternalStorageDirectory().toString()+"/VisualAndroid/Entrada de dados.txt");
		
		if(!file.exists()){
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy ");
  		  Date date = new Date();
  		  System.out.println();
  		
	    	String data ="algoritmo \""+"Entrada de dados"+"\" \n"
					+ "// Exemplo utilizando o leia e o escreval \n"
					+ "// Data : "+dateFormat.format(date)+"\n"
					+ "var \nvalor1,valor2,resultado:inteiro \n"
					+ "inicio  \n"
					+ "//Ler do usuário dois valores e imprimir o resultado da soma \n"
					+ "escreval(\"Digite o valor 1:\") \n"
					+ "leia(valor1) \n"
					+ "escreval(\"Digite o valor 2:\") \n"
					+ "leia(valor2) \n"
					+ "resultado<- valor1 + valor2 \n"
					+ "escreval(\"O resultado é :\",resultado) \n"
					+ "fimalgoritmo";	
	
     	 File myFile = new File("/sdcard/VisualAndroid/" +"Entrada de dados"+".txt");
	        
	         try {
	        	 myFile.createNewFile();
	             FileOutputStream fOut = new FileOutputStream(myFile);
	             OutputStreamWriter myOutWriter = 
	                                     new OutputStreamWriter(fOut);
	             myOutWriter.append(data);
	             myOutWriter.close();
	             fOut.close();
	        
	         } catch (Exception e) { }
			
		}
		
	   file = new File(Environment.getExternalStorageDirectory().toString()+"/VisualAndroid/Laço de repetição.txt");
		
		if(!file.exists()){
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy ");
  		  Date date = new Date();
  		  System.out.println();
  		
	    	String data ="algoritmo \""+"Laço de repetição"+"\" \n"
					+ "// Exemplo utilizando o para \n"
					+ "// Data : "+dateFormat.format(date)+"\n"
					+ "var \n contador:inteiro \n"
					+ "inicio  \n"
					+ "//Imprimir os números de um a dez usando um laço de repetição  \n"
					+ "\n"
					+ "para contador de 1 ate 10 faca \n"
					+ " \n"
					+ "escreval(contador) \n"
					+ " \n"
					+ "fimpara \n"
					+ "fimalgoritmo";	
	
     	 File myFile = new File("/sdcard/VisualAndroid/" +"Laço de repetição"+".txt");
	        
	         try {
	        	 myFile.createNewFile();
	             FileOutputStream fOut = new FileOutputStream(myFile);
	             OutputStreamWriter myOutWriter = 
	                                     new OutputStreamWriter(fOut);
	             myOutWriter.append(data);
	             myOutWriter.close();
	             fOut.close();
	        
	         } catch (Exception e) { }
			
		}
		
file = new File(Environment.getExternalStorageDirectory().toString()+"/VisualAndroid/Condicional.txt");
		
		if(!file.exists()){
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy ");
  		  Date date = new Date();
  		  System.out.println();
  		
	    	String data ="algoritmo \""+"Condicional"+"\" \n"
					+ "// Exemplo utilizando o se \n"
					+ "// Data : "+dateFormat.format(date)+"\n"
					+ "var \n nota:real \n"
					+ "inicio  \n"
					+ "// Caso o aluno tenha nota maior que sete será impresso na tela se ele passou ou não  \n"
					+ "\n"
					+ "escreval(\"Qual foi a nota do aluno?\") \n"
					+ "leia(nota) \n"
					+ " \n"
					+ "se nota>=7 entao \n"
					+ "  escreval(\"Aluno aprovado!\") \n"
					+ "senao \n"
					+ "  escreval(\"Aluno reprovado!\") \n"
					+ "fim se \n"
					+ "fimalgoritmo";	
	
     	 File myFile = new File("/sdcard/VisualAndroid/" +"Condicional"+".txt");
	        
	         try {
	        	 myFile.createNewFile();
	             FileOutputStream fOut = new FileOutputStream(myFile);
	             OutputStreamWriter myOutWriter = 
	                                     new OutputStreamWriter(fOut);
	             myOutWriter.append(data);
	             myOutWriter.close();
	             fOut.close();
	        
	         } catch (Exception e) { }
			
		}
		
         file = new File(Environment.getExternalStorageDirectory().toString()+"/VisualAndroid/Atribuição.txt");
		
		if(!file.exists()){
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy ");
  		  Date date = new Date();
  		  System.out.println();
  		
	    	String data ="algoritmo \""+"Atribuição"+"\" \n"
					+ "// Exemplo utilizando atribuição de valor e o escreval\n"
					+ "// Data : "+dateFormat.format(date)+"\n"
					+ "var \n val:inteiro \n"
					+ "inicio  \n"
					+ "// Atribuindo um valor a val   \n"					
					+ "val <- 7 \n"				
					+ "  escreval(\"O valor de val é \",val) \n"					
					+ "fimalgoritmo";	
	
     	 File myFile = new File("/sdcard/VisualAndroid/" +"Atribuição"+".txt");
	        
	         try {
	        	 myFile.createNewFile();
	             FileOutputStream fOut = new FileOutputStream(myFile);
	             OutputStreamWriter myOutWriter = 
	                                     new OutputStreamWriter(fOut);
	             myOutWriter.append(data);
	             myOutWriter.close();
	             fOut.close();
	        
	         } catch (Exception e) { }
			
		}
		
	}
	
private String readFromFile(String path) {
        
        String ret = "";
         
        try {
        	File arquivo = new File("/sdcard/VisualAndroid/" + path) ;
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
}
