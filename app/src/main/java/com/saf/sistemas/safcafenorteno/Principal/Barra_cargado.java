package com.saf.sistemas.safcafenorteno.Principal;



import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.saf.sistemas.safcafenorteno.R;
import com.saf.sistemas.safcafenorteno.Auxiliar.variables_publicas;

public class Barra_cargado extends Activity {

	int progreso;//Guardar� un numero entero que ser� el numero de segundos que ha pasado desde que hemos inicializado la aplicaci�n.
    ProgressBar barraProgreso;//Declaraci�n de la barra de progreso.
    int cantPorcent=1;
    TextView txtPorcentaje;
	
	@SuppressLint("SuspiciousIndentation")
    @Override
	protected void onCreate(Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_barra_cargado);
		

		 barraProgreso = (ProgressBar)findViewById(R.id.progressBar1);//Relacionamos el la barra de progreso del layout con el de java
	        new TareaSegundoPlano().execute();//Iniciamos la tarea en segundo plano, en este caso no necesitamos pasarle nada.
	
	        
	}
	  //Clase interna que extiende de AsyncTask
 public class TareaSegundoPlano extends AsyncTask<Void, Void, Void>{

     //M�todo que se ejecutar� antes de la tarea en segundo plano, normalmente utilizado para iniciar el entorno gr�fico
     protected void onPreExecute(){           
			barraProgreso.setProgress(0);//Ponemos la barra de progreso a 0
         barraProgreso.setMax(10);//El m�ximo de la barra de progreso son 10, de los 10 segundo que va a durar la tarea en segundo plano.
     }

     //Este m�todo se ejecutar� despu�s y ser� el que ejecute el c�digo en segundo plano
     @Override
     protected Void doInBackground(Void... params) {
         for(int progreso = 1;progreso<=10;progreso++){//Creamos un for de 1 a 10 que ir� contando los segundos.
         	
             try {
                     Thread.sleep(100);//Esto lo que hace es ralentizar este proceso un segundo (el tiempo que se pone entre par�ntesis es en milisegundos) tiene que ir entre try y catch

             } catch (InterruptedException e) {}
            
             publishProgress();//Actualizamos el progreso, es decir al llamar a este proceso en realidad estamos llamamos al m�todo onProgresssUpdate()
         }
             return null;//Al llegar aqu�, no devolvemos nada y acaba la tarea en segundo plano y se llama al m�todo onPostExecute().
         }
  
         protected void onProgressUpdate(Void... values) {
             barraProgreso.setProgress(progreso);//Actualizamos la barra de progreso con los segundos que vayan.
         }
  
         protected void onPostExecute(Void result){//A este m�todo se le llama cada vez que termine la tarea en segundo plano.
             
      	   //Toast.makeText(getBaseContext(), "Tarea Finalizada", Toast.LENGTH_LONG).show();//Nos muestra una notificaci�n informando de que la tarea en segundo plano ha finalizado
      	 // ENVIA Al otro activity
             if(variables_publicas.LoginOk)
             {
				Intent intent=new Intent("android.intent.action.MenuActivity");
				startActivity(intent);
				finish();
             }
             else
             {
                 String MensajeLogin = "MensajeLogin";
                 Intent newAct = new Intent(getApplicationContext(), Login.class);
                 newAct.putExtra(variables_publicas.MensajeLogin, MensajeLogin );
                 startActivity(newAct);
                 finish();
             }
         }
 }
 //Definimos que para cuando se presione la tecla BACK no volvamos para atras  	 
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event)  {
	     if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
	         // no hacemos nada.
	         return true;
	     }

	     return super.onKeyDown(keyCode, event);
	 }
 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.barra_cargado, menu);
		return true;
	}


}
