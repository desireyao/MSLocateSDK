package com.beacool.widget.exception;

/**
 * Package com.beacool.widget.exception.
 * Created by JoshuaYin on 16/2/16.
 * Company Beacool IT Ltd.
 * <p/>
 * Description:
 */
public class AddViewException extends Throwable {
    protected static final String TAG = "AddViewException";

    public AddViewException(String detailMessage) {
        super(TAG + "--->" + detailMessage);
    }
}
