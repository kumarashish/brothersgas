package common;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.brothersgas.R;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;



/**
 * Created by ashish.kumar on 20-03-2019.
 */

   public class GeneratePdfFormat  extends Activity {
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consumption);
        //createPdf("testing");
        try {
            generatePdf();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
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
         document.add(new Paragraph("Item 1 : Brothers Gas"));

         document.add(new Paragraph("Item 2 : Brothers Gas"));

         document.add(new Paragraph("Item 3 : Brothers Gas"));

         document.add(new Paragraph("Item 4: Brothers Gas"));

         document.add(new Paragraph("Item 5 : Brothers Gas"));

         document.add(new Paragraph("Item 6 : Brothers Gas"));


         document.add(new Paragraph("Item 1 : Brothers Gas"));

         document.add(new Paragraph("Item 2 : Brothers Gas"));

         document.add(new Paragraph("Item 3 : Brothers Gas"));

         document.add(new Paragraph("Item 4: Brothers Gas"));

         document.add(new Paragraph("Item 5 : Brothers Gas"));

         document.add(new Paragraph("Item 6 : Brothers Gas"));
         document.add(new Chunk(lineSeparator));
         document.add(new Paragraph(" Page End"));
         document.add(new Chunk(lineSeparator));

         document.close();
}


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void createPdf(String sometext){
        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawCircle(50, 50, 30, paint);
        paint.setColor(Color.BLACK);
        canvas.drawText(sometext, 80, 50, paint);
        //canvas.drawt
        // finish the page
        document.finishPage(page);
// draw text on the graphics object of the page
        // Create Page 2
        pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 2).create();
        page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawCircle(100, 100, 100, paint);
        document.finishPage(page);
        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"test-2.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }
}
