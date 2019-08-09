package test;

import java.io.File;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class Dom4jDemo{
	public static void main(String[] args) {
		File file = new File("C:\\Users\\zhanchunfei\\Desktop\\³É¿¨Êý¾Ý.xml");
		SAXReader saxreader = new SAXReader();
		try {
			Document read = saxreader.read(file);
			Element rootElement = read.getRootElement();
			Element elem;
			Iterator elementIterator = rootElement.elementIterator("Head");
			elem=(Element) elementIterator.next();
			System.out.println(elem.elementText("FileVersion"));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
