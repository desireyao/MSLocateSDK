package com.beacool.widget.models;

/**
 * Package com.beacool.widget.install.utils.
 * Created by JoshuaYin on 16/3/2.
 * Company Beacool IT Ltd.
 * <p>
 * Description:
 */
public class MapPositionData {
	private boolean hasCalculateView = false;
	private double mCoordinateX = 0.f, mCoordinateY = 0.f;
	private double mPosX = 0.f, mPosY = 0.f;

	private double mLeft = 0.f, mRight = 0.f, mTop = 0.f, mBottom = 0.f;
	private double mCurWidth = 0.f, mCurHeight = 0.f;

	private int viewID = -1;

	/**
	 * Created by JoshuaYin on 16/3/2-11:21.
	 * Company Beacool IT Ltd.
	 * <p>
	 * Description:
	 * Setter
	 */
	public void setmCoordinateX(double mCoordinateX) {
		this.mCoordinateX = mCoordinateX;
	}

	public void setmCoordinateY(double mCoordinateY) {
		this.mCoordinateY = mCoordinateY;
	}

	public void setmPosX(double mPosX) {
		this.mPosX = mPosX;
	}

	public void setmPosY(double mPosY) {
		this.mPosY = mPosY;
	}

	public void setmBottom(double mBottom) {
		this.mBottom = mBottom;
	}

	public void setmLeft(double mLeft) {
		this.mLeft = mLeft;
	}

	public void setmRight(double mRight) {
		this.mRight = mRight;
	}

	public void setmTop(double mTop) {
		this.mTop = mTop;
	}

	public void setmCurHeight(double mCurHeight) {
		this.mCurHeight = mCurHeight;
	}

	public void setmCurWidth(double mCurWidth) {
		this.mCurWidth = mCurWidth;
	}

	public void setHasCalculateView(boolean hasCalculateView) {
		this.hasCalculateView = hasCalculateView;
	}

	public void setViewID(int viewID) {
		this.viewID = viewID;
	}

	/**
	 * Created by JoshuaYin on 16/3/2-11:22.
	 * Company Beacool IT Ltd.
	 * <p>
	 * Description:
	 * Getter
	 */
	public double getmCoordinateX() {
		return mCoordinateX;
	}

	public double getmCoordinateY() {
		return mCoordinateY;
	}

	public double getmPosX() {
		return mPosX;
	}

	public double getmPosY() {
		return mPosY;
	}

	public double getmBottom() {
		return mBottom;
	}

	public double getmLeft() {
		return mLeft;
	}

	public double getmRight() {
		return mRight;
	}

	public double getmTop() {
		return mTop;
	}

	public double getmCurHeight() {
		return mCurHeight;
	}

	public double getmCurWidth() {
		return mCurWidth;
	}

	public boolean hasCalculateView() {
		return hasCalculateView;
	}

	public int getViewID() {
		return viewID;
	}

	@Override
	public String toString() {
		return "MapPositionData{" + "\n" +
				"hasCalculateView=" + hasCalculateView + "\n" +
				", mCoordinateX=" + mCoordinateX + "\n" +
				", mCoordinateY=" + mCoordinateY + "\n" +
				", mPosX=" + mPosX + "\n" +
				", mPosY=" + mPosY + "\n" +
				", mLeft=" + mLeft + "\n" +
				", mRight=" + mRight + "\n" +
				", mTop=" + mTop + "\n" +
				", mBottom=" + mBottom + "\n" +
				", mCurWidth=" + mCurWidth + "\n" +
				", mCurHeight=" + mCurHeight + "\n" +
				", viewID=" + viewID + "\n" +
				'}';
	}
}
