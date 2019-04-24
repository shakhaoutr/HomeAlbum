/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package homealbum;

import model.CollageImagePicture;
import model.CollageImageText;
import model.GreyscaleMethod;
import model.Settings;
import view.gui.AlertWindow;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;



/**
 * offers methods to manipulate pictures and save them
 * @author Shakhaout Rahman
 */
public class PictureManipulator {
    
    /**
     * returns the image gives as a byte array
     * 
     * @param path  the path to file
     * @return      the byte array
     */
    public static byte[] imageToByteArray(String path){
        try {
            File img = new File(path);                              // open the file
            FileInputStream fis = new FileInputStream(img);         // opens input stram to file
            ByteArrayOutputStream bos = new ByteArrayOutputStream();//byte array writer
            byte[] buf = new byte[1024];
            for(int readNum; (readNum = fis.read(buf)) != -1; ){    //reads b.lenght num of byte from input stream and
                bos.write(buf, 0, readNum);                         // write it to the output stream
            }
            byte[] image = bos.toByteArray();                       // saves the bytes to the array
            
            bos.close();
            fis.close();
            img.delete();
            
            return image;           
        } catch (FileNotFoundException ex) {
            showError(ex, "File to convert to byte array not found");
            return null;
        } catch (IOException ex) {
            showError(ex, "IOException during byte array conversion");
            return null;
        }
    }
    
    /**
     * converts the given FX Image to BufferedImage
     * 
     * @param img   the image to convert
     * @return      the converted image
     */
    public static BufferedImage convertImageToBuffImage(Image img){
        BufferedImage image = SwingFXUtils.fromFXImage(img, null);  // buffered image with alpha channel problems

        BufferedImage bfImage = new BufferedImage(      // new blank buffered image with corrected alpha channel 
            image.getWidth(), 
            image.getHeight(), 
            BufferedImage.OPAQUE);                      // correct alpha channel

        Graphics2D graphics = bfImage.createGraphics(); // graphics for the blank image

        graphics.drawImage(image, 0, 0, null);          // redrawing of the image
        return bfImage;
    }
    
    /**
     * saves image to file
     * 
     * @param filepath  the name file in which to save
     * @param img       the image to save
     * @param format    the format of the image
     */
    public static void saveImageToFile(String filepath, Image img, String format){
        BufferedImage bfImage = convertImageToBuffImage(img);   // convert the image
        saveImageToFile(filepath, bfImage, format);             // saves the image
    }
    
    /**
     * saves image to file
     * 
     * @param filepath  the name file in which to save
     * @param img       the image to save
     * @param format    the format of the image
     */
    public static void saveImageToFile(String filepath, BufferedImage img, String format){
        try {
            File imgFile = new File(filepath);  // opens the file
            ImageIO.write(img, format, imgFile);// saves the file
        } catch (IOException ex) {
            showError(ex, "IO Error during image save");
        }
    }
    
    /**
     * creates a preview of the given image and saves it into the preview folder
     * 
     * @param filename  file in which to save
     * @param fImg      image to create the preview of
     * @param format    the format of the image
     */
    public static void savePreview(String filename, File fImg, String format){
        try {
            Settings s = Settings.getSettings();                    
            File imgFile = new File(s.BASE_DIR_PREVIEW+filename);           // the destination file
            BufferedImage img = ImageIO.read(fImg);                         // read the FX image as BufferedImage
            BufferedImage preview = PictureManipulator.previewImage(img);   // creates preview of BuffImage 
            ImageIO.write(preview, format, imgFile);                        // saves the BuffImage preview
        } catch (IOException ex) {
            showError(ex, "IO Error during image preview save");
        }
    }
    
    /**
     * creates a preview of the given image and saves it into the preview folder
     * 
     * @param filename  file in which to save
     * @param pImg      the preview image
     * @param format    the format of the image
     */
    public static void savePreview(String filename, Image pImg, String format){
        try {
            Settings s = Settings.getSettings();
            File imgFile = new File(s.BASE_DIR_PREVIEW+filename);
            BufferedImage img = convertImageToBuffImage(pImg);
            BufferedImage preview = PictureManipulator.previewImage(img);
            ImageIO.write(preview, format, imgFile);
        } catch (IOException ex) {
            showError(ex, "IO Error during image preview save");
        }
    }
    
