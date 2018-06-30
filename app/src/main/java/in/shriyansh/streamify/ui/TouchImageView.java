package in.shriyansh.streamify.ui;

import android.content.Context;
    import android.graphics.Matrix;  
    import android.graphics.PointF;  
    import android.graphics.drawable.Drawable;  
    import android.util.AttributeSet;  
    import android.util.Log;  
    import android.view.MotionEvent;
    import android.view.ScaleGestureDetector;  
    import android.view.View;

public class TouchImageView extends android.support.v7.widget.AppCompatImageView {
        private Matrix matrix;
        // We can be in one of these 3 states  
        private static final int NONE = 0;
        private static final int DRAG = 1;
        private static final int ZOOM = 2;
      
        private int mode = NONE;
     
        // Remember some things for zooming  
        private final PointF last = new PointF();
        private final PointF start = new PointF();
        private final float minScale = 1f;
        private float maxScale = 3f;
        private float[] mat;
        private int viewWidth;
        private int viewHeight;
      
        private static final int CLICK = 3;
      
        private float saveScale = 1f;
      
        private float origWidth;
        private float origHeight;
      
        private int oldMeasuredWidth;
        private int oldMeasuredHeight;
     
        private ScaleGestureDetector scaleDetector;
     
        private Context context;
     
        public TouchImageView(Context context) {  
            super(context);  
            sharedConstructing(context);  
        } 

        public TouchImageView(Context context, AttributeSet attrs) {  
            super(context, attrs);  
            sharedConstructing(context);  
        }  
      
        private void sharedConstructing(Context context) {  
      
            super.setClickable(true);  
      
            this.context = context;  
      
            scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
      
            matrix = new Matrix();  
      
            mat = new float[9];
      
            setImageMatrix(matrix);  
      
            setScaleType(ScaleType.MATRIX);  
     
            setOnTouchListener(new OnTouchListener() {  
     
                @Override  
                public boolean onTouch(View v, MotionEvent event) {  
      
                    scaleDetector.onTouchEvent(event);
      
                    PointF curr = new PointF(event.getX(), event.getY());  
      
                    switch (event.getAction()) {  
      
                        case MotionEvent.ACTION_DOWN:  
      
                           last.set(curr);  
      
                            start.set(last);  
      
                            mode = DRAG;  
      
                            break;  
     
                        case MotionEvent.ACTION_MOVE:  
      
                            if (mode == DRAG) {  
      
                                float deltaX = curr.x - last.x;  
      
                                float deltaY = curr.y - last.y;  
      
                                float fixTransX = getFixDragTrans(deltaX, viewWidth,
                                        origWidth * saveScale);
      
                                float fixTransY = getFixDragTrans(deltaY, viewHeight,
                                        origHeight * saveScale);
      
                                matrix.postTranslate(fixTransX, fixTransY);  
      
                                fixTrans();  
      
                                last.set(curr.x, curr.y);  
      
                            }  
      
                            break;  
      
                        case MotionEvent.ACTION_UP:  
      
                            mode = NONE;  
      
                            int xDiff = (int) Math.abs(curr.x - start.x);  
      
                            int yDiff = (int) Math.abs(curr.y - start.y);  
      
                            if (xDiff < CLICK && yDiff < CLICK)  {
                                performClick();
                            }

                            break;  
      
                        case MotionEvent.ACTION_POINTER_UP:  
      
                            mode = NONE;  
      
                            break;
                        default:
      
                    }  
      
                    setImageMatrix(matrix);  
      
                    invalidate();  
      
                    return true; // indicate event was handled  
      
                }  
     
            });
        }

    /**
     * Sets maximum zoom multiplier.
     *
     * @param zoom multiplier
     */
    public void setMaxZoom(float zoom) {
      
            maxScale = zoom;
      
        }  
      
