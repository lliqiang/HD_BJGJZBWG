package com.example.lenovo.hd_beijing_meseum.defiteview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lenovo.hd_beijing_meseum.R;
import com.example.lenovo.hd_beijing_meseum.utils.ColorUtils;


public class HDialogBuilder extends Dialog {

    private boolean cancelable;
    private LinearLayout rootPanel;
    private LinearLayout topPanel;
    private LinearLayout customPanel;
    private View mDivider;
    private TextView mTitle;
    private TextView mMsg;
    private ImageView mIcon;
    private Button mBtnP;
    private Button mBtnN;

    public HDialogBuilder(Context context) {
        super(context, R.style.hd_dialog_dim);
        init(context);
    }

    public HDialogBuilder(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);
    }


    private void init(Context context) {
        View dialogContainer = View.inflate(context, R.layout.layout_hd_dialog, null);

        RelativeLayout outerRelative = (RelativeLayout) dialogContainer.findViewById(R.id.outer);
        rootPanel = (LinearLayout) dialogContainer.findViewById(R.id.rootPanel);
        topPanel = (LinearLayout) dialogContainer.findViewById(R.id.topPanel);
        customPanel = (LinearLayout) dialogContainer.findViewById(R.id.customPanel);
        mIcon = (ImageView) dialogContainer.findViewById(R.id.icon);
        mDivider = dialogContainer.findViewById(R.id.titleDivider);
        mTitle = (TextView) dialogContainer.findViewById(R.id.alertTitle);
        mMsg = (TextView) dialogContainer.findViewById(R.id.alertMsg);
        mBtnP = (Button) dialogContainer.findViewById(R.id.dialog_btn_n);
        mBtnN = (Button) dialogContainer.findViewById(R.id.dialog_btn_p);

        setContentView(dialogContainer);

        outerRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cancelable)
                    dismiss();
            }
        });
    }

    public HDialogBuilder title(CharSequence title) {
        topPanel.setVisibility(View.VISIBLE);
        mTitle.setText(title);
        return this;
    }


    public HDialogBuilder titleColor(String colorString) {
        mTitle.setTextColor(Color.parseColor(colorString));
        return this;
    }

    public HDialogBuilder titleColor(int color) {
        mTitle.setTextColor(color);
        return this;
    }

    public HDialogBuilder message(CharSequence msg) {
        mMsg.setVisibility(View.VISIBLE);
        mMsg.setText(msg);
        return this;
    }

    public HDialogBuilder msgColor(String colorString) {
        mMsg.setTextColor(Color.parseColor(colorString));
        return this;
    }

    public HDialogBuilder msgColor(int color) {
        mMsg.setTextColor(color);
        return this;
    }

    public HDialogBuilder dividerColor(String colorString) {
        mDivider.setBackgroundColor(Color.parseColor(colorString));
        return this;
    }

    public HDialogBuilder dividerColor(int color) {
        mDivider.setBackgroundColor(color);
        return this;
    }

    public HDialogBuilder dialogColor(String colorString) {
        rootPanel.getBackground().setColorFilter(ColorUtils.getColorFilter(Color.parseColor
                (colorString)));
        return this;
    }

    public HDialogBuilder dialogColor(int color) {
        if (color == Color.TRANSPARENT) {
            rootPanel.setBackgroundColor(color);
        } else {
            rootPanel.getBackground().setColorFilter(ColorUtils.getColorFilter(color));
        }
        return this;
    }

    public HDialogBuilder withIcon(int drawableResId) {
        mIcon.setImageResource(drawableResId);
        return this;
    }

    public HDialogBuilder withIcon(Drawable icon) {
        mIcon.setImageDrawable(icon);
        return this;
    }

    public HDialogBuilder btnBg(int resid) {
        mBtnP.setBackgroundResource(resid);
        mBtnN.setBackgroundResource(resid);
        return this;
    }

    public HDialogBuilder pBtnText(CharSequence text) {
        mBtnP.setVisibility(View.VISIBLE);
        mBtnP.setText(text);
        return this;
    }

    public HDialogBuilder nBtnText(CharSequence text) {
        mBtnN.setVisibility(View.VISIBLE);
        mBtnN.setText(text);
        return this;
    }

    public HDialogBuilder pBtnClickListener(View.OnClickListener click) {
        mBtnP.setOnClickListener(click);
        return this;
    }

    public HDialogBuilder nBtnClickListener(View.OnClickListener click) {
        mBtnN.setOnClickListener(click);
        return this;
    }

    public HDialogBuilder setCustomView(Context context, int layoutId) {
        View customView = View.inflate(context, layoutId, null);
        if (customPanel.getChildCount() > 0) {
            customPanel.removeAllViews();
        }
        customPanel.addView(customView);
        customPanel.setVisibility(View.VISIBLE);
        return this;
    }

    public HDialogBuilder setCustomView(View view) {
        if (customPanel.getChildCount() > 0) {
            customPanel.removeAllViews();
        }
        customPanel.addView(view);
        customPanel.setVisibility(View.VISIBLE);
        return this;
    }

    public HDialogBuilder cancelable(boolean cancelable) {
        setCancelable(cancelable);
        return this;
    }

    public HDialogBuilder outsideCancelable(boolean outsideCancelable) {
        cancelable = outsideCancelable;
        return this;
    }

    public HDialogBuilder setTypeface(Typeface typeface) {
        mTitle.setTypeface(typeface);
        mMsg.setTypeface(typeface);
        mBtnP.setTypeface(typeface);
        mBtnN.setTypeface(typeface);
        return this;
    }

}
