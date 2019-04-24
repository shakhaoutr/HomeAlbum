/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Color;

/**
 *
 * @author Shakhaout Rahman
 */
public class CollageImageText {
    
    String text;
    String font;
    Color color;
    int size;
    Color bgcolor;
    int x;
    int y;
    int width;
    int height;

    public CollageImageText(String text, String font, Color color, int size, Color bgcolor, int x, int y, int width, int height) {
        this.text = text;
        this.font = font;
        this.color = color;
        this.size = size;
        this.bgcolor = bgcolor;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Color getBgcolor() {
        return bgcolor;
    }

    public void setBgcolor(Color bgcolor) {
        this.bgcolor = bgcolor;
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

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    
    
    
    
}
