package jeu;

import java.awt.Image;
import java.awt.Rectangle;

class Pipe {
    int x;
    int y;
    int width;
    int height;
    Image img;
    boolean passed = false;

    Pipe(Image img, int x, int y, int width, int height) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        // Création du rectangle
        new Rectangle(x, y, width, height);
    }
}