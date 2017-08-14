package ph.com.jeffreyvcabrera.stlukesdev.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ph.com.jeffreyvcabrera.stlukesdev.R;

/**
 * Created by Jeffrey on 5/15/2017.
 */

public class ValidationUtil {

    public static final String MODULE = "ValidationUtil";
    public static String TAG = "";

    public static boolean isEmailDataValid(EditText edtEmail, Context mContext,
                                           View mBGView) {
        TAG = "isEmailDataValid";
        Log.d(MODULE, TAG + " Called.");

        boolean isValid = true;
        try {
            edtEmail.setError(null);

            final String mEmail = edtEmail.getText().toString();

            boolean cancel = false;
            View focusView = null;

            if (TextUtils.isEmpty(mEmail)) {
                edtEmail.setError(mContext.getResources().getString(
                        R.string.error_empty_email));
                focusView = edtEmail;
                cancel = true;
            } else if (ValidationUtil.isValidEmailId(mEmail) == false) {
                edtEmail.setError(mContext.getResources().getString(
                        R.string.error_invalid_email));
                focusView = edtEmail;
                cancel = true;
            }

            if (cancel) {

                focusView.requestFocus();
                isValid = false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e(MODULE, TAG + " Exception Occurs : " + e);
        }

        return isValid;
    }

    public static boolean isPasswordDataValid(EditText edtPassword,
                                              Context mContext, View mBGView) {
        TAG = "isPasswordDataValid";
        Log.d(MODULE, TAG + " Called.");

        boolean isValid = true;
        try {
            edtPassword.setError(null);

            boolean cancel = false;
            View focusView = null;

            if (TextUtils.isEmpty(edtPassword.getText().toString().trim())) {
                edtPassword.setError(mContext.getResources().getString(
                        R.string.error_empty_password));
                focusView = edtPassword;
                cancel = true;
            }

            if (cancel) {

                focusView.requestFocus();
                isValid = false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e(MODULE, TAG + " Exception Occurs : " + e);
        }

        return isValid;
    }

    public static boolean isValidEmailId(String strEmail) {
        // TODO Auto-generated method stub
        TAG = "isValidEmailId";
        Log.d(MODULE, TAG);

        boolean isValid = true;

        try {
            if (strEmail == null) {
                isValid = false;
            } else {
                isValid = Patterns.EMAIL_ADDRESS.matcher(strEmail).matches();
            }

        } catch (Exception e) {
            Log.e(MODULE, TAG + ", Exception Occurs " + e);
        }

        return isValid;
    }

    public static boolean isValidPhoneNO(String strPhone) {
        // TODO Auto-generated method stub
        TAG = "isValidPhoneNO";
        Log.d(MODULE, TAG);

        boolean isValid = true;

        try {
            if (strPhone == null) {
                isValid = false;
            } else {
                isValid = Patterns.PHONE.matcher(strPhone).matches();
            }

        } catch (Exception e) {
            Log.e(MODULE, TAG + ", Exception Occurs " + e);
        }

        return isValid;
    }

    public static String convertUnixTimeStampToDate(String unixTime) {
        TAG = "convertUnixTimeStampToDate";
        Log.d(MODULE, TAG);

        String dateString = "";
        try {
            Log.d(MODULE, TAG + "unixTime " + unixTime);
            String timeLong = getTimeLongFromUnixTime(unixTime);

            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(Long.parseLong(timeLong));

            Date date = new Date(cal.getTimeInMillis());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",
                    Locale.getDefault());
            dateString = dateFormat.format(date);

        } catch (Exception e) {
            Log.e(MODULE, TAG + ", Exception Occurs " + e);
        }
        return dateString;
    }

    public static String getTimeLongFromUnixTime(String unixTime) {
        TAG = "getTimeLongFromUnixTime";
        Log.d(MODULE, TAG);

        String timeLong = "";
        try {
            Log.d(MODULE, TAG + "unixTime " + unixTime);
            timeLong = unixTime.substring(5, unixTime.length() - 1);
            Log.d(MODULE, TAG + "timeLong " + timeLong);
        } catch (Exception e) {
            Log.e(MODULE, TAG + ", Exception Occurs " + e);
        }
        return timeLong.trim();
    }

    public static String getFileNameFromURL(String strURL) {
        return strURL.substring(strURL.lastIndexOf('/') + 1, strURL.length());
    }

    public static boolean isFileNameValid(String fileName) {
        TAG = "isFileNameValid";
        Log.d(MODULE, TAG);

        boolean isValid = true;
        char[] ILLEGAL_CHARACTERS = { '/', '\n', '\r', '\t', '\0', '\f', '`',
                '?', '*', '\\', '<', '>', '|', '\"', ':' };
        try {
            for (int i = 0; i < ILLEGAL_CHARACTERS.length; i++) {
                if (fileName.contains(ILLEGAL_CHARACTERS[i] + "")) {
                    isValid = false;
                }
            }
        } catch (Exception e) {
            Log.e(MODULE, TAG + ", Exception Occurs " + e);
            isValid = false;
        }
        return isValid;
    }

    public static boolean isHtmlHasString(String htmlString) {
        TAG = "isHtmlHasString";
        Log.d(MODULE, TAG);

        boolean isValid = false;

        try {
            if (htmlString != null && htmlString.length() > 0) {
                String html = Html.fromHtml(htmlString).toString();

                if (html != null) {
                    if (html.length() > 0) {
                        isValid = true;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(MODULE, TAG + ", Exception Occurs " + e);
            isValid = false;
        }
        return isValid;
    }

    public static int getLoadMoreCurrentValue(int currentData,
                                              int loadMoreInterval) {
        TAG = "getLoadMoreCurrentValue";
        Log.d(MODULE, TAG);

        int loadIndex = 0;// ConsData.LOAD_MORE;

        try {
            do {
                loadIndex = loadIndex + loadMoreInterval;
            } while (currentData >= loadIndex);

            if (loadIndex > loadMoreInterval) {
                loadIndex = loadIndex - currentData;
                loadIndex = loadIndex + 1;
            }
        } catch (Exception e) {
            Log.e(MODULE, TAG + ", Exception Occurs " + e);
        }

        return loadIndex;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void showNoInternetAlert(Context mContext) {
        // TODO Auto-generated method stub
        TAG = "showNoInternetAlert";
        Log.d(MODULE, TAG);

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage(
                    mContext.getResources().getString(R.string.txt_No_Internet))
                    .setTitle(
                            mContext.getResources()
                                    .getString(R.string.app_name))
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.dismiss();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {
            // TODO: handle exception
            Log.e(MODULE, TAG + ", Exception Occurs " + e);
        }
    }

    public static boolean isPasswordConfirmDataValid(EditText edtPassword,
                                                     EditText edtCPassword, Context mContext) {
        TAG = "isPasswordConfirmDataValid";
        Log.d(MODULE, TAG + " Called.");

        boolean isValid = true;
        try {
            edtPassword.setError(null);
            edtCPassword.setError(null);

            boolean cancel = false;
            View focusView = null;

            if (TextUtils.isEmpty(edtPassword.getText().toString().trim())) {
                edtPassword.setError(mContext.getResources().getString(
                        R.string.error_empty_new_password));
                focusView = edtPassword;
                cancel = true;
            }

            if (TextUtils.isEmpty(edtCPassword.getText().toString().trim())
                    && !cancel) {
                edtCPassword.setError(mContext.getResources().getString(
                        R.string.error_empty_confirm_password));
                focusView = edtCPassword;
                cancel = true;
            }

            if (!edtPassword.getText().toString().trim()
                    .equals(edtCPassword.getText().toString())
                    && !cancel) {
                edtCPassword.setError(mContext.getResources().getString(
                        R.string.error_no_match_confirm_password));
                focusView = edtCPassword;
                cancel = true;
            }

            if (cancel) {
                focusView.requestFocus();
                isValid = false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e(MODULE, TAG + " Exception Occurs : " + e);
        }

        return isValid;
    }

    public static boolean isEmptyDataValid(EditText edtText, Context mContext,
                                           String strMsg) {
        TAG = "isEmptyDataValid";
        Log.d(MODULE, TAG + " Called.");

        boolean isValid = true;
        try {
            edtText.setError(null);

            boolean cancel = false;
            View focusView = null;

            if (TextUtils.isEmpty(edtText.getText().toString().trim())) {
                edtText.setError(strMsg);
                focusView = edtText;
                cancel = true;
            }

            if (cancel) {
                focusView.requestFocus();
                isValid = false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e(MODULE, TAG + " Exception Occurs : " + e);
        }

        return isValid;
    }

//    public static boolean TermsConditionValid(CheckBox checkBox,
//                                              Context mContext, View mBGView) {
//        TAG = "TermsConditionValid";
//        Log.d(MODULE, TAG + " Called.");
//
//        boolean isValid = true;
//        try {
//            boolean cancel = false;
//            View focusView = null;
//
//            if (checkBox.isChecked() == false) {
//                focusView = checkBox;
//                cancel = true;
//            }
//
//            if (cancel) {
//                Animation animation = AnimationUtils.loadAnimation(mContext,
//                        R.anim.zoom_in_out);
//                mBGView.startAnimation(animation);
//                focusView.requestFocus();
//                isValid = false;
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//            Log.e(MODULE, TAG + " Exception Occurs : " + e);
//        }
//
//        return isValid;
//    }

    public static boolean isMobileNoDataValid(EditText edtMNo, Context mContext) {
        TAG = "isMobileNoDataValid";
        Log.d(MODULE, TAG + " Called.");

        boolean isValid = true;
        try {
            edtMNo.setError(null);

            boolean cancel = false;
            View focusView = null;


            if (edtMNo.getText().toString().trim().length() > 0
                    && edtMNo.getText().toString().trim().length() != 11) {
                edtMNo.setError(mContext.getResources().getString(
                        R.string.error_invalid_mobile));
                focusView = edtMNo;
                cancel = true;
            }

            if (cancel) {
                focusView.requestFocus();
                isValid = false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            Log.e(MODULE, TAG + " Exception Occurs : " + e);
        }

        return isValid;
    }
}
