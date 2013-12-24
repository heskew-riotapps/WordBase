package com.riotapps.wordbase.ui;

import com.riotapps.wordbase.R;
 
import com.riotapps.wordbase.interfaces.ICloseDialog;
import com.riotapps.wordbase.utils.Logger;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CustomToast extends AlertDialog{
	private static final String TAG = CustomToast.class.getSimpleName();
	  private String dialogText = "";
	  private TextView dialog_text;
	  private int numMillisecondsInView = 800;
	  private Context context;
	  private int onFinishReturnCode = 0;
	  
	 // public void SetText(String text){
	//	  this.dialogText = text;
	 // }
	
	  public CustomToast(Context context) {  
	        super(context);
	        this.context = context;
	  }
	  
	  public CustomToast(Context context, int numMillisecondsInView) {  
	        super(context);
	        this.context = context;
	        this.numMillisecondsInView = numMillisecondsInView;
	  }
	 
	  public CustomToast(Context context, int numMillisecondsInView, int onFinishReturnCode) {  
	        super(context);
	        this.onFinishReturnCode = onFinishReturnCode;
	        this.context = context;
	        this.numMillisecondsInView = numMillisecondsInView;
	  }
	  
	  public CustomToast(Context context, String text) {  
	        super(context);
	        this.context = context;
	        this.dialogText = text;
	  }
	  
	  public CustomToast(Context context, String text, int numMillisecondsInView, int onFinishReturnCode) {  
	        super(context);
	        this.context = context;
	        this.onFinishReturnCode = onFinishReturnCode;
	        this.numMillisecondsInView = numMillisecondsInView;
	        this.dialogText = text;
	  }

	  
	  public CustomToast(Context context, String text, int numMillisecondsInView) {  
	        super(context);
	        this.context = context;
	        this.numMillisecondsInView = numMillisecondsInView;
	        this.dialogText = text;
	  }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		     LayoutInflater inflater = getLayoutInflater();//(LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
	        View layout = inflater.inflate(R.layout.toast, 
	                                        (ViewGroup) findViewById(R.id.toast_root));
	     
	        this.setCancelable(false);
	        dialog_text = (TextView) layout.findViewById(R.id.dialog_text);
	     	if (this.dialogText.length() > 0) {
	     		dialog_text.setText(this.dialogText);
	     	}
	     	else {
	     		dialog_text.setVisibility(View.GONE);
	     	}

	 		this.setContentView(layout);
	 		
	 		CountDownTimer countdown = new CountDownTimer(numMillisecondsInView, numMillisecondsInView) {

			     public void onTick(long millisUntilFinished) {
			     			     }

			     public void onFinish() {
			        // mTextField.setText("done!");
			    	 if (onFinishReturnCode > 0) {
			    		 ((ICloseDialog)context).dialogClose(onFinishReturnCode);
			    	 }
			    	 
			    	 CustomToast.this.dismiss(); 
			     }
			  };
			  
			  countdown.start();
	 		 
	 		
	}

	@Override
	public void setMessage(CharSequence message) {
		// TODO Auto-generated method stub
	//	super.setMessage(message);
		  this.dialogText = (String)message;
	}  
	@Override
	public void show() {
		// TODO Auto-generated method stub
		Logger.d(TAG, "SHOW");
		super.show();
	}  
	    
	 
	
}
