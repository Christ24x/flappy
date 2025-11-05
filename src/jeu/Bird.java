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
}