        private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {  
      
            @Override  
            public boolean onScaleBegin(ScaleGestureDetector detector) {  
      
                mode = ZOOM;  
      
                return true;  
      
            }  

            @Override  
            public boolean onScale(ScaleGestureDetector detector) {  
      
                float scaleFactor = detector.getScaleFactor();
      
                float origScale = saveScale;  
      
                saveScale *= scaleFactor;
      
                if (saveScale > maxScale) {  
      
                    saveScale = maxScale;  
      
                    scaleFactor = maxScale / origScale;
      
                } else if (saveScale < minScale) {  
      
                    saveScale = minScale;  
      
                    scaleFactor = minScale / origScale;
      
                }  
     
                if (origWidth * saveScale <= viewWidth || origHeight * saveScale <= viewHeight) {
                    matrix.postScale(scaleFactor, scaleFactor, viewWidth / 2, viewHeight / 2);
                } else {
                    matrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(),
                            detector.getFocusY());
                }
                fixTrans();  
      
                return true;  
      
            }  
      
        }  
      
        private void fixTrans() {
      
            matrix.getValues(mat);
      
            float transX = mat[Matrix.MTRANS_X];
      
            float transY = mat[Matrix.MTRANS_Y];
      
            float fixTransX = getFixTrans(transX, viewWidth, origWidth * saveScale);  
      
            float fixTransY = getFixTrans(transY, viewHeight, origHeight * saveScale);  
      
            if (fixTransX != 0 || fixTransY != 0)  {
                matrix.postTranslate(fixTransX, fixTransY);
            }
        }  
      
       
      
        private float getFixTrans(float trans, float viewSize, float contentSize) {
      
            float minTrans;
            float maxTrans;
      
            if (contentSize <= viewSize) {  
      
                minTrans = 0;  
      
                maxTrans = viewSize - contentSize;  
      
            } else {  
      
                minTrans = viewSize - contentSize;  
      
                maxTrans = 0;  
      
            }  
      
            if (trans < minTrans) {
                return -trans + minTrans;
            }

            if (trans > maxTrans) {
                return -trans + maxTrans;
            }
      

      
            return 0;  
      
        }  
     
        private float getFixDragTrans(float delta, float viewSize, float contentSize) {
      
            if (contentSize <= viewSize) {  
      
                return 0;  
      
            }  
      
            return delta;  
      
        }  
      
        @Override  
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
      
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);  
      
            viewWidth = MeasureSpec.getSize(widthMeasureSpec);  
      
            viewHeight = MeasureSpec.getSize(heightMeasureSpec);  
      
            //  
            // Rescales image on rotation  
            //  
            if (oldMeasuredHeight == viewWidth && oldMeasuredHeight == viewHeight  
      
                    || viewWidth == 0 || viewHeight == 0)  {
                return;
            }

            oldMeasuredHeight = viewHeight;  
      
            oldMeasuredWidth = viewWidth;
      
            if (saveScale == 1) {  
      
                //Fit to screen.  
      
                float scale;  
      
                Drawable drawable = getDrawable();  
      
                if (drawable == null || drawable.getIntrinsicWidth() == 0
                        || drawable.getIntrinsicHeight() == 0) {
                    return;
                }

                int bmWidth = drawable.getIntrinsicWidth();  
      
                int bmHeight = drawable.getIntrinsicHeight();  
      
                Log.d("bmSize", "bmWidth: " + bmWidth + " bmHeight : " + bmHeight);  
      
                float scaleX = (float) viewWidth / (float) bmWidth;  
      
                float scaleY = (float) viewHeight / (float) bmHeight;  
      
                scale = Math.min(scaleX, scaleY);  
      
                matrix.setScale(scale, scale);  
      
                // Center the image  
      
                float redundantYSpace = (float) viewHeight - (scale * (float) bmHeight);  
      
                float redundantXSpace = (float) viewWidth - (scale * (float) bmWidth);  
      
                redundantYSpace /= (float) 2;  
      
                redundantXSpace /= (float) 2;  
      
                matrix.postTranslate(redundantXSpace, redundantYSpace);  
      
                origWidth = viewWidth - 2 * redundantXSpace;  
      
                origHeight = viewHeight - 2 * redundantYSpace;  
      
                setImageMatrix(matrix);  
      
            }  
      
            fixTrans();  
      
        }  
      
    } 


