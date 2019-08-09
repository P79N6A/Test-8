package test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class Demo6 {
  public static void main(String[] args) throws IOException {

    int i = 0;
    File file = new File("D://test.txt");
    if (!file.exists()) {
		file.createNewFile();
	}
    FileInputStream fis = new FileInputStream(file);
    // read till the end of the file
    while ((i = fis.read()) != -1) {
      // get file channel // By: W  £÷ W.Y I iba I.c  o  M
      FileChannel fc = fis.getChannel();

      // get channel position
      long pos = fc.position();

      char c = (char) i;

      System.out.println("No of bytes read: " + pos);
      System.out.println("Char read: " + c);
    }
  }
}