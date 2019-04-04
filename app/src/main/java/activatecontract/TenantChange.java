package activatecontract;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.brothersgas.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import adapter.ContractListAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.WebServiceAcess;
import contracts.Search;
import interfaces.ListItemClickListner;
import invoices.Connection_Disconnection_Invoice_details;
import model.ContractDetails;
import model.ContractModel;
import payment.PaymentReceipt;
import utils.Utils;

public class TenantChange extends Activity implements View.OnClickListener {
    AppController controller;
    WebServiceAcess webServiceAcess;
    @BindView(R.id.contract_num)
    EditText contract_number;
            @BindView(R.id.current_reading)
            EditText current_reading;
    @BindView(R.id.em_id1)
    EditText em_id1;
    @BindView(R.id.em_id2)
    EditText em_id2;
    @BindView(R.id.em_id3)
    EditText em_id3;
    @BindView(R.id.em_id4)
    EditText em_id4;
            @BindView(R.id.expiry_date)
            Button expiry_date;
            @BindView(R.id.address)
            EditText address;
            @BindView(R.id.emailId)
            EditText emailId;
            @BindView(R.id.contact_num)
            EditText contact_number;
    @BindView(R.id.progressBar2)
    ProgressBar progressBar;
    @BindView(R.id.footer)
    LinearLayout footer;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.back_button)
    Button back;
    @BindView(R.id.c_name)
    EditText customer_name;
    @BindView(R.id.emirates_id_front)
    Button em_id_front;
    @BindView(R.id.emirates_id_back)
    Button em_id_back;
    private DatePicker datePicker;
    private Calendar calendar;

    private int year, month, day;
public static ContractModel model=null;
public static String currentMeterReading="";

int requestedType;
int frontImage=1,backImage=2;
String fronImagePath="";
String backImagePath="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teenant_change);
        controller = (AppController) getApplicationContext();
        webServiceAcess = new WebServiceAcess();
        ButterKnife.bind(this);
        submit.setOnClickListener(this);
        back.setOnClickListener(this);
        expiry_date.setOnClickListener(this);
        contract_number.setText(model.getContract_Meternumber());
        customer_name.setText(model.getCustomername());
        current_reading.setText(currentMeterReading );
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        em_id_front.setOnClickListener(this);
        em_id_back.setOnClickListener(this);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        em_id1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==3) {
                    em_id2.requestFocus();
                }

            }
        });
        em_id2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==4) {
                    em_id3.requestFocus();
                }

            }
        });
        em_id3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length()==7) {
                    em_id4.requestFocus();
                }
            }
        });


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
            ActivityCompat.requestPermissions(TenantChange.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    22);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public void requestPermissionForCamera() throws Exception {
        try {
            ActivityCompat.requestPermissions(TenantChange.this, new String[]{Manifest.permission.CAMERA},
                    21);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean checkPermissionForCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = checkSelfPermission(Manifest.permission.CAMERA);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public boolean checkPermissionForReadExtertalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            DatePickerDialog dialog= new DatePickerDialog(this,
                    myDateListener, year, month, day);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis());
            return dialog;
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
       expiry_date.setText(new StringBuilder().append(dayValue).append("/")
                .append(monthValue).append("/").append(year));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
            case R.id.emirates_id_front:
                requestedType=frontImage;
                Utils.selectImageDialog(TenantChange.this,"EmirateId Front");
                break;
            case R.id.emirates_id_back:
                requestedType=backImage;
                Utils.selectImageDialog(TenantChange.this,"EmirateId Back");
                break;
            case R.id.submit:
if(isFieldsValidated())
{progressBar.setVisibility(View.VISIBLE);
footer.setVisibility(View.GONE);
    new Update().execute();
}

                break;
            case R.id.expiry_date:
                setDate(v);
                break;

        }
    }
