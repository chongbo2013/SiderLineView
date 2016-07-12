package example.ferris.com.siderlineview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * 高仿猎豹cm桌面，所有应用，快捷字母栏
 * 459821731@qq.com
 * Created by ferris.xu on 2016/7/12.
 */
public class SiderLineView extends View implements ValueAnimator.AnimatorUpdateListener{


    String[] abc={"A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A","A"};
    Paint mPaintCircle=new Paint();
    Path mCirclePaht=new Path();
    Rect mDrawRect=new Rect();
    PathMeasure measure=new PathMeasure();
    Paint mColorPaht=new Paint();
    float radioHight=0f;
    ValueAnimator valueAnimator=null;
    public SiderLineView(Context context) {
        super(context);
        init();
    }

    public SiderLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SiderLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDrawRect.set(0,0,w-getPaddingLeft()-getPaddingRight(),h-getPaddingTop()-getPaddingBottom());
        seleteDeley=-h/20;

    }

    public void init() {
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setColor(Color.GREEN);
        mPaintCircle.setTextSize(30); //以px为单位
        mPaintCircle.setStrokeWidth((float) 1.0);              //线宽
        mPaintCircle.setStyle(Paint.Style.STROKE);
        mPaintCircle.setTextAlign(Paint.Align.CENTER);



        mColorPaht.setColor(Color.BLACK);
        setPadding(16,0,16,0);

    }





    @Override
    protected void onDraw(Canvas canvas) {

        mCirclePaht.reset();
        mCirclePaht.moveTo(getPaddingLeft(),getPaddingTop()+mDrawRect.height()/2);
        mCirclePaht.quadTo(getPaddingLeft()+mDrawRect.width()/2,getPaddingTop()+mDrawRect.height()/2-radioHight*(float)(mDrawRect.height()/2),getPaddingLeft()+mDrawRect.width(),getPaddingTop()+mDrawRect.height()/2);
        measure.setPath(mCirclePaht,false);

        canvas.save();
        canvas.drawRect(new Rect(0,0,getWidth(),getHeight()),mColorPaht);
        canvas.drawPath(mCirclePaht,mPaintCircle);

        for(int i=0;i<abc.length;i++){

            float indexDeldy= ((float)(i+1)/(float)abc.length);
            float lengthDeldy = measure.getLength()*indexDeldy;
            float[] pos=new float[2];
            measure.getPosTan(lengthDeldy,pos,null);

            canvas.save();
            float centerTextWidth = mPaintCircle.measureText(abc[i]);


            float centerTextHeiht=Math.abs(mPaintCircle.ascent()+ mPaintCircle.descent())/2;

            int deleyHight=0;
            if(i==seleteAbc){
                deleyHight=seleteDeley;
            }
            canvas.drawText(abc[i],pos[0]-centerTextWidth,pos[1]+centerTextHeiht+deleyHight , mPaintCircle);

            canvas.restore();
        }

        canvas.restore();
    }

    int[] pointDown=new int[2];

    int seleteAbc=-1;
    int seleteLast=-1;
    int seleteDeley=0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        pointDown[0]= (int) event.getX();
        pointDown[1]=(int)event.getY();

        float tempX= pointDown[0]-getPaddingLeft()-getPaddingRight();
        float tempX2=mDrawRect.width();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                pointDown[0]= (int) event.getX();
                pointDown[1]=(int)event.getY();

                seleteAbc= (int) Math.ceil((tempX/tempX2)*((float) abc.length));
                Log.d("seleteAbc","seleteAbc="+seleteAbc);
                seleteLast=seleteAbc;
                startAnimaton();

                break;
            case MotionEvent.ACTION_MOVE:
                //根据X 判别选中

                seleteAbc= (int) Math.ceil((tempX/tempX2)*((float) abc.length));
                Log.d("seleteAbc","seleteAbc="+seleteAbc);
                seleteLast=seleteAbc;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                seleteAbc=-1;
                endAnimation();
                break;
        }

        return true;
    }

    public void startAnimaton(){
        if(valueAnimator!=null&&valueAnimator.isRunning()){
            valueAnimator.cancel();
        }
        ValueAnimator tempValueAnimaton=ValueAnimator.ofFloat(radioHight,1f);
        tempValueAnimaton.setDuration(200);
        tempValueAnimaton.setInterpolator(new AccelerateInterpolator());
        tempValueAnimaton.addUpdateListener(this);
        valueAnimator=tempValueAnimaton;
        valueAnimator.start();
    }


    public void endAnimation(){
        if(valueAnimator!=null&&valueAnimator.isRunning()){
            valueAnimator.cancel();
        }
        ValueAnimator tempValueAnimaton=ValueAnimator.ofFloat(radioHight,0f);
        tempValueAnimaton.setDuration(200);
        tempValueAnimaton.setInterpolator(new AccelerateInterpolator());
        tempValueAnimaton.addUpdateListener(this);
        valueAnimator=tempValueAnimaton;
        valueAnimator.start();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        radioHight= (float) animation.getAnimatedValue();
        invalidate();
    }
}