    /**
     * saves image to file
     * @param filepath  the name file in which to save
     * @param fImg      the image to save 
     * @param format    the format of the image
     */
    public static void saveImageToFile(String filepath, File fImg, String format){
        try {
            File imgFile = new File(filepath);
            BufferedImage img = ImageIO.read(fImg);
            ImageIO.write(img, format, imgFile);
        } catch (IOException ex) {
            showError(ex, "IO Error during image save");
        }
    }
    
    /**
     * resize the image given its path
     * 
     * @param path      the path of the image file
     * @param width     the new width
     * @param height    the new height
     * @return          the resized image
     */
    public static BufferedImage resizeImage(String path, int width, int height){
        try {
            BufferedImage orImg = ImageIO.read(new File(path)); // read the image
            
            return resizeImage(orImg, width, height);           // return the resize image
        } catch (IOException ex) {
            showError(ex, "IO Error during image resize");
            return null;
        }
    }
    
    /**
     * resize the given image
     * @param img       the image to resize
     * @param width     the new width
     * @param height    the new height
     * @return          the resized image
     */
    public static BufferedImage resizeImage(BufferedImage img, int width, int height){
        BufferedImage orImg = img;                                              // original image
        BufferedImage rzImg= new BufferedImage(width, height, orImg.getType()); // creates a new blank image with the resized sizes
        
        Graphics2D g2 = rzImg.createGraphics();         // graphics of the original image
        g2.drawImage(orImg, 0, 0, width, height, null); // redraws the origianl image with the resized sizes
        g2.dispose();
        
        return rzImg;
    }
    
    /**
     * creates the preview of the given image
     * (resized with 150 px height and proportional width)
     * 
     * @param img   the image create the preview of
     * @return      the resized image
     */
    public static BufferedImage previewImage(BufferedImage img){
        int height = img.getHeight();
        int width = img.getWidth();
        int w = (width * 150) / height;     // calculates the new width
        
        BufferedImage orImg = img;                                      // original image
        BufferedImage rzImg= new BufferedImage(w, 150, orImg.getType());// creates a new blank image with the resized sizes
        
        Graphics2D g2 = rzImg.createGraphics();     // graphics of the original image
        g2.drawImage(orImg, 0, 0, w, 150, null);    // redraws the origianl image with the resized sizes
        g2.dispose();
        
        return rzImg;
    }
    
    /**
     * creates an image collage of the given images
     * 
     * @param imgs      the ArrayList of images to collage, must be of CollageImagePicture  
     * @param txts      the ArrayList of text to add to collage, must be of CollageImageText  
     * @param width     the width of the collage image
     * @param height    the height of the collage image
     * @param bgCol     the background colour of the image
     * @return          the collage image
     */
    public static BufferedImage collageImage(ArrayList<CollageImagePicture> imgs, 
            ArrayList<CollageImageText> txts, int width, int height, Color bgCol){
        
        BufferedImage clImg= new BufferedImage(width, height, imgs.get(0).getImg().getType()); // the blank for the collage-image
        Graphics2D g2 = clImg.createGraphics();
        g2.setColor(bgCol);                 
        g2.fillRect(0, 0, width, height);   // paints the backgrounds white
        for(CollageImagePicture c : imgs){
            g2.drawImage(c.getImg(),                    // draws the various images of the collage
                    c.getoX(), c.getoY(), 
                    c.getWidth(), c.getHeight(), null);
        }
        for(CollageImageText t : txts){
            g2.setColor(t.getBgcolor());        // set backgorund colour for the text
            g2.fillRect(t.getX(), t.getY(), 
                    (int)(t.getWidth()*1.1), t.getHeight());    // draws the bg-colour
            g2.setColor(t.getColor());                          // sets text font colour
            g2.setFont(new Font(t.getFont(), Font.PLAIN, t.getSize())); // sets text font 
            g2.drawString(t.getText(), 
                    t.getX(), (int)(t.getY()+t.getHeight()*0.7));   // writes the text
        }
        
        g2.dispose();
        
        return clImg;
    }
    