public String getEmId()
{
    return em_id1.getText().toString()+"-"+em_id2.getText().toString()+"-"+em_id3.getText().toString()+"-"+em_id4.getText().toString();
}
    public boolean isEmidValidated() {
        if ((em_id1.getText().length() == 3) && (em_id2.getText().length() == 4) && (em_id3.getText().length() == 7) && (em_id4.getText().length() == 1)) {
            return true;
        } else {
            em_id1.requestFocus();
            Toast.makeText(TenantChange.this, "Please enter emirates Id", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    public boolean isFieldsValidated() {
        if ((current_reading.getText().length() > 0)  && (expiry_date.getText().length() > 0)&&(isEmidValidated()) && (address.getText().length() > 0) && (contact_number.getText().length() > 0) && (emailId.getText().length() > 0)) {
        if(Utils.isValidEmailId(emailId.getText().toString())) {
            return true;
        }else{
            Toast.makeText(TenantChange.this,"Please enter valid email id",Toast.LENGTH_SHORT).show();
        }
        }else{
            if(current_reading.getText().length()==0)
            {
                Toast.makeText(TenantChange.this,"Please enter current reading",Toast.LENGTH_SHORT).show();
            }else if(expiry_date.getText().length()==0)
            {
                Toast.makeText(TenantChange.this,"Please enter expiry date",Toast.LENGTH_SHORT).show();
            }else if(address.getText().length()==0)
            {
                Toast.makeText(TenantChange.this,"Please enter address",Toast.LENGTH_SHORT).show();
            }else if(contact_number.getText().length()==0)
            {
                Toast.makeText(TenantChange.this,"Please enter contact number",Toast.LENGTH_SHORT).show();
            }else if(emailId.getText().length()==0)
            {
                Toast.makeText(TenantChange.this,"Please enter email id",Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }


    /*-------------------------------------------------------------------block-------------------------------------------------------*/
    public class Update extends AsyncTask<String, Void, String> {

        String frontImageValue="";
        String backImageValue="";


        @Override
        protected String doInBackground(String... strings) {
            if(fronImagePath.length()>0)
            {
                frontImageValue=Utils.getBase64(fronImagePath);
            }

            if(backImagePath.length()>0)
            {
                backImageValue=Utils.getBase64(backImagePath);
            }
            String result = webServiceAcess.runRequest(Common.runAction, Common.Tennant_Change, new String[]{model.getContract_Meternumber(), current_reading.getText().toString(), getEmId(),Utils.getFormatted(expiry_date.getText().toString()),address.getText().toString(),contact_number.getText().toString(),emailId.getText().toString(),frontImageValue,backImageValue});
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: " + s, null);
            if (s.length() > 0) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject result = jsonObject.getJSONObject("RESULT");
                    JSONArray jsonArray = result.getJSONArray("GRP");
                    JSONObject item = jsonArray.getJSONObject(1);
                    JSONArray Fld = item.getJSONArray("FLD");
                    JSONObject contractDetails=Fld.getJSONObject(0);
                    JSONObject messageObject = Fld.getJSONObject(1);
                    String contractNumber = contractDetails.isNull("content") ? "Message not available" : contractDetails.getString("content");
                    String message = messageObject.isNull("content") ? "Message not available" : messageObject.getString("content");
                    if (message.contains("New")) {
                        contract_number.setText(contractNumber);
                        Connection_Disconnection_Invoice_details.isCalledFromTenanatChange=true;
                        Utils.showAlertNavigateToInvoices(TenantChange.this, message,Connection_Disconnection_Invoice_details.class,contractNumber);

                        submit.setVisibility(View.GONE);

                    } else {
                        Utils.showAlertNormal(TenantChange.this, message);
                        submit.setVisibility(View.VISIBLE);
                    }
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
            } else {
                Utils.showAlertNormal(TenantChange.this, Common.message);
            }
            progressBar.setVisibility(View.GONE);
            footer.setVisibility(View.VISIBLE);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                Common.tempPath = Common.imageUri.getPath();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap myBitmap = BitmapFactory.decodeFile(Common.tempPath,options);
                BitmapDrawable bdrawable = new BitmapDrawable(getResources(),myBitmap);

                switch (requestedType)
                {
                    case 1:
                        fronImagePath=Common.imageUri.getPath();
                        em_id_front.setBackground(bdrawable);
                        em_id_front.setText("");
                        break;
                    case 2:
                        backImagePath=Common.imageUri.getPath();
                        em_id_back.setBackground(bdrawable);
                        em_id_back.setText("");
                        break;
                }

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
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;
                Bitmap myBitmap = BitmapFactory.decodeFile(Common.tempPath,options);
                BitmapDrawable bdrawable = new BitmapDrawable(getResources(),myBitmap);
                switch (requestedType)
                {
                    case 1:
                        fronImagePath=c.getString(columnIndex);
                        em_id_front.setBackground(bdrawable);
                        em_id_front.setText("");
                        break;
                    case 2:
                        backImagePath=c.getString(columnIndex);
                        em_id_back.setBackground(bdrawable);
                        em_id_back.setText("");
                        break;
                }
                c.close();
                // Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath, options));

            } else {
                Toast.makeText(this, " This Image cannot be stored .please try with some other Image. ", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onDestroy() {
        currentMeterReading="";
        super.onDestroy();
    }
}