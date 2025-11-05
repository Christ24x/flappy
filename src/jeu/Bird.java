package jeu;

import java.awt.Image;
import java.awt.Rectangle;

class Bird {
    private int x;
    private int y;
    private int width;
    private int height;
    private Image img;

    Bird(Image img, int x, int y, int width, int height) {
        this.img = img;
        this.x = x; 
        this.y = y;
        this.width = width;
        this.height = height;

        new Rectangle(x, y, width, height);
    }

    public Image getImg(){
        return this.img;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public int getHeight(){
        return this.height;
    }

    public int getWidth(){
        return this.width;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setImg(Image img){
        this.img = img;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public void setWidth(int width){
        this.width = width;
    }
    
}