    /**
     * crops an image
     * 
     * @param path      the name of the image file to crop
     * @param sX        the x-coordinate of the starting corner of the cropping rectangle
     * @param sY        the y-coordinate of the starting corner of the cropping rectangle
     * @param width     the width of the rectangle
     * @param height    the height of the rectangle
     * @return          the cropped image
     */
    public static BufferedImage cropImage(String path, int sX, int sY, int width, int height){
        try {
            return cropImage(ImageIO.read(new File(path)), sX, sY, width, height);
        } catch (IOException ex) {
            showError(ex, "IO Error during image crop");
            return null;
        }
    }
    
    /**
     * rectangular crops an image
     * 
     * @param bfi       the image to crop
     * @param sX        the x-coordinate of the starting corner of the cropping rectangle
     * @param sY        the y-coordinate of the starting corner of the cropping rectangle
     * @param width     the width of the rectangle
     * @param height    the height of the rectangle
     * @return          the cropped image
     */
    public static BufferedImage cropImage(BufferedImage bfi, int sX, int sY, int width, int height){
        SnapshotParameters parameters = new SnapshotParameters();   // creates a snapsot object for crop
        parameters.setFill(javafx.scene.paint.Color.TRANSPARENT);   // makes the crop keep its color
        
        // resizes the rectangle if it goes out of bounds of the original image
        int w = width, h = height;
        if (sX < 0) {
            w = width + sX;
            sX = 0;
        }
        if (sY < 0) {
            h = height + sY;
            sY = 0;
        }
        
        if(sX + width > bfi.getWidth()){
            w = bfi.getWidth() - sX;
        }
        if(sY + height > bfi.getHeight()){
            h = bfi.getHeight() - sY;
        }
        
        parameters.setViewport(new Rectangle2D( sX, sY, w, h));         // sets the sizes for the crop

        WritableImage wi = new WritableImage(w, h);
        ImageView iv = new ImageView(SwingFXUtils.toFXImage(bfi, null));
        iv.snapshot(parameters, wi);                                    // crops the image

        BufferedImage imgOpaque = SwingFXUtils.fromFXImage(wi, null);   // redraws the image
        BufferedImage imgNormal = new BufferedImage(                    // correcting its
                imgOpaque.getWidth(), imgOpaque.getHeight(),            // colours
                BufferedImage.OPAQUE);

        Graphics2D g2 = imgNormal.createGraphics();
        g2.drawImage(imgOpaque, 0, 0, null);
        return imgNormal;
    }
    
