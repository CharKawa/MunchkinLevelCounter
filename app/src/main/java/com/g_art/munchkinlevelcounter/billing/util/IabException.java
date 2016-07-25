package com.g_art.munchkinlevelcounter.billing.util;


import com.g_art.munchkinlevelcounter.billing.IabResult;

/**
 * MunchinLevelCounter
 * Created by G_Art on 7/12/2014.
 * <p/>
 * Exception thrown when something went wrong with in-app billing.
 * An IabException has an associated IabResult (an error).
 * To get the IAB result that caused this exception to be thrown,
 * call {@link #getResult()}.
 */
public class IabException extends Exception {
    private IabResult mResult;

    private IabException(IabResult r) {
        this(r, null);
    }

    public IabException(int response, String message) {
        this(new IabResult(response, message));
    }

    private IabException(IabResult r, Exception cause) {
        super(r.getMessage(), cause);
        mResult = r;
    }

    public IabException(int response, String message, Exception cause) {
        this(new IabResult(response, message), cause);
    }

    /**
     * Returns the IAB result (error) that this exception signals.
     */
    public IabResult getResult() {
        return mResult;
    }
}
