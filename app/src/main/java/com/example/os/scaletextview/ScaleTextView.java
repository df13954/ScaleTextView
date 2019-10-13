package com.example.os.scaletextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;

/**
 * @author dr
 * create at 2019/10/12 10:51 AM
 */
public class ScaleTextView extends android.support.v7.widget.AppCompatTextView {
    private static final int KEY_DEFAULT_SCALE = -1;
    private String foregroundsText;
    private int foregroundsColor;
    private int start;
    private int end;
    private int scale;


    public ScaleTextView(Context context) {
        super(context);
    }

    public ScaleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ScaleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScaleTextView);
        if (typedArray != null) {
            //前置文字
            foregroundsText = typedArray.getString(R.styleable.ScaleTextView_foreground_text);
            //颜色
            foregroundsColor = typedArray.getColor(R.styleable.ScaleTextView_foreground_text_color,
                    Color.parseColor("#ff0000"));
            //起点
            start = typedArray.getInt(R.styleable.ScaleTextView_foreground_color_start, 0);
            //终点
            end = typedArray.getInt(R.styleable.ScaleTextView_foreground_color_end, 1);
            //缩放长度
            scale = typedArray.getInt(R.styleable.ScaleTextView_scale_content_length, KEY_DEFAULT_SCALE);
            typedArray.recycle();
        }

        CharSequence text = getText();
        SpannableStringBuilder scaleString = scaleString(text, scale);
        setText(scaleString);
    }

    public SpannableStringBuilder scaleString(CharSequence str, int size) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (TextUtils.isEmpty(str)) {
            return spannableStringBuilder;
        }
        int l = str.length();

        float scale = (float) (size - l) / (l - 1);
        //当前置上色文字不是空的时候,才追加在前面
        if (!TextUtils.isEmpty(foregroundsText)) {
            spannableStringBuilder.append(foregroundsText);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(foregroundsColor);
            //结束位置必须少于长度,起点必须大于0.如果有误,直接奔溃
            if (start < 0 || end > foregroundsText.length()) {
                throw new IllegalArgumentException("结束位置必须少于长度,起点必须大于0");
            }
            spannableStringBuilder.setSpan(colorSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (l == size || KEY_DEFAULT_SCALE == size) {
            //如果缩放长度,和文字长度一致,直接拼接,不需要缩放
            return spannableStringBuilder.append(str);
        }
        if (l > size || str.length() == 1) {
            return spannableStringBuilder.append(str);
        }
        for (int i = 0; i < l; i++) {
            spannableStringBuilder.append(str.charAt(i));
            if (i != l - 1) {
                //全角空格
                SpannableString strChar = new SpannableString("　");
                strChar.setSpan(new ScaleXSpan(scale), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.append(strChar);
            }
        }
        return spannableStringBuilder;
    }
}
