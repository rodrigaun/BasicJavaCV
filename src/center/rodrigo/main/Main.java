package center.rodrigo.main;

import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import org.bytedeco.javacpp.opencv_core.IplImage;
import center.rodrigo.Core.BlobCore;

public class Main {

    public static void main(String[] args) {

        IplImage img = cvLoadImage("images/image.png");
        
        BlobCore bc = new BlobCore(img, 10);
        bc.run();

    }
}
