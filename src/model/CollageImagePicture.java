/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.image.BufferedImage;

/**
 *
 * @author Shakhaout Rahman
 */
public class CollageImagePicture {
    BufferedImage img;
    int oX;
    int oY;
    int width;
    int height;

    public CollageImagePicture(BufferedImage img, int oX, int oY, int width, int height) {
        this.img = img;
        this.oX = oX;
        this.oY = oY;
        this.width = width;
        this.height = height;
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public int getoX() {
        return oX;
    }

    public void setoX(int oX) {
        this.oX = oX;
    }

    public int getoY() {
        return oY;
    }

    public void setoY(int oY) {
        this.oY = oY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
}
