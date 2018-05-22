package com.beacool.widget.locate.widgets.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beacool.widget.models.MapPositionData;
import com.beacool.widget.tools.BitmapTool;


/**
 * Package com.beacool.widget.install.widgets.
 * Created by JoshuaYin on 16/2/14.
 * Company Beacool IT Ltd.
 * <p/>
 * Description:
 */
public class PositionView extends LinearLayout {
    private static final String TAG = "PositionView";

    private Context mContext;
    private TextView mTxtName;
    private ImageView mImgPos;
    private String mPosName = "";
    private String mUserID = "";
    private long mTime = 0;

    public interface OnUserViewClickListener {
        void onClick(String name, int viewId);
    }

    private OnUserViewClickListener mListener;

    // Draw data
    private MapPositionData mPosition;

    public PositionView(Context context) {
        super(context);
        init(context);
    }

    public PositionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PositionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        mTxtName = new TextView(mContext);
        mImgPos = new ImageView(mContext);
    }

    private boolean isAnchorCenter = false;
    private Bitmap mBmpAlive = null, mBmpLost = null;

    public int initView(boolean anchorCenter, String userID,
                        MapPositionData position, OnUserViewClickListener listener) {
        return initView(anchorCenter, userID, "", BitmapTool.getBmpFromAsset(mContext, "icon/icon_pos_1.png"),
                BitmapTool.getBmpFromAsset(mContext, "icon/icon_pos_2.png"), position, listener);
    }

    public int initView(boolean anchorCenter, String userID, String name, Bitmap alive, Bitmap lost,
                        MapPositionData position, OnUserViewClickListener listener) {
        mBmpAlive = alive;
        mBmpLost = lost;
        if (mBmpAlive == null || mBmpLost == null)
            return -1;

        isAnchorCenter = anchorCenter;
        setOrientation(VERTICAL);
        mPosName = name == null ? "" : name;
        mUserID = userID;
        mListener = listener;
        mPosition = position;


        mImgPos.setImageBitmap(mBmpAlive);
        mImgPos.setScaleType(ImageView.ScaleType.FIT_CENTER);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onClick(mPosName, getId());
            }
        });
//        mImgPos.setId(View.generateViewId());

//        int id = View.generateViewId();
//        setId(id);

        if (TextUtils.isEmpty(mPosName)) {
            LayoutParams paramsImg = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            paramsImg.gravity = Gravity.CENTER;

            addView(mImgPos, paramsImg);
        } else {
            mTxtName.setGravity(Gravity.CENTER);
            mTxtName.setTextColor(0xff000000);
            mTxtName.setSingleLine(true);
            mTxtName.setText(mPosName);
            mTxtName.setTextSize(8);
//            mTxtName.setId(View.generateViewId());

            LayoutParams paramsTxt = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            paramsTxt.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;

            LayoutParams paramsImg = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            paramsImg.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

            addView(mTxtName, paramsTxt);
            addView(mImgPos, paramsImg);
        }

        return 0;
    }

    float getPosAnchorMarginBottom() {
        if (isAnchorCenter) {
            return mImgPos.getMeasuredHeight() / 2;
        }

        return 0;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        LogTool.LogV(TAG,"onDraw--->");
        super.onDraw(canvas);
    }

    public static final int FLAG_USER_ALIVE = 3000;
    public static final int FLAG_USER_LOST = 3001;
    private int userStatus = FLAG_USER_ALIVE;

    public void changeImage(int flag) {
        if (userStatus == flag)
            return;

        userStatus = flag;
        if (FLAG_USER_ALIVE == userStatus)
            mImgPos.setImageBitmap(mBmpAlive);
        else if (FLAG_USER_LOST == userStatus)
            mImgPos.setImageBitmap(mBmpLost);
    }

    public MapPositionData getmPosition() {
        return mPosition;
    }

    public String getmPosName() {
        return mPosName;
    }

    public void setmPosName(String mPosName) {
        this.mPosName = mPosName;
    }

    public String getmUserID() {
        return mUserID;
    }

    public long getmTime() {
        return mTime;
    }

    public void setmTime(long mTime) {
        this.mTime = mTime;
    }

    Bitmap getBmpAlive() {
        return mBmpAlive;
    }
}
