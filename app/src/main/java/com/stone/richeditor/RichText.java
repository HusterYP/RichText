package com.stone.richeditor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BlurMaskFilter;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.support.annotation.RequiresApi;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.QuoteSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.Button;
import android.widget.EditText;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

/**
 * @created by PingYuan at 9/2/18
 * @email: husteryp@gmail.com
 * @description:
 */
public class RichText extends EditText {
    public RichText(Context context) {
        super(context);
    }

    public RichText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RichText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RichText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    // 加粗
    public void bold() {
        if (isTextSelected()) {
            getEditableText().setSpan(new StyleSpan(Typeface.BOLD), getSelectionStart(),
                    getSelectionEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    // 斜体
    public void italic() {
        if (isTextSelected()) {
            getEditableText().setSpan(new TypefaceSpan("monospace"), getSelectionStart(),
                    getSelectionEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            getEditableText().setSpan(new StyleSpan(Typeface.ITALIC), getSelectionStart(),
                    getSelectionEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    // 下划线
    public void underline() {
        if (isTextSelected()) {
            getEditableText().setSpan(new UnderlineSpan(), getSelectionStart(),
                    getSelectionEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    // 删除线
    public void deleteline() {
        if (isTextSelected()) {
            getEditableText().setSpan(new StrikethroughSpan(), getSelectionStart(),
                    getSelectionEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    // 引用
    public void quote() {
        if (isTextSelected()) {
            getEditableText().setSpan(new QuoteSpan(0xff000000), getSelectionStart(),
                    getSelectionEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    // 链接
    public void url() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.url_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();
        Button cancel = view.findViewById(R.id.cancle);
        Button ok = view.findViewById(R.id.ok);
        final EditText text = view.findViewById(R.id.url);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = text.getText().toString();
                dialog.dismiss();
                if (isTextSelected()) {
                    getEditableText().setSpan(new URLSpan(url), getSelectionStart(),
                            getSelectionEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    // 在单击链接时凡是有要执行的动作，都必须设置MovementMethod对象
//                    contentTV.setMovementMethod(LinkMovementMethod.getInstance());
                    // 设置点击后的颜色，这里涉及到ClickableSpan的点击背景
//                    contentTV.setHighlightColor(0xff8FABCC);
                }
            }
        });
    }

    // 字体大小
    public void size() {
        if (isTextSelected()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View view = LayoutInflater.from(getContext()).inflate(R.layout.url_dialog, null);
            builder.setView(view);
            final AlertDialog dialog = builder.create();
            dialog.show();
            Button cancel = view.findViewById(R.id.cancle);
            Button ok = view.findViewById(R.id.ok);
            final EditText text = view.findViewById(R.id.url);
            cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });
            ok.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int textSize = Integer.parseInt(text.getText().toString());
                    getEditableText().setSpan(new AbsoluteSizeSpan(textSize, true),
                            getSelectionStart(),
                            getSelectionEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    dialog.dismiss();
                }
            });
        }
    }

    // 字体颜色
    public void color() {
        ColorPickerDialogBuilder
                .with(getContext())
                .setTitle("Choose color")
                .initialColor(0xf00)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {

                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[]
                            allColors) {
                        if (isTextSelected()) {
                            getEditableText().setSpan(new ForegroundColorSpan(selectedColor),
                                    getSelectionStart(),
                                    getSelectionEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    // 字体背景色
    public void background() {
        ColorPickerDialogBuilder
                .with(getContext())
                .setTitle("Choose color")
                .initialColor(0xf00)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {

                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[]
                            allColors) {
                        if (isTextSelected()) {
                            getEditableText().setSpan(new BackgroundColorSpan(selectedColor),
                                    getSelectionStart(),
                                    getSelectionEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    // 字体
    public void font() {
        if (isTextSelected()) {
            Typeface typeface = Typeface.createFromAsset(getContext().getAssets(),
                    "fonts/AlexBrush-Regular.ttf");
            getEditableText().setSpan(new CustomTypefaceSpan("", typeface), getSelectionStart(),
                    getSelectionEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    // 文字上标: SuperscriptSpan
    public void superScriptSpan() {
        Parcel parcel = Parcel.obtain();
        parcel.writeString(getEditableText().toString());
        if (isTextSelected()) {
            getEditableText().setSpan(new SuperscriptSpan(parcel), getSelectionStart(),
                    getSelectionEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        parcel.recycle();
    }

    // 文字下标: SubscriptSpan
    public void subScriptSpan() {
        Parcel parcel = Parcel.obtain();
        parcel.writeString(getEditableText().toString());
        if (isTextSelected()) {
            getEditableText().setSpan(new SubscriptSpan(parcel), getSelectionStart(),
                    getSelectionEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        parcel.recycle();
    }

    // 文字模糊效果
    // TODO 可以扩展为艺术字, 其中有多种效果设置
    public void blur() {
        // TODO 这里MaskFilterSpan中传入的MaskFilter, 应该还有其他Filter
        if (isTextSelected()) {
            MaskFilter blurMask = new BlurMaskFilter(5f, BlurMaskFilter.Blur.NORMAL);
            MaskFilterSpan span = new MaskFilterSpan(blurMask);
            // 需要加上该效果, 否则Blur效果显示不出来, 这里受到的启发是在设置字体为斜体时, 也需要加该效果
            getEditableText().setSpan(new TypefaceSpan("monospace"), getSelectionStart(),
                    getSelectionEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            getEditableText().setSpan(span, getSelectionStart(),
                    getSelectionEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    // 文字浮雕
    public void emboss() {
        if (isTextSelected()) {
            MaskFilterSpan span = new MaskFilterSpan(new EmbossMaskFilter(new float[]{1, 5, 1},
                    0.5f, 10, 7.5f));
            // 需要加上该效果, 否则浮雕效果显示不出来
            getEditableText().setSpan(new TypefaceSpan("monospace"), getSelectionStart(),
                    getSelectionEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            getEditableText().setSpan(span, getSelectionStart(),
                    getSelectionEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    // 插入图片
    public void imgInsert() {

    }

    // 文字前圆点
    // TODO 这个属性和blur和emboss等合用好像有问题
    public void bullet() {
//        if (isTextSelected()) {
//            getEditableText().setSpan(new BulletSpan(66, 0xff303F9F), getSelectionStart(),
//                    getSelectionEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
    }


    // 判断当前是否有文字选中
    private boolean isTextSelected() {
        return getSelectionStart() < getSelectionEnd();
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new DeleteInputConnection(super.onCreateInputConnection(outAttrs),
                true);
    }

    private class DeleteInputConnection extends InputConnectionWrapper {

        public DeleteInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            return super.sendKeyEvent(event);
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            if (beforeLength == 1 && afterLength == 0) {
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_DEL))
                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP,
                        KeyEvent.KEYCODE_DEL));
            }

            return super.deleteSurroundingText(beforeLength, afterLength);
        }

    }
}
