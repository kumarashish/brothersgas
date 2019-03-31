package common;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.ImageView;
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
import java.io.InputStream;
import java.util.ArrayList;


/**
 * Created by ashish.kumar on 20-03-2019.
 */

   public class GeneratePdfFormat  extends Activity {
       ImageView image;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        //createPdf("testing");
        try {
            image=(ImageView) findViewById(R.id.image);
        //  String path=  generatePdf();
            String directory_path = Environment.getExternalStorageDirectory().getPath() + "/BrothersGas/";
            File f=new File(directory_path);
            if(!f.exists())
            {
                f.mkdir();
            }
            String targetPdf = directory_path+"temp.pdf";
            File temp=new File(targetPdf);
            if(new File(targetPdf).exists())
            {
                temp.delete();
            }
            CopyRAWtoSDCard(R.raw.invoice,targetPdf);
          ArrayList<Bitmap> getImages=pdfToBitmap(new File(targetPdf));
            image.setImageBitmap(getImages.get(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     public String generatePdf() throws IOException, DocumentException {
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/BrothersGas/";
    File f=new File(directory_path);
        if(!f.exists())
        {
            f.mkdir();
        }
         String targetPdf = directory_path+"temp.pdf";
        File temp=new File(targetPdf);
        if(new File(targetPdf).exists())
        {
            temp.delete();
        }
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
         return targetPdf;
}


    public void CopyRAWtoSDCard(int id, String path) throws IOException {
        InputStream in = getResources().openRawResource(id);
        FileOutputStream out = new FileOutputStream(path);
        byte[] buff = new byte[1024];
        int read = 0;
        try {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        } finally {
            in.close();
            out.close();
        }
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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ArrayList<Bitmap> pdfToBitmap(File pdfFile) {
        ArrayList<Bitmap> bitmaps = new ArrayList<>();

        try {
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY));

            Bitmap bitmap;
            final int pageCount = renderer.getPageCount();
            for (int i = 0; i < pageCount; i++) {
                PdfRenderer.Page page = renderer.openPage(i);

                int width = getResources().getDisplayMetrics().densityDpi / 72 * page.getWidth();
                int height = getResources().getDisplayMetrics().densityDpi / 72 * page.getHeight();
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                bitmaps.add(bitmap);

                // close the page
                page.close();

            }

            // close the renderer
            renderer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return bitmaps;

    }
}