    /**
     * circularly crops an image
     * 
     * @param bfi       the image to crop
     * @param region    the shape of the crop 
     * @return          the cropped image
     */
    public static BufferedImage cropImage(BufferedImage bfi, Circle region){
        BufferedImage img = bfi;
        int width = (int)bfi.getWidth();
        int height = (int)bfi.getHeight();
        
        int argb, a, r, g, b, rgb, sum;
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if(!region.contains(i, j)){     // colours in white the pixels which are 
                    argb = img.getRGB(i, j);    // outside of the cropped region

                    rgb = ((0&0x0ff)<<24)|((255&0x0ff)<<16)|((255&0x0ff)<<8)|(255&0x0ff); // white colour

                    img.setRGB(i,j, rgb);
                }
            }
        }
        return img;
    }
    
    /**
     * rotates an image
     * 
     * @param path              the name of the image file to rotate
     * @param rotationDegree    the rotation degree
     * @return                  the rotated image
     */
    public static BufferedImage rotateImage(String path, int rotationDegree){
        return rotateImage(new Image(new File(path).toURI().toString()), rotationDegree);
    }
    
    /**
     * rotates an image
     * 
     * @param img               the image to rotate
     * @param rotationDegree    the rotation degree
     * @return                  the rotated image
     */
    public static BufferedImage rotateImage(Image img, int rotationDegree){
        ImageView iv = new ImageView(img);
        iv.setRotate(rotationDegree);
        WritableImage wi = iv.snapshot(new SnapshotParameters(), null); // re-captures the rotated image

        BufferedImage imgOpaque = SwingFXUtils.fromFXImage(wi, null);   // converts the rotated image
        BufferedImage imgNormal = new BufferedImage(                    // with the correct colours
                imgOpaque.getWidth(), imgOpaque.getHeight(),
                BufferedImage.OPAQUE);

        Graphics2D g2 = imgNormal.createGraphics();
        g2.drawImage(imgOpaque, 0, 0, null);            // re-draws the rotated image
        return imgNormal;
    }
    
    /**
     * apply Black and White filter to image 
     * 
     * @param path      the name of the image file to rotate
     * @return          the filtered image
     */
    public static BufferedImage filterImageBlackAndWhite(String path){   
        try {
            return filterImageBlackAndWhite(ImageIO.read(new File(path)));
        } catch (IOException ex) {
            showError(ex, "IO Error during image filter - black and white");
            return null;
        }
    }
    
    /**
     * apply Black and White filter to image 
     * 
     * @param bif       the image to filter
     * @return          the filtered image
     */
    public static BufferedImage filterImageBlackAndWhite(BufferedImage bif){   
        Rectangle rect = new Rectangle(0, 0, bif.getWidth(), bif.getHeight());
        return filterImageBlackAndWhite(bif, rect);
    }
    
    /**
     * apply Black and White filter to the given region image
     * 
     * @param bif       the image to filter
     * @param region    the region - rectangle
     * @return          the filtered image
     */
    public static BufferedImage filterImageBlackAndWhite(BufferedImage bif, Rectangle region){   
        BufferedImage img = bif;
        int width = (int)bif.getWidth();
        int height = (int)bif.getHeight();
        
        int argb, a, r, g, b, rgb, sum;
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if(region.contains(i, j)){      // if the pixel is inside the region apply the filter
                    argb = img.getRGB(i, j);    // reads the pixels colors value

                    a = (argb>>24)&0xFF;        // reads the alpha value
                    r = (argb>>16)&0xFF;        // reads the red value
                    g = (argb>>8) &0xFF;        // reads the greeen value
                    b = (argb)    &0xFF;        // reads the blue value

                    sum = r+g+b;
                    if ((sum/3) > 127) {            // if the average of the three colours is > 127
                        rgb = ((255&0x0ff)<<24)|    // set the colour to white
                              ((255&0x0ff)<<16)|
                              ((255&0x0ff)<<8) |
                               (255&0x0ff);
                    } else {                        // else set to black
                        rgb = ((255&0x0ff)<<24)|    // set to blakc
                              ((0&0x0ff)<<16)  |
                              ((0&0x0ff)<<8)   |
                               (0&0x0ff);
                    }

                    img.setRGB(i,j, rgb);
                }
            }
        }
        return img;
    }
    
    /**
     * apply Black and White filter to region of the image
     * 
     * @param bif           the image to filter
     * @param region        the region - circle
     * @return              the filtered image
     */
    public static BufferedImage filterImageBlackAndWhite(BufferedImage bif, Circle region){   
        BufferedImage img = bif;
        int width = (int)bif.getWidth();
        int height = (int)bif.getHeight();
        
        int argb, a, r, g, b, rgb, sum;
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if(region.contains(i, j)){      // if the pixle is within the region, apply the filter 
                    argb = img.getRGB(i, j);

                    a = (argb>>24)&0xFF;
                    r = (argb>>16)&0xFF;
                    g = (argb>>8) &0xFF;
                    b = (argb)    &0xFF;

                    sum = r+g+b;
                    if ((sum/3) > 127) {
                        rgb = ((255&0x0ff)<<24)|    // white colour
                              ((255&0x0ff)<<16)|
                              ((255&0x0ff)<<8) |
                               (255&0x0ff);
                    } else {
                        rgb = ((255&0x0ff)<<24)|
                              ((0&0x0ff)<<16)  |    // black colour
                              ((0&0x0ff)<<8)   |
                               (0&0x0ff);
                    }

                    img.setRGB(i,j, rgb);
                }
            }
        }
        return img;
    }
    
    /**
     * apply greyscale filter to image
     * 
     * @param path      the name of the image to filter
     * @param method    the method to use to calculate the greyscale value
     * @return          the filtered image
     */
    public static BufferedImage filterImageGreyscale(String path, GreyscaleMethod method){   
        try {
            return filterImageGreyscale(ImageIO.read(new File(path)), method);
        } catch (IOException ex) {
            showError(ex, "IO Error during image filter - greyscale");
            return null;
        }
    }
    
    /**
     * apply greyscale filter to image
     * 
     * @param bfi       the buffered image to filter
     * @param method    the greyscale method
     * @return          the filtered image
     */
    public static BufferedImage filterImageGreyscale(BufferedImage bfi, GreyscaleMethod method){   
        Rectangle rect = new Rectangle(0, 0, bfi.getWidth(), bfi.getHeight());
        return filterImageGreyscale(bfi, method, rect);
    }
    
    /**
     * apply greyscale filter to a region of the image
     * 
     * @param bfi       the buffered image to filter
     * @param method    the method to use to calculate the greyscale value
     * @param region    the region to filter - rectangle
     * @return          the filtered image
     */
    public static BufferedImage filterImageGreyscale(BufferedImage bfi, 
            GreyscaleMethod method, Rectangle region){
        
        BufferedImage img = bfi;
        int width = (int)img.getWidth();
        int height = (int)img.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb;
                if(region.contains(i, j)){      // selects the algorithm based on the method
                    switch(method){
                        case AVG:
                            rgb = PictureManipulator.getRGBGreyscaleAveraging(img.getRGB(i, j));
                            break;

                        case LUMA:
                            rgb = PictureManipulator.getRGBGreyscaleLumaCorrection(img.getRGB(i, j));
                            break;

                        case DESAT:
                            rgb = PictureManipulator.getRGBGreyscaleDesaturation(img.getRGB(i, j));
                            break;

                        case DECOM_MIN:
                            rgb = PictureManipulator.getRGBGreyscaleDecompositionMin(img.getRGB(i, j));
                            break;

                        case DECOM_MAX:
                            rgb = PictureManipulator.getRGBGreyscaleDecompositionMax(img.getRGB(i, j));
                            break;

                        case SNGCHL_R:
                            rgb = PictureManipulator.getRGBGreyscaleSingleChannelRed(img.getRGB(i, j));
                            break;

                        case SNGCHL_G:
                            rgb = PictureManipulator.getRGBGreyscaleSingleChannelGreen(img.getRGB(i, j));
                            break;

                        case SNGCHL_B:
                            rgb = PictureManipulator.getRGBGreyscaleSingleChannelBlue(img.getRGB(i, j));
                            break;

                        default:
                            rgb = img.getRGB(i, j);
                    }
                    img.setRGB(i,j, rgb);
                }
            }
        }
        return img;
    }
    
    /**
     * apply greyscale filter to a region of the image
     * 
     * @param bfi       the buffered image to filter
     * @param method    the method to use to calculate the greyscale value
     * @param region    the region to filter - circle
     * @return          the filtered image
     */
    public static BufferedImage filterImageGreyscale(BufferedImage bfi, 
            GreyscaleMethod method, Circle region){
        
        BufferedImage img = bfi;
        int width = (int)img.getWidth();
        int height = (int)img.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb;
                if(region.contains(i, j)){
                    switch(method){      // selects the algorithm based on the method
                        case AVG:
                            rgb = PictureManipulator.getRGBGreyscaleAveraging(img.getRGB(i, j));
                            break;

                        case LUMA:
                            rgb = PictureManipulator.getRGBGreyscaleLumaCorrection(img.getRGB(i, j));
                            break;

                        case DESAT:
                            rgb = PictureManipulator.getRGBGreyscaleDesaturation(img.getRGB(i, j));
                            break;

                        case DECOM_MIN:
                            rgb = PictureManipulator.getRGBGreyscaleDecompositionMin(img.getRGB(i, j));
                            break;

                        case DECOM_MAX:
                            rgb = PictureManipulator.getRGBGreyscaleDecompositionMax(img.getRGB(i, j));
                            break;

                        case SNGCHL_R:
                            rgb = PictureManipulator.getRGBGreyscaleSingleChannelRed(img.getRGB(i, j));
                            break;

                        case SNGCHL_G:
                            rgb = PictureManipulator.getRGBGreyscaleSingleChannelGreen(img.getRGB(i, j));
                            break;

                        case SNGCHL_B:
                            rgb = PictureManipulator.getRGBGreyscaleSingleChannelBlue(img.getRGB(i, j));
                            break;

                        default:
                            rgb = img.getRGB(i, j);
                    }
                    img.setRGB(i,j, rgb);
                }
            }
        }
        return img;
    }
    
    /**
     * calculates the greyscale based on the average of the RGB colours value
     * 
     * @param argb      the pixel's colour
     * @return          the greyscale colour
     */
    private static int getRGBGreyscaleAveraging(int argb){
        int a = (argb>>24)&0xFF;
        int r = (argb>>16)&0xFF;
        int g = (argb>>8) &0xFF;
        int b = (argb)    &0xFF;

        int grey = (r+g+b)/3;
        return ((a&0x0ff)<<24)|((grey&0x0ff)<<16)|((grey&0x0ff)<<8)|(grey&0x0ff);
    }
    
    /**
     * calculates the greyscale based on LUMA correction algorithm
     * 
     * @param argb      the pixel's colour
     * @return          the greyscale colour
     */
    private static int getRGBGreyscaleLumaCorrection(int argb){
        int a = (argb>>24)&0xFF;
        int r = (argb>>16)&0xFF;
        int g = (argb>>8) &0xFF;
        int b = (argb)    &0xFF;
        
        int gr = (int) (r * 0.3);   // new red
        int gg = (int) (g * 0.59);  // new green
        int gb = (int) (b * 0.11);  // new blue
        int grey = (r+g+b);         // the gray color
        grey = (grey > 255) ? 255 : grey;  // sets grey to 255 if exceeds it
        return ((a&0x0ff)<<24)|((grey&0x0ff)<<16)|((grey&0x0ff)<<8)|(grey&0x0ff);
    }
    
    /**
     * calculates the greyscale based on desaturation algorithm
     * 
     * @param argb      the pixel's colour
     * @return          the greyscale colour
     */
    private static int getRGBGreyscaleDesaturation(int argb){
        int a = (argb>>24)&0xFF;
        int r = (argb>>16)&0xFF;
        int g = (argb>>8) &0xFF;
        int b = (argb)    &0xFF;
        
        int grey = ( Math.max(r, Math.max(g, b)) + Math.min(r, Math.min(g, b)) )/2;
        return ((a&0x0ff)<<24)|((grey&0x0ff)<<16)|((grey&0x0ff)<<8)|(grey&0x0ff);
    }
    
    /**
     * calculates the greyscale based on min value decomposition algorithm
     * 
     * @param argb      the pixel's colour
     * @return          the greyscale colour
     */
    private static int getRGBGreyscaleDecompositionMin(int argb){
        int a = (argb>>24)&0xFF;
        int r = (argb>>16)&0xFF;
        int g = (argb>>8) &0xFF;
        int b = (argb)    &0xFF;
        
        int grey = Math.min(r, Math.min(g, b));
        return ((a&0x0ff)<<24)|((grey&0x0ff)<<16)|((grey&0x0ff)<<8)|(grey&0x0ff);
    }
    
    /**
     * calculates the greyscale based on max value decomposition algorithm
     * 
     * @param argb      the pixel's colour
     * @return          the greyscale colour
     */
    private static int getRGBGreyscaleDecompositionMax(int argb){
        int a = (argb>>24)&0xFF;
        int r = (argb>>16)&0xFF;
        int g = (argb>>8) &0xFF;
        int b = (argb)    &0xFF;
        
        int grey = Math.max(r, Math.max(g, b));
        return ((a&0x0ff)<<24)|((grey&0x0ff)<<16)|((grey&0x0ff)<<8)|(grey&0x0ff);
    }
    
    /**
     * calculates the greyscale based on red value
     * 
     * @param argb      the pixel's colour
     * @return          the greyscale colour
     */
    private static int getRGBGreyscaleSingleChannelRed(int argb){
        int a = (argb>>24)&0xFF;
        int r = (argb>>16)&0xFF;
        
        int grey = r;
        return ((a&0x0ff)<<24)|((grey&0x0ff)<<16)|((grey&0x0ff)<<8)|(grey&0x0ff);
    }
    
    /**
     * calculates the greyscale based on green value
     * 
     * @param argb      the pixel's colour
     * @return          the greyscale colour
     */
    private static int getRGBGreyscaleSingleChannelGreen(int argb){
        int a = (argb>>24)&0xFF;
        int g = (argb>>8) &0xFF;
        
        int grey = g;
        return ((a&0x0ff)<<24)|((grey&0x0ff)<<16)|((grey&0x0ff)<<8)|(grey&0x0ff);
    }
    
    /**
     * calculates the greyscale based on blue value
     * 
     * @param argb      the pixel's colour
     * @return          the greyscale colour
     */
    private static int getRGBGreyscaleSingleChannelBlue(int argb){
        int a = (argb>>24)&0xFF;
        int b = (argb)    &0xFF;
        
        int gary = b;
        return ((a&0x0ff)<<24)|((gary&0x0ff)<<16)|((gary&0x0ff)<<8)|(gary&0x0ff);
    }
    
    /**
     * apply negative filter to the image
     * 
     * @param path      the path of the image
     * @return          the filtered image
     */
    public static BufferedImage filterImageNegative(String path){   
        try {
           return filterImageNegative(ImageIO.read(new File(path)));
        } catch (IOException ex) {
            showError(ex, "IO Error during image filter - greyscale");
            return null;
        }
    }
    
    /**
     * apply negative filter to the image
     * 
     * @param bfi       the image to filter
     * @return          the filtered image
     */
    public static BufferedImage filterImageNegative(BufferedImage bfi){   
        Rectangle rect = new Rectangle(0, 0, bfi.getWidth(), bfi.getHeight());
        return filterImageNegative(bfi, rect);
    }
    
    /**
     * apply negative filter to region of the image
     * @param bfi       the image to filter
     * @param region    the region - rectangle
     * @return          the filtered image
     */
    public static BufferedImage filterImageNegative(BufferedImage bfi, Rectangle region){   
        BufferedImage img = bfi;
        int width = (int)img.getWidth();
        int height = (int)img.getHeight();
        
        int argb, a, r, g, b, rgb, sum;
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if(region.contains(i, j)){      // if the pixel is in the image, apply the filter
                    argb = img.getRGB(i, j);

                    a = (argb>>24)&0xFF;
                    r = (argb>>16)&0xFF;
                    g = (argb>>8) &0xFF;
                    b = (argb)    &0xFF;

                    r = 255 - r;
                    g = 255 - g;
                    b = 255 - r;
                    rgb = ((a&0x0ff)<<24)|((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);

                    img.setRGB(i,j, rgb);
                }
            }
        }
        return img;
    }
    
    /**
     * apply negative filter to region of the image
     * @param bfi       the image to filter
     * @param region    the region - circle
     * @return          the filtered image
     */
    public static BufferedImage filterImageNegative(BufferedImage bfi, Circle region){   
        BufferedImage img = bfi;
        int width = (int)img.getWidth();
        int height = (int)img.getHeight();
        
        int argb, a, r, g, b, rgb, sum;
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if(region.contains(i, j)){          // if the pixel is in the image, apply the filter
                    argb = img.getRGB(i, j);

                    a = (argb>>24)&0xFF;
                    r = (argb>>16)&0xFF;
                    g = (argb>>8) &0xFF;
                    b = (argb)    &0xFF;

                    r = 255 - r;
                    g = 255 - g;
                    b = 255 - r;
                    rgb = ((a&0x0ff)<<24)|((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);

                    img.setRGB(i,j, rgb);
                }
            }
        }
        return img;
    }
    
    /**
     * apply sepia filter to region of the image
     * @param path      the path of the image
     * @return          the filtered image
     */
    public static BufferedImage filterImageSepia(String path){   
        try {
           return filterImageSepia(ImageIO.read(new File(path)));
        } catch (IOException ex) {
            showError(ex, "IO Error during image filter - sepia");
            return null;
        }
    }
    
    /**
     * apply sepia filter to region of the image
     * @param bfi       the image to filter
     * @return          the filtered image
     */
    public static BufferedImage filterImageSepia(BufferedImage bfi){   
        Rectangle rect = new Rectangle(0, 0, bfi.getWidth(), bfi.getHeight());
        return filterImageSepia(bfi, rect);
    }
    
    /**
     * apply sepia filter to region of the image
     * @param bfi       the image to filter
     * @param region    the region - rectangle
     * @return          the filtered image
     */
    public static BufferedImage filterImageSepia(BufferedImage bfi, Rectangle region){   
        BufferedImage img = bfi;
        int width = (int)img.getWidth();
        int height = (int)img.getHeight();
        
        int argb, a, r, g, b, rgb, sum;
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if(region.contains(i, j)){
                    argb = img.getRGB(i, j);

                    a = (argb>>24)&0xFF;
                    r = (argb>>16)&0xFF;
                    g = (argb>>8) &0xFF;
                    b = (argb)    &0xFF;

                    r = ((int) ((r * 0.393) + (g * 0.769) + (b * 0.189)));
                    r = (r>255) ? 255 : r;
                    g = (int) ((r * 0.349) + (g * 0.686) + (b * 0.168));
                    g = (g>255) ? 255 : g;
                    b = (int) ((r * 0.272) + (g * 0.534) + (b * 0.131));
                    b = (b>255) ? 255 : b;

                    rgb = ((a&0x0ff)<<24)|((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);

                    img.setRGB(i,j, rgb);
                }
            }
        }
        return img;
    }
    
    /**
     * apply sepia filter to region of the image
     * @param bfi       the image to filter
     * @param region    the region - circle
     * @return          the filtered image
     */
    public static BufferedImage filterImageSepia(BufferedImage bfi, Circle region){   
        BufferedImage img = bfi;
        int width = (int)img.getWidth();
        int height = (int)img.getHeight();
        
        int argb, a, r, g, b, rgb, sum;
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if(region.contains(i, j)){
                    argb = img.getRGB(i, j);

                    a = (argb>>24)&0xFF;
                    r = (argb>>16)&0xFF;
                    g = (argb>>8) &0xFF;
                    b = (argb)    &0xFF;

                    r = ((int) ((r * 0.393) + (g * 0.769) + (b * 0.189)));
                    r = (r>255) ? 255 : r;
                    g = (int) ((r * 0.349) + (g * 0.686) + (b * 0.168));
                    g = (g>255) ? 255 : g;
                    b = (int) ((r * 0.272) + (g * 0.534) + (b * 0.131));
                    b = (b>255) ? 255 : b;

                    rgb = ((a&0x0ff)<<24)|((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);

                    img.setRGB(i,j, rgb);
                }
            }
        }
        return img;
    }
    
    
    private static void showError(Exception ex, String title){
        String cause = "";
        if(ex.getCause() != null){
            cause = ex.getCause().toString();
        }
        AlertWindow.display(title, ex.getMessage()+"\n\nCause--\n\n"+cause);
    }
  
}
