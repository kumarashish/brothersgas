package payment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.brothersgas.R;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.NumberToWords;
import common.ScreenshotUtils;
import common.WebServiceAcess;
import consumption.ConsumptionPreview;
import model.Connection_Disconnection_Invoice_Preview_Model;
import model.PaymentPreviewModel;
import utils.Utils;


/**
 * Created by ashish.kumar on 19-03-2019.
 */

public class PaymentReceiptPreview extends Activity implements View.OnClickListener{
    @BindView(R.id.back_button)
    Button back_button;
    AppController controller;
    WebServiceAcess webServiceAcess;
   // public static String invoice="RMRC-U1L1900113";
   public static String invoice="";
    NumberToWords numToWords;
    @BindView(R.id.progressBar)
    ProgressBar progress;
    @BindView(R.id.contentView)
    ScrollView contentView;
    @BindView(R.id.mainLayout)
    LinearLayout mainLayout;
    @BindView(R.id. received_from)
    android.widget.TextView received_from;
    @BindView(R.id. receiptno)
    android.widget.TextView receiptno;
    @BindView(R.id.date)
    android.widget.TextView date;
    @BindView(R.id.time)
    android.widget.TextView time;
    @BindView(R.id.address)
    android.widget.TextView address;
    @BindView(R.id.userId)
    android.widget.TextView userId;
    @BindView(R.id.username)
    android.widget.TextView username;
    @BindView(R.id.amount)
    android.widget.TextView  amount;
    @BindView(R.id.amount_in_words)
    android.widget.TextView amount_in_words;
    @BindView(R.id.payment_method)
    android.widget.TextView payment_method;
    @BindView(R.id. chequeView)
    LinearLayout chequeView;
    @BindView(R.id.chequenumber)
    android.widget.TextView chequenumber;
    @BindView(R.id. chequedate)
    android.widget.TextView chequedate;
    @BindView(R.id.bank)
    android.widget.TextView bank;
    @BindView(R.id.invoice_numbers)
    android.widget.TextView invoice_numbers;
    @BindView(R.id. signature)
    ImageView signature;
    public static String imagePath="";
    @BindView(R.id.footer)
    LinearLayout footer;
    @BindView(R.id.progressBar2)
    ProgressBar progressbar2;
    @BindView(R.id.print_email)
    Button print_email;
    int sendAttempt=0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_receipt_preview);
        ButterKnife.bind(this);
        webServiceAcess = new WebServiceAcess();
        controller=(AppController)getApplicationContext();
        numToWords=new NumberToWords();
        back_button.setOnClickListener(this);
        print_email.setOnClickListener(this);
        if(Utils.isNetworkAvailable(PaymentReceiptPreview.this)) {
            progress.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
            new GetInvoiceDetails().execute();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id. back_button:
                finish();
                break;

            case R.id.print_email:
                progressbar2.setVisibility(View.VISIBLE);
                footer.setVisibility(View.GONE);
                new EmailInvoice().execute();
                break;
        }
    }


    /*-------------------------------------------------------------------block-------------------------------------------------------*/
    public class GetInvoiceDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String result = webServiceAcess.runRequest(Common.runAction,Common.PaymentInoviceDetails, new String[]{invoice});
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e("value", "onPostExecute: ", null);
            if (s.length() > 0) {
                try {
                    PaymentPreviewModel model=new   PaymentPreviewModel(s);
                    if(model.getStatusValue()==2)
                    {
                        setValues(model);
                    }else{
                        Utils.showAlertNormal(PaymentReceiptPreview.this,model.getMessageValue());
                        progress.setVisibility(View.GONE);
                        footer.setVisibility(View.GONE);

                    }
                } catch (Exception ex) {
                    ex.fillInStackTrace();
                    Utils.showAlertNormal(PaymentReceiptPreview.this,"format changed");
                    progress.setVisibility(View.GONE);
                    footer.setVisibility(View.GONE);

                }


            } else {
                Utils.showAlertNormal(PaymentReceiptPreview.this,"Data not available.");
                progress.setVisibility(View.GONE);
                footer.setVisibility(View.GONE);
            }

        }
    }

    public void setValues(PaymentPreviewModel model)
    {

        received_from.setText(model.getCustomerNameValue());
        receiptno.setText(model.getPaymentNumberValue());
        date.setText(Utils.getNewDate(model.getDateValue()));
        time.setText(model.getTimeValue());
        address.setText(model.getCustomerAddressValue());
        userId.setText(model.getUseridValue());
        username.setText(model.getUSERNameValue());
        amount.setText("AED "+model.getAmountValue());
        amount_in_words.setText("AED " + numToWords.convertNumberToWords((int) Math.round(Double.parseDouble(model.getAmountValue()))) + " Only /-");
        payment_method.setText("By " + model.getPaymentTypeValue());
        if (model.getPaymentTypeValue().equalsIgnoreCase("cheque")) {
            chequeView.setVisibility(View.VISIBLE);
            chequenumber.setText(model.getChequeNumberValue());
            chequedate.setText(Utils.getNewDate(model.getChequeDateValue()));
            bank.setText(model.getBankValue());
        } else {
            chequeView.setVisibility(View.GONE);
        }

        invoice_numbers.setText(model.getInvoiceNumbrsValue());
//        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//        signature.setImageBitmap(bitmap);
        progress.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);



    }
    /*-------------------------------------------------------------------block-------------------------------------------------------*/
    public class EmailInvoice extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {


            String result = webServiceAcess.runRequest(Common.runAction,Common.Print_Email, new String[]{receiptno.getText().toString(),"5"});
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
                    JSONArray Fld = item.getJSONArray("FLD");
                    JSONObject messageJsonObject=Fld.getJSONObject(1);
                    JSONObject status=Fld.getJSONObject(0);
                    String message =messageJsonObject.isNull("content")?"No Message From API": messageJsonObject.getString("content");
                    int statusValue=status.isNull("content")?1: status.getInt("content");
                    if(statusValue==2)
                    { sendAttempt=sendAttempt+1;
                        if(sendAttempt==1)
                        {
                            print_email.setText("Resend");
                        }else {
                            print_email.setVisibility(View.GONE);
                        }
                        Utils.showAlertNormal(PaymentReceiptPreview.this,message);
                    }else{
                        Utils.showAlertNormal(PaymentReceiptPreview.this,message);
                    }


                } catch (Exception ex) {
                    ex.fillInStackTrace();
                }
                progressbar2.setVisibility(View.GONE);
                footer.setVisibility(View.VISIBLE);
            } else {
                progressbar2.setVisibility(View.GONE);
                footer.setVisibility(View.VISIBLE);
            }

        }
    }

    public void generatePdf() throws IOException, DocumentException {
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/BrothersGas/";
        File f=new File(directory_path);
        if(!f.exists())
        {
            f.mkdir();
        }
        String targetPdf = directory_path+"consumption.pdf";
        Document document = new Document();
// Location to save
        PdfWriter.getInstance(document, new FileOutputStream(targetPdf ));


// Open to write
        document.open();
        document.setPageSize(PageSize.A4);
        document.addCreationDate();
        document.addAuthor("Ashish");
        document.addCreator("Brothers Gas");
        /***
         * Variables for further use....
         */
        BaseColor mColorAccent = new BaseColor(0, 153, 204, 255);
        float mHeadingFontSize = 20.0f;
        float mValueFontSize = 26.0f;
/**
 * How to USE FONT....
 */
        // BaseFont urName = BaseFont.createFont("assets/fontnew.otf", "UTF-8", BaseFont.EMBEDDED);
        // LINE SEPARATOR
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
        // Title Order Details...
// Adding Title....
        //Font mOrderDetailsTitleFont = new Font(urName, 36.0f, Font.NORMAL, BaseColor.BLACK);
// Creating Chunk
        Chunk mOrderDetailsTitleChunk = new Chunk("Consumption Invoice");
// Creating Paragraph to add...
        Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);
// Setting Alignment for Heading
        mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_CENTER);
// Finally Adding that Chunk
        document.add(mOrderDetailsTitleParagraph);
        document.add(new Paragraph(""));
        document.add(new Chunk(lineSeparator));
        document.add(new Paragraph(""));
        document.add(new Chunk(lineSeparator));
        document.add(new Paragraph(" Page End"));
        document.add(new Chunk(lineSeparator));

        document.close();
    }




    }

