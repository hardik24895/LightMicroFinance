package com.lightmicrofinance.app.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ms_square.etsyblur.Blur;
import com.ms_square.etsyblur.BlurConfig;
import com.ms_square.etsyblur.BlurEngine;
import com.ms_square.etsyblur.ViewUtil;

public class BlurBottomSheetDialog extends BottomSheetDialogFragment {

    public static final int DEFAULT_ANIM_DURATION = 400;
    public static final boolean DEFAULT_BACKGROUND_DIMMING_ENABLED = true;
    private static final String TAG = com.ms_square.etsyblur.BlurDialogFragment.class.getSimpleName();
    private Blur blur;

    private ViewGroup root;

    private ImageView blurImgView;
    private final ViewTreeObserver.OnPreDrawListener preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
            root.getViewTreeObserver().removeOnPreDrawListener(this);
            // makes sure to get the complete drawing after the layout pass
            root.post(new Runnable() {
                @Override
                public void run() {
                    setUpBlurringViews();
                    startEnterAnimation();
                }
            });
            return true;
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        blur = new Blur(context, blurConfig());

        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            root = (ViewGroup) activity.getWindow().getDecorView();
            if (root.isShown()) {
                setUpBlurringViews();
                startEnterAnimation();
            } else {
                root.getViewTreeObserver().addOnPreDrawListener(preDrawListener);
            }
        } else {
            Log.w(TAG, "onAttach(Context context) - context is not type of Activity. Currently Not supported.");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // will be only called if onCreateView returns non-null view
        // set to dismiss when touched outside content view
        view.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setOnTouchListener(null);
                dismiss();
                return true;
            }
        });
    }

    @Override
    public void onStart() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            if (!backgroundDimmingEnabled()) {
                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            }
        }
        super.onStart();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        startExitAnimation();
        super.onDismiss(dialog);
    }

    @Override
    public void onDetach() {
        root.getViewTreeObserver().removeOnPreDrawListener(preDrawListener);
        blur.destroy();
        super.onDetach();
    }

    /**
     * Configuration object for the blur effect.
     * If not overwritten, it just returns {@link BlurConfig#DEFAULT_CONFIG} which uses {@link com.ms_square.etsyblur.SimpleAsyncPolicy}.
     *
     * @return blur operation configuration
     */
    protected BlurConfig blurConfig() {
        return BlurConfig.DEFAULT_CONFIG;
    }

    /**
     * Controls if everything behind this window will be dimmed.
     *
     * @return true if dimming should be enabled
     */
    protected boolean backgroundDimmingEnabled() {
        return DEFAULT_BACKGROUND_DIMMING_ENABLED;
    }

    /**
     * Alpha animation duration (ms) of the blurred image added in this fragment's hosting activity.
     *
     * @return animation duration in ms
     */
    protected int animDuration() {
        return DEFAULT_ANIM_DURATION;
    }

    private void setUpBlurringViews() {
        Rect visibleFrame = new Rect();
        root.getWindowVisibleDisplayFrame(visibleFrame);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(visibleFrame.right - visibleFrame.left,
                visibleFrame.bottom - visibleFrame.top);
        params.setMargins(visibleFrame.left, visibleFrame.top, 0, 0);

        blurImgView = new ImageView(root.getContext());
        blurImgView.setLayoutParams(params);
        blurImgView.setAlpha(0f);

        root.addView(blurImgView);

        // apply blur effect
        Bitmap bitmapToBlur = ViewUtil.drawViewToBitmap(root, visibleFrame.right,
                visibleFrame.bottom, visibleFrame.left, visibleFrame.top, blurConfig().downScaleFactor(),
                blurConfig().overlayColor());
        blur.execute(bitmapToBlur, true, new BlurEngine.Callback() {
            @Override
            public void onFinished(Bitmap blurredBitmap) {
                blurImgView.setImageBitmap(blurredBitmap);
            }
        });
    }

    private void startEnterAnimation() {
        if (blurImgView != null) {
            ViewUtil.animateAlpha(blurImgView, 0f, 1f, animDuration(), null);
        }
    }

    private void startExitAnimation() {
        if (blurImgView != null) {
            ViewUtil.animateAlpha(blurImgView, 1f, 0f, animDuration(), new Runnable() {
                @Override
                public void run() {
                    root.removeView(blurImgView);
                }
            });
        }
    }
}
