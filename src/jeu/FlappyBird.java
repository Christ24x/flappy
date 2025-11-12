import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;

    //images
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    //classe oiseau
    int birdX = boardWidth/8;
    int birdY = boardHeight/2;
    int birdWidth = 34;
    int birdHeight = 24;

    class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }

    //classe tuyau
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;  //mis à l'échelle par 1/6
    int pipeHeight = 512;
    
    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false; //indique si l'oiseau a passé ce tuyau

        Pipe(Image img) {
            this.img = img;
        }
    }

    //logique du jeu
    Bird bird;
    int velocityX = -4; //vitesse de déplacement des tuyaux vers la gauche (simule l'oiseau avançant)
    int velocityY = 0; //vitesse de déplacement de l'oiseau vers le haut/bas.
    int gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop; //boucle de jeu
    Timer placePipeTimer; //minuteur pour placer les tuyaux
    boolean gameOver = false;
    double score = 0;

    FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        // met la couleur de fond en bleu (commenté)
        setFocusable(true);
        addKeyListener(this);

        //charger les images
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        //oiseau
        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();

        //minuteur pour placer les tuyaux
        placePipeTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              // Code à exécuter
              placePipes();
            }
        });
        placePipeTimer.start();
        
        //minuteur de jeu
        gameLoop = new Timer(1000/60, this); //temps nécessaire pour démarrer le minuteur, millisecondes entre les frames
        gameLoop.start();
    }
    
    void placePipes() {
        // La hauteur des tuyaux sera dans la plage [-3/4 pipeHeight, -1/4 pipeHeight]
        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = boardHeight/4; //espace d'ouverture entre les tuyaux
    
        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);
    
        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }
    
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        //arrière-plan
        g.drawImage(backgroundImg, 0, 0, this.boardWidth, this.boardHeight, null);

        //oiseau
        g.drawImage(birdImg, bird.x, bird.y, bird.width, bird.height, null);

        //tuyaux
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        //score
        g.setColor(Color.white);

        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
        }
        else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
        
    }

    public void move() {
        //oiseau
        velocityY += gravity;
        bird.y += velocityY;
        //appliquer la gravité au bird.y actuel, limiter le bird.y au sommet du canevas
        bird.y = Math.max(bird.y, 0); 

        //tuyaux
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                //0.5 car il y a 2 tuyaux ! donc 0.5*2 = 1, 1 pour chaque paire de tuyaux
                score += 0.5; 
                pipe.passed = true;
            }

            if (collision(bird, pipe)) {
                gameOver = true;
            }
        }

        if (bird.y > boardHeight) {
            gameOver = true;
        }
    }

    boolean collision(Bird a, Pipe b) {
        //le coin supérieur gauche de 'a' n'atteint pas le coin supérieur droit de 'b'
        return a.x < b.x + b.width &&  
                //le coin supérieur droit de 'a' dépasse le coin supérieur gauche de 'b'
               a.x + a.width > b.x &&  
               //le coin supérieur gauche de 'a' n'atteint pas le coin inférieur gauche de 'b'
               a.y < b.y + b.height &&  
               //le coin inférieur gauche de 'a' dépasse le coin supérieur gauche de 'b'
               a.y + a.height > b.y;    
    }

    @Override
    public void actionPerformed(ActionEvent e) { 
        //appelé toutes les x millisecondes par le minuteur gameLoop
        move();
        repaint();
        if (gameOver) {
            placePipeTimer.stop();
            gameLoop.stop();
        }
    }  

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            // Affichage du saut (commenté)
            velocityY = -9;

            if (gameOver) {
                //redémarrer le jeu en réinitialisant les conditions
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                gameOver = false;
                score = 0;
                gameLoop.start();
                placePipeTimer.start();
            }
        }
    }

    //pas nécessaire
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}