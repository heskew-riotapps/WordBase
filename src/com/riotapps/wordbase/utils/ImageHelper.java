package com.riotapps.wordbase.utils;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;

public class ImageHelper {
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels, int borderPixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        //pixels = 8;
        
        if (borderPixels < 0){borderPixels = 0;}
        
        if (borderPixels > 11110){
	        final int colorBorder = Color.BLACK;
	        final Paint paintBorder = new Paint();
	        final Rect rectBorder = new Rect(0, 0, bitmap.getWidth() + borderPixels, bitmap.getHeight() + borderPixels);
	        final RectF rectFBorder = new RectF(rectBorder);
	        final float roundPxBorder = pixels;
	
	        paintBorder.setAntiAlias(true);
	        canvas.drawARGB(0, 0, 0, 0);
	        paintBorder.setColor(colorBorder);
	        canvas.drawRoundRect(rectFBorder, roundPxBorder, roundPxBorder, paintBorder);
	       // return output;	        
	      //  paintBorder.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	      //  canvas.drawPaint(paintBorder); //(bitmap, rectBorder, rectBorder,paintBorder);
        }

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
    
    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight)
    {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return resizedBitmap;
    }
    
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight) {
 
	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
	 
	    
	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);

	}
	
	public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

   // Logger.d(TAG, "calculateInSampleSize height=" + height + " reqHeight=" + reqHeight + " width=" + width + " reqWidth=" + reqWidth);
    
    if (height > reqHeight || width > reqWidth) {

        // Calculate ratios of height and width to requested height and width
        final int heightRatio = Math.round((float) height / (float) reqHeight);
        final int widthRatio = Math.round((float) width / (float) reqWidth);

        // Choose the smallest ratio as inSampleSize value, this will guarantee
        // a final image with both dimensions larger than or equal to the
        // requested height and width.
        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
    }
   // Logger.d(TAG, "calculateInSampleSize inSampleSize=" + inSampleSize);
    return inSampleSize;
}
}
