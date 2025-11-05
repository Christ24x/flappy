package jeu;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    
    // Infos fenêtre
    private int boardWidth = 360;
    private int boardHeight = 640;

    // Images
    Image backgroundImg;
    private Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    // Constantes de l'oiseau
    private int birdX = boardWidth / 8;
    private int birdY = boardHeight / 2;
    private int birdWidth = 90;
    private int birdHeight = 80;

    // Infos sur la disposition des tuyaux
    private int gap = boardHeight / 4;

    // Constantes des tuyaux
    private int maxPipeHeight = 512;
    private int minPipeHeight = 50;
    // int pipeX = boardWidth;
    // int pipeY = 0;
    private int pipeWidth = 250; // Largeur de tuyau ajustée
    // int pipeHeight = 512;


    // ------------------------------------
    // 3. LOGIQUE GLOBALE DU JEU
    // ------------------------------------
    Bird bird;
    int velocityX = -3; // Vitesse de défilement des tuyaux
    int velocityY = 0;
    int gravity = 1;
    
    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer placePipesTimer;

    // Variables d'état et de score
    boolean gameOver = false;
    boolean gameStarted = false;
    double score = 0;

    // ------------------------------------
    // 4. CONSTRUCTEUR
    // ------------------------------------
    FlappyBird() {

        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        // Chargement des images
        backgroundImg = new ImageIcon(getClass().getResource("./bground.jpg")).getImage();
        this.birdImg = new ImageIcon(getClass().getResource("./flappy.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./thaut.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./tbas.png")).getImage();

        // Initialisation des objets
        bird = new Bird(this.birdImg, this.birdX, this.birdY, this.birdWidth, this.birdHeight);
        pipes = new ArrayList<Pipe>();

        // Timer pour l'apparition des tuyaux
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Les tuyaux n'apparaissent que si le jeu est démarré et non terminé
                if (!gameOver && gameStarted) {
                    placePipes();
                }
            }
        });
        placePipesTimer.start(); 

        // Game timer (boucle principale)
        gameLoop = new Timer(20, this);
        gameLoop.start();
    }

    // ------------------------------------
    // 5. CRÉATION DES TUYAUX
    // ------------------------------------
    public void 
    placePipes() {
        
        // Plage de hauteur aléatoire jouable
        int randomInterval = this.maxPipeHeight - this.minPipeHeight;

        int topPipeHeight = this.minPipeHeight + random.nextInt(randomInterval);
        int pipeX = this.boardWidth;

        // Tuyau du haut
        Pipe topPipe = new Pipe(topPipeImg, pipeX, 0, this.pipeWidth, topPipeHeight);
        pipes.add(topPipe);

        // Tuyau du bas

        // Calcul de la hauteur de la pipe du bas
        int bottomPipeHeight = this.boardHeight - topPipeHeight - this.gap;

        // Calcul de la coordonnée Y de la pipe du bas
        int bottomPipeY = topPipeHeight + this.gap;

        // Création de la pipe du bas
        Pipe bottomPipe = new Pipe(bottomPipeImg, pipeX, bottomPipeY, this.pipeWidth, bottomPipeHeight);
        pipes.add(bottomPipe);
    }

    // ------------------------------------
    // 6. DESSIN
    // ------------------------------------
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        // Arrière-plan
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);
        
        // Hêvi (l'oiseau)
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);
        
        // Tuyaux
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }
        
        // Affichage du Score et Game Over
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        
        if (gameOver) {
            // Affichage du score final et du message de redémarrage
            g.drawString("GAME OVER : " + (int) score, 10, 50);
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Appuyez sur ESPACE pour rejouer", boardWidth / 2 - 170, boardHeight / 2);
            
        } else {
            // Affichage du score en temps réel
            g.drawString(String.valueOf((int) score), boardWidth / 2 - 10, 50);
        }
    }

    // ------------------------------------
    // 7. LOGIQUE DE JEU (MOVE)
    // ------------------------------------
    public void move() {
        if (gameOver) return; 

        // Mouvement vertical de l'oiseau (gravité)
        if (gameStarted) {
            velocityY += gravity;
            bird.y += velocityY;
            bird.y = Math.max(bird.y, 0); // Limite supérieure
        }

        // Détection de la collision avec le sol
        if (bird.y + bird.height > boardHeight) {
            gameOver = true;
        }

        // Mouvement, score et collision des tuyaux
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            
            // CORRECTION: Le mouvement horizontal ne se fait que si le jeu est démarré
            if (gameStarted) {
                pipe.x += velocityX; 
            }

            // Détection de la collision
            if (collision(bird, pipe)) {
                gameOver = true;
            }

            // CORRECTION: Calcul du score uniquement si le jeu est démarré
            if (gameStarted && pipe.x + pipe.width < bird.x && !pipe.passed && pipe.img == topPipeImg) {
                score += 0.5; 
                pipe.passed = true;
            }
        }
    }

    // ------------------------------------
    // 8. DÉTECTION DE COLLISION
    // ------------------------------------
    public boolean collision(Bird a, Pipe b) {
        // Utilisation de la méthode intersects pour une vérification fiable
        return a.getBounds().intersects(b.getBounds());
    }
    
    // ------------------------------------
    // 9. RÉINITIALISATION DU JEU
    // ------------------------------------
    public void resetGame() {
        // Réinitialisation de l'oiseau
        bird.y = birdy;
        velocityY = 0;
        
        // Réinitialisation de l'état
        pipes.clear();
        score = 0;
        gameOver = false;
        gameStarted = false;
        
        // Redémarrage des minuteurs
        gameLoop.start();
        placePipesTimer.start();
    }

    // ------------------------------------
    // 10. GESTION DES ÉVÉNEMENTS (LISTENERS)
    // ------------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        
        // Arrêt des minuteurs si Game Over
        if (gameOver) {
            placePipesTimer.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            
            if (gameOver) {
                // Redémarre le jeu si Game Over
                resetGame();
                
            } else {
                // Lance le jeu au premier saut et applique l'impulsion
                if (!gameStarted) {
                    gameStarted = true;
                }
                velocityY = -9; // Impulsion de saut
            }
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}