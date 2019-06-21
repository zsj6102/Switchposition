package com.zsj.myapplication;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int startY;
    private int endY;
    private TextView leftCityTextView;
    private TextView rightCityTextView;
    private ValueAnimator endCityAnimator;
    private ValueAnimator startCityAnimation;
    int leftMoveX;
    int rightMoveX;
    int height = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        leftCityTextView = ((TextView) this.findViewById(R.id.left_tv));
        rightCityTextView = ((TextView) this.findViewById(R.id.right_tv));

        Button mBtn = ((Button) this.findViewById(R.id.btn));
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCityAnimation.start();
                endCityAnimator.start();
            }
        });
    }

    private void getLocation() {
        int[] startYLocation = new int[2];

        int resourceId = getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            height = getApplicationContext().getResources().getDimensionPixelSize(resourceId);
//        }

        leftCityTextView.getLocationOnScreen(startYLocation);//获取坐标
        int[] endYLocation = new int[2];
        rightCityTextView.getLocationOnScreen(endYLocation);
        startY = startYLocation[1]-height;//0为x坐标
        endY = endYLocation[1]-height;
          leftMoveX = endY - startY;
         rightMoveX = endY - startY;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        getLocation();
        startCityAnimation = ValueAnimator.ofInt(0, leftMoveX).setDuration(2000);
        startCityAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                //重新布局
                leftCityTextView.layout(leftCityTextView.getLeft(),startY+value ,leftCityTextView.getRight(),leftCityTextView.getHeight()+startY+value );
//                leftCityTextView.layout(startY + value,
//                        leftCityTextView.getTop(),
//                        startX + value + leftCityTextView.getWidth(),
//                        leftCityTextView.getBottom());
            }
        });

        endCityAnimator = ValueAnimator.ofInt(0, rightMoveX).setDuration(2000);
        endCityAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                //重新布局
                rightCityTextView.layout(rightCityTextView.getLeft(),endY-value ,rightCityTextView.getRight(),endY+rightCityTextView.getHeight()-value );
//                rightCityTextView.layout(endX - value,
//                        rightCityTextView.getTop(),
//                        endX + rightCityTextView.getWidth() - value,
//                        rightCityTextView.getBottom());
            }
        });

        endCityAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //用于下次交换
                TextView tempTextView = leftCityTextView;
                leftCityTextView = rightCityTextView;
                rightCityTextView = tempTextView;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }
}
