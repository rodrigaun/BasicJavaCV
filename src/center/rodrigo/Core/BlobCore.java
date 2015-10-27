package center.rodrigo.Core;

import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_core.cvPoint;
import static org.bytedeco.javacpp.opencv_core.cvScalar;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.CV_THRESH_BINARY;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.cvRectangle;
import static org.bytedeco.javacpp.opencv_imgproc.cvThreshold;

import org.bytedeco.javacpp.opencv_core.CvPoint;
import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacv.Blobs;

import static org.bytedeco.javacpp.opencv_imgcodecs.*;

public class BlobCore {

    private IplImage imgOri;
    private IplImage imgBW;
    private IplImage imgGray;
    private IplImage imgSave;
    private int areaMin;

    public BlobCore(IplImage img, int areaMin) {
        this.imgOri = img;
        this.areaMin = areaMin;
    }

    public void run() {

        /* Transforma a imagen em escala de cinza */
        imgGray = toGrayImage(imgOri);

        /* Filtra range de cinza e transforma em Binary image */
        imgBW = filterGrayImage(imgGray);

        /* Encontra e identifica Figuras */
        imgSave = findBlobs(imgBW, imgOri);

        cvSaveImage("images/SAVE.png", imgSave);
    }

    public IplImage toGrayImage(IplImage img) {

        IplImage temp = cvCreateImage(cvGetSize(img), IPL_DEPTH_8U, 1);
        cvCvtColor(img, temp, CV_BGR2GRAY);
        cvSaveImage("images/imgGray.png", temp);
        return temp;
    }

    public IplImage filterGrayImage(IplImage img) {

        IplImage temp = cvCreateImage(cvGetSize(img), IPL_DEPTH_8U, 1);
        cvThreshold(img, temp, 250, 255, CV_THRESH_BINARY);
        cvSaveImage("images/imgBW.jpg", temp);
        return temp;
    }

    public IplImage findBlobs(IplImage img, IplImage ori) {
        Blobs Regions = new Blobs();
        Regions.BlobAnalysis(img,   // image
                -1, -1,             // ROI start col, row
                -1, -1,             // ROI cols, rows
                1,                  // border (0 = black; 1 = white)
                areaMin);           // area minima

        for (int i = 1; i <= Blobs.MaxLabel; i++) {
            double[] Region = Blobs.RegionData[i];
            int Parent = (int) Region[Blobs.BLOBPARENT];
            int Color = (int) Region[Blobs.BLOBCOLOR];
            int MinX = (int) Region[Blobs.BLOBMINX];
            int MaxX = (int) Region[Blobs.BLOBMAXX];
            int MinY = (int) Region[Blobs.BLOBMINY];
            int MaxY = (int) Region[Blobs.BLOBMAXY];
            drawBlobRectangle(ori, MinX, MinY, MaxX, MaxY, 1);
        }
        Regions.PrintRegionData(); // print coordenadas
        return ori;
    }

    public void drawBlobRectangle(IplImage image, int xMin, int yMin, int xMax, int yMax, int Thick) {

        CvPoint pt1 = cvPoint(xMin, yMin);
        CvPoint pt2 = cvPoint(xMax, yMax);
        CvScalar color = cvScalar(255, 0, 0, 0); // blue [green] [red]
        cvRectangle(image, pt1, pt2, color, Thick, 4, 0);
    }
}
