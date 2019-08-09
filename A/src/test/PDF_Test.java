package test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.pdfbox.PDFReader;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
public class PDF_Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file = new File("C:\\Users\\zhanchunfei\\Desktop\\1\\附件2：移动转售国际业务资费表201709.pdf");
		PDDocument document=null;
		try {
			document=PDDocument.load(file);
			int pages = document.getNumberOfPages();
			PDFTextStripper ptext = new PDFTextStripper();
			ptext.setSortByPosition(true);
			ptext.setStartPage(1);
			ptext.setEndPage(17);
			String text = ptext.getText(document);
			System.out.println(text);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}