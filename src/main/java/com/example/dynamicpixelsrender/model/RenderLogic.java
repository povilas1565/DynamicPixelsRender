package com.example.dynamicpixelsrender.model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class RenderLogic {

    public static int COLOR_RGB = 0;
    public static int COLOR_BW = 1;
    public static int COLOR_CUSTOM = 2;

    private int imageWidth;
    private int imageHeight;
    private int imageColor;
    private int imagePixelWidth;
    private int imagePixelHeight;
    private boolean imageAlpha;
    private ArrayList<CustomPixelColor> imageCustomPC = new ArrayList<CustomPixelColor>();

    private int videoWidth;
    private int videoHeight;
    private int videoFps;
    private int videoLength;
    private int videoColor;
    private int videoPixelWidth;
    private int videoPixelHeight;
    private ArrayList<CustomPixelColor> videoCustomPC = new ArrayList<CustomPixelColor>();

    public RenderLogic(boolean setData) {
        if(setData) {
            imageWidth = 100;
            imageHeight = 100;
            imageColor = 1;
            imagePixelWidth = 1;
            imagePixelHeight = 1;
            imageAlpha = false;
            imageCustomPC.add(new CustomPixelColor(Color.RED.getRGB()));
            imageCustomPC.add(new CustomPixelColor(Color.GREEN.getRGB()));
            imageCustomPC.add(new CustomPixelColor(Color.BLUE.getRGB()));
        }
    }

    public void createImage(LogicNote logicNote) {
        //Start
        logicNote.runing(true);

        String format = "png";

        try {

            int imageType = imageAlpha ? BufferedImage.TYPE_4BYTE_ABGR : BufferedImage.TYPE_3BYTE_BGR;
            BufferedImage img = new BufferedImage(imageWidth, imageHeight, imageType);

            if(imagePixelWidth == 0 && imagePixelHeight == 0) {
                for (int h = 0; h < imageHeight; h++) {

                    // Time
                    long start = System.currentTimeMillis();

                    for (int w = 0; w < imageWidth; w++) {
                        int r = 0;
                        int g = 0;
                        int b = 0;

// TODO: Extend method and optimize color variable
                        switch (imageColor) {
                            case 0:
                                r = (int)(Math.random()*256);	//red
                                g = (int)(Math.random()*256);	//green
                                b = (int)(Math.random()*256);	//blue
                                break;
                            case 1:
                                r = g = b = (int)(Math.random()*256);
                                break;
                            case 2:
                                int i = (int)(Math.random()*imageCustomPC.size());

                                Color c = new Color( imageCustomPC.get(i).getCPColor() );

                                r = c.getRed();
                                g = c.getGreen();
                                b = c.getBlue();

                                break;
                        }

                        if(imageAlpha) {
                            int a = (int)(Math.random()*256);	//alpha
                            img.setRGB(w, h, new Color(r, g, b, a).getRGB());
                        }else {
                            img.setRGB(w, h, new Color(r, g, b).getRGB());
                        }
                    }

                    // Time
                    long end = System.currentTimeMillis() - start;

                    if(logicNote.isCancel()) {

                        //On Cancel
                        logicNote.display("Cancel");
                        logicNote.runing(false);
                        logicNote.setCancel(false);
                        System.out.println("Loop Cancel");
                        return;
                    }else {
                        if(end > 0) {
                            // Time
                            logicNote.display("Done: "+(100*h/imageHeight)+"% Time left: "+  (end + end*(imageHeight - h))/1000l +"s");
                        }
                    }
                }
            }else {
                Graphics2D g2d = img.createGraphics();

                for (int h = 0; h < imageHeight; h += imagePixelHeight) {

                    // Time
                    long start = System.currentTimeMillis();

                    for (int w = 0; w < imageWidth; w += imagePixelWidth) {
                        int r = 0;
                        int g = 0;
                        int b = 0;

// TODO: Extend method and optimize color variable
                        switch (imageColor) {
                            case 0:
                                r = (int)(Math.random()*256);
                                g = (int)(Math.random()*256);
                                b = (int)(Math.random()*256);
                                break;
                            case 1:
                                r = g = b = (int)(Math.random()*256);
                                break;
                            case 2:
                                int i = (int)(Math.random()*imageCustomPC.size());

                                Color c = new Color( imageCustomPC.get(i).getCPColor() );

                                r = c.getRed();
                                g = c.getGreen();
                                b = c.getBlue();

                                break;
                        }

                        if(imageAlpha) {
                            int a = (int)(Math.random()*256);	//alpha
                            g2d.setPaint(new Color(r, g, b, a));
                        }else {
                            g2d.setPaint(new Color(r, g, b));
                        }

                        g2d.fillRect(w, h, imagePixelWidth, imagePixelHeight);
                    }

                    // Time
                    long end = System.currentTimeMillis() - start;

                    if(logicNote.isCancel()) {

                        //On Cancel
                        logicNote.display("Cancel");
                        logicNote.runing(false);
                        logicNote.setCancel(false);
                        System.out.println("Loop Cancel");
                        return;
                    }else {
                        if(end > 0) {
                            // Time
                            logicNote.display("Done: "+(100*h/imageHeight)+"% Time left: "+  (end + end*(imageHeight - h))/1000l +"s");
                        }
                    }
                }
                g2d.dispose();
            }

            try {
                checkRenderFolder();
                File f = new File("render/image." + format);

                logicNote.display("Writing file...");

                ImageIO.write(img, format, f);
                img.flush();
            } catch (Exception e) {
                System.out.println(e);
                logicNote.setStatus(e);
            }

        } catch (Exception e) {
            System.out.println(e);
            logicNote.setStatus(e);
        } catch (OutOfMemoryError e) {
            System.out.println(e);
            logicNote.setStatus(new Exception(e));
        }

        //End
        logicNote.display("Done");
        logicNote.displayAlert("Image");
        logicNote.runing(false);
    }

    private void checkRenderFolder() {
        File file = new File("render");
        if(!file.isDirectory()) {
            file.mkdirs();
        }
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int width) {
        this.imageWidth = width;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int height) {
        this.imageHeight = height;
    }

    public int getImageColor() {
        return imageColor;
    }

    public void setImageColor(int imageColor) {
        this.imageColor = imageColor;
    }

    public int getImagePixelWidth() {
        return imagePixelWidth;
    }

    public int getImagePixelHeight() {
        return imagePixelHeight;
    }

    public void setImagePixelWidth(int width) {
        this.imagePixelWidth = width;
    }

    public void setImagePixelHeight(int height) {
        this.imagePixelHeight = height;
    }

    public boolean isImageAlpha() {
        return imageAlpha;
    }

    public void setImageAlpha(boolean imageAlpha) {
        this.imageAlpha = imageAlpha;
    }

    public ArrayList<CustomPixelColor> getImageCustomPC() {
        return imageCustomPC;
    }

}