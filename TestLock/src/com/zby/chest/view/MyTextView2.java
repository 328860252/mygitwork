package com.zby.chest.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.zby.chest.utils.Tools;

public class MyTextView2 extends TextView{
	private final String namespace = "http://www.angellecho.com/";
	private String text;
	private float textSize;
	private float paddingLeft;
	private float paddingRight;
	private float marginLeft;
	private float marginRight;
	private int textColor;
	
	private float density;
	//private float singleWordWidth;
	private Context mContext;
	
	private Paint paint1 = new Paint();
	private float textShowWidth;
	public MyTextView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		text = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/android", "text");
		density = getResources().getDisplayMetrics().density;  
		
		textSize = attrs.getAttributeIntValue(namespace, "textSize", 18) *density;
		textColor = attrs.getAttributeIntValue(namespace, "textColor",Color.BLACK);
		paint1.setColor(textColor);
		paint1.setAntiAlias(true);
		
		paint1.setTextSize(textSize);
		paddingLeft = attrs.getAttributeIntValue(namespace, "paddingLeft", 0) *density;
		paddingRight = attrs.getAttributeIntValue(namespace, "paddingRight", 0) * density;
		marginLeft = attrs.getAttributeIntValue(namespace, "marginLeft", 0) * density;
		marginRight = attrs.getAttributeIntValue(namespace, "marginRight", 0) *density;
	}
	@Override
	protected void onDraw(Canvas canvas) {
//	    if(Tools.isLocalLanguageChina(mContext)) {
//	        textShowWidth = getWidth()-paddingLeft-paddingRight;
//	        int lineCount = 0;
//	        text = this.getText().toString();//.replaceAll("\n", "\r\n");
//	        if(text==null)return;
//	        char[] textCharArray = text.toCharArray();
//	        // �ѻ�Ŀ���
//	        float drawedWidth = 0;
//	        float charWidth;
//	        for (int i = 0; i < textCharArray.length; i++) {
//	            char str = textCharArray[i];
//	            charWidth = paint1.measureText(textCharArray, i, 1);
//	            
//	            if(textCharArray[i]=='\n'){
//	                lineCount++;
//	                drawedWidth = 0;
//	                continue;
//	            }
//	            if (textShowWidth - drawedWidth < charWidth) {
//	                lineCount++;
//	                drawedWidth = 0;
//	            }
//	            canvas.drawText(textCharArray, i, 1, paddingLeft + drawedWidth,
//	                    (lineCount + 1) * textSize, paint1);
//	            drawedWidth += charWidth;
//	        }
//	        setHeight( (int) ((lineCount + 1)  * textSize + 5));
//	    } else {
	        super.onDraw(canvas);
//	    }
	}
}
