package com.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;

public class Myalertdialog extends AlertDialog{  
    private static int NONE = -1;  
    private int tint = NONE;  
   
    /** 
     * @param context 
    * @param theme 
    */  
     protected Myalertdialog(Context context) {  
       super(context);  
       init();  
        
   }  
    
  /** 
   * @param context 
   * @param theme 
    */  
  protected Myalertdialog(Context context, int theme) {  
      super(context, theme);  
     init();  
   }  
     
   /** 
    *  
   */  
   private void init() {        
	   
     final Theme theme = getContext().getTheme();  
      final TypedArray attrs = theme.obtainStyledAttributes(new int[] { android.R.attr.tint });  
     tint = attrs.getColor(0, NONE);
  }  
 
   @Override  
   public void show() {  
       // TODO Auto-generated method stub  
       super.show();  
        setTint(tint);  
   }  
 
   public void setTint(int tint) {  
       // TODO Auto-generated method stub  
        this.tint = tint;  
        android.graphics.PorterDuff.Mode mode =android.graphics.PorterDuff.Mode.SRC_ATOP;  
          
        
           final Drawable d =  this.getWindow().getDecorView().getBackground();  
           
              d.mutate().setColorFilter(tint, mode);  
           
               }  
     
 
   /** 
    * @param button 
    */  
   public void setCancelButton(Button button) {  
      button.setOnClickListener(new View.OnClickListener() {  
  
         @Override  
        public void onClick(View v) {  
           cancel();  
         }  
     
      });  
   }  
     
   /** 
    * @param button 
    */  
   public void setPositiveButton(Button button) {  
      button.setOnClickListener(new View.OnClickListener() {  
  
         @Override  
         public void onClick(View v) {  
            dismiss();  
         }  
     
      });  
   }  
  
  
     
   public static class Builder extends AlertDialog.Builder {  
 
      private Myalertdialog dialog;  
       
      public Builder(Context context) {  
         super(context);  
           
         dialog = new Myalertdialog(context);  
      }  
 
      public Builder(Context context, int theme) {  
         super(context);  
         dialog = new Myalertdialog(context, theme);  
      }  
        
       
 
   @Override  
      public Myalertdialog create() {  
         return dialog;  
      }  
  
      @Override  
      public Builder setMessage(CharSequence message) {  
         dialog.setMessage(message);  
         return this;  
      }  
  
      @Override  
      public Builder setTitle(CharSequence title) {  
         dialog.setTitle(title);  
         return this;  
      }  
  
      @Override  
      public Builder setPositiveButton(CharSequence text,  
            OnClickListener listener) {  
         dialog.setButton(BUTTON_POSITIVE, text, listener);  
         return this;  
      }  
  
         @Override  
         public Builder setIcon(int iconId) {  
            dialog.setIcon(iconId);  
            return this;  
         }  
  
   }  

}
