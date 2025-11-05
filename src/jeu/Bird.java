package jeu;

class Bird {
        private int x = birdx;
        private int y = birdy;
        private int width = birdWidth;
        private int height = birdHeight;
        private Image img;

        Bird(Image img, int x, int y, int width, int height) {
            this.img = img;
            this.x = x; 
            this.y = y;
            this.width = width;
            this.height = height;

            return new Rectangle(x, y, width, height);
        }
}