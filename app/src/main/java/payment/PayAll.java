package payment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.brothersgas.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;

import common.WebServiceAcess;
import model.BankModel;
import model.PaymentReceiptModel;
import utils.Utils;

public class PayAll  extends Activity implements View.OnClickListener {
    AppController controller;
    WebServiceAcess webServiceAcess;


    @BindView(R.id.progressBar2)
    ProgressBar progressBar;


    @BindView(R.id.back_button)
    Button back;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.mode)
    RadioGroup paymentMode;
    @BindView(R.id.cash)
    RadioButton cash;
    @BindView(R.id.cheque)
    RadioButton cheque;
    @BindView(R.id.cheaqueDateView)
    LinearLayout cheaqueDateView;
    @BindView(R.id.cheaqueNumberView)
    LinearLayout cheaqueNumberView;
    @BindView(R.id.bankView)
    LinearLayout bankView;
    @BindView(R.id.cheaqueImageView)
    LinearLayout cheaqueImageView;
    @BindView(R.id.amount_value)

    EditText amountValue;
    @BindView(R.id.amount_unit)
    EditText amountUnit;
    @BindView(R.id.invoice_number)
    android.widget.TextView invoice_numberValue;
    @BindView(R.id.cheaquenumber)
    EditText cheaqueNumber;
    @BindView(R.id.date)
    Button chequeDate;
    @BindView(R.id.bank)
    Spinner bank;
    @BindView(R.id.progressBar)
    ProgressBar progressBar1;
    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;
    ArrayList<BankModel> bankmodelList=new ArrayList<>();
    ArrayList<String>bankNameList=new ArrayList<>();
    private Calendar calendar;

    private int year, month, day;

    public static String customerNumberValue = "";
    public static String amount = "";
    public static String unit = "";
    boolean paymentModeCheque = true;
    @BindView(R.id.customer_number)
    android.widget.TextView customerNumber;
    String imagePath="";
    @BindView(R.id.cheaqueImage)
    Button cheaqueImage;
    boolean isImageCaptured=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_screen);
        controller = (AppController) getApplicationContext();
        webServiceAcess = new WebServiceAcess();
        ButterKnife.bind(this);
        submit.setOnClickListener(this);
        back.setOnClickListener(this);
        cheaqueImage.setOnClickListener(this);
        paymentMode.check(cheque.getId());
        customerNumber.setText("Customer : ");
        invoice_numberValue.setText(customerNumberValue);
        amountValue.setText(amount);
        amountUnit.setText("AED");
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        chequeDate.setOnClickListener(this);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        paymentMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == cheque.getId()) {
                    cheaqueNumberView.setVisibility(View.VISIBLE);
                    cheaqueDateView.setVisibility(View.VISIBLE);
                    bankView.setVisibility(View.VISIBLE);
                    cheaqueImageView.setVisibility(View.VISIBLE);
                    paymentModeCheque = true;
                } else {
                    cheaqueNumberView.setVisibility(View.GONE);
                    cheaqueDateView.setVisibility(View.GONE);
                    bankView.setVisibility(View.GONE);
                    cheaqueImageView.setVisibility(View.GONE);
                    paymentModeCheque = false;
                }
            }
        });

     if(Utils.isNetworkAvailable(PayAll.this))
     {
            new FetchBankList().execute();
        }


        if(checkPermissionForReadExtertalStorage()==false)
        {
            try {
                requestPermissionForReadExtertalStorage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(checkPermissionForCamera()==false)
        {
            try {
                requestPermissionForCamera();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
    public void requestPermissionForReadExtertalStorage() throws Exception {
        try {
            ActivityCompat.requestPermissions(PayAll.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    22);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public void requestPermissionForCamera() throws Exception {
        try {
            ActivityCompat.requestPermissions(PayAll.this, new String[]{Manifest.permission.CAMERA},
                    21);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }
    public boolean checkPermissionForCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = checkSelfPermission(Manifest.permission.CAMERA);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void showAlert(String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(PayAll.this);
        builder1.setMessage(message);
        builder1.setCancelable(true);
        builder1.setNeutralButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };
    private void showDate(int year, int month, int day) {
        String dayValue=Integer.toString(day);
        String monthValue=Integer.toString(month);
        if(month<10)
        {
            monthValue="0"+month;
        }
        if(day<10)
        {
            dayValue="0"+day;
        }
        chequeDate.setText(new StringBuilder().append(dayValue).append("/")
                .append(monthValue).append("/").append(year));



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.date:
                setDate(v);
                break;
            case R.id.cheaqueImage:
                Utils.selectImageDialog(PayAll.this,"Cheque Image");
                break;
            case R.id.submit:
                if (paymentMode.getCheckedRadioButtonId() == cheque.getId()) {
                    if ((cheaqueNumber.getText().length() > 0) && (chequeDate.getText().length() > 0) && (amountValue.getText().length() > 0)) {

                            progressBar.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.GONE);
                            new CreatePayment().execute();


                    } else {
                        if (cheaqueNumber.getText().length() == 0) {
                            showAlert("Please enter cheque number");
                        } else if (chequeDate.getText().length() == 0) {
                            showAlert("Please enter cheque date");
                        } else if(amountValue.getText().length()==0)
                        {
                            showAlert("Please enter amount");
                        }
                    }
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.GONE);
                    new CreatePayment().execute();

                }
                break;
        }
    }

    /*-------------------------------------------------------------------block-------------------------------------------------------*/
    public class CreatePayment extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String cheaueNumberString = "";
            String mode = "1";
            String bankDetails="";
            String cheaqueIssueDate="";
            String imageBase64="";
            if (paymentMode.getCheckedRadioButtonId() == cheque.getId()) {
                cheaueNumberString = cheaqueNumber.getText().toString();
                bankDetails= bank.getSelectedItem().toString();
                cheaqueIssueDate=   Utils.getFormatted(chequeDate.getText().toString());
                mode = "2";
                imageBase64=Utils.getBase64(imagePath);
            }
            String result = webServiceAcess.runRequest(Common.runAction, Common.PayAll, new String[]{customerNumberValue, cheaueNumberString, amountValue.getText().toString(), mode,bankDetails,imageBase64,cheaqueIssueDate});
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: ", null);
            if (s.length() > 0) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject result = jsonObject.getJSONObject("RESULT");
                    JSONArray jsonArray = result.getJSONArray("GRP");
                    JSONObject item = jsonArray.getJSONObject(1);
                    JSONArray fld = item.getJSONArray("FLD");
                    PaymentReceiptModel model = new PaymentReceiptModel(fld);
                    if (model.getStatus().equalsIgnoreCase("2")) {
                        PaymentReceipt_Print_Email.model = model;
                        PaymentReceipt_Print_Email.isPaymentTakenByCheaque = paymentModeCheque;
                        PaymentReceipt_Print_Email.number=customerNumberValue;
                        PaymentReceipt_Print_Email.isCalledFromPayAll=true;

                        Utils.showAlertNavigateToPrintEmail(PayAll.this, model.getMessage(), PaymentReceipt_Print_Email.class);
                    } else {
                        Utils.showAlertNormal(PayAll.this, model.getMessage());
                        submit.setVisibility(View.VISIBLE);

                    }
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            } else {
                Utils.showAlertNormal(PayAll.this,Common.message);
                submit.setVisibility(View.VISIBLE);

            }
            progressBar.setVisibility(View.GONE);

        }
    }

    /*-------------------------------------------------------------------getData-------------------------------------------------------*/
    public class FetchBankList extends AsyncTask<String, Void, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = webServiceAcess.runRequest(Common.runAction, Common.BankList, new String[]{"1"});
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: ", null);
            if (s.length() > 0) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject result = jsonObject.getJSONObject("RESULT");
                    JSONObject tab = result.getJSONObject("TAB");
                    JSONArray jsonArray = tab.getJSONArray("LIN");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);
                        BankModel model = new BankModel(item.getJSONArray("FLD"));
                        bankmodelList.add(model);
                        bankNameList.add(model.getBankName());
                    }

                    if (bankNameList.size() > 0) {
                        bank.setAdapter(new ArrayAdapter<String>(PayAll.this, android.R.layout.simple_spinner_item, bankNameList));
                    }


                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }

            } else {
                Utils.showAlertNormal(PayAll.this,Common.message);

            }
            if (pd != null) {
                pd.cancel();
            }

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                imagePath=Common.imageUri.getPath();
                Common.tempPath = Common.imageUri.getPath();
                isImageCaptured = true;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap myBitmap = BitmapFactory.decodeFile(Common.tempPath,options);
                BitmapDrawable bdrawable = new BitmapDrawable(getResources(),myBitmap);
                cheaqueImage.setBackground(bdrawable);
                cheaqueImage.setText("");
            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(this, " Picture was not taken ", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == Common.SELECT_FILE) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                Common.tempPath = c.getString(columnIndex);
                imagePath = c.getString(columnIndex);
                c.close();
                isImageCaptured = true;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap myBitmap = BitmapFactory.decodeFile(Common.tempPath,options);
                BitmapDrawable bdrawable = new BitmapDrawable(getResources(),myBitmap);
                cheaqueImage.setBackground(bdrawable);
                cheaqueImage.setText("");
                // Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath, options));

            } else {
                Toast.makeText(this, " This Image cannot be stored .please try with some other Image. ", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onDestroy() {
        Common.tempPath="";
        super.onDestroy();
    }
}