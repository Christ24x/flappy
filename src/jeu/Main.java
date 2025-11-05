package jeu;

import javax.swing.*;

// La classe Main sert de point d'entrée pour l'application graphique (JFrame).
public class Main {

    public static void main(String[] args) {
        // Dimensions utilisées par la classe FlappyBird
        int boardWidth = 360;
        int boardHeight = 640;
        
        // 1. Création de la fenêtre (JFrame)
        JFrame frame = new JFrame("Flappy Bird");

        // 2. Configuration de la fenêtre
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null); // Centrer la fenêtre sur l'écran
        frame.setResizable(false); // Empêcher le redimensionnement
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fermer l'application

        // 3. Création et ajout du panneau de jeu (FlappyBird)
        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird); 
        
        // La méthode pack() redimensionne la fenêtre pour s'adapter à la taille préférée du JPanel (360x640)
        frame.pack();
        
        // 4. Donner le focus au panneau de jeu pour qu'il reçoive les événements clavier (très important !)
        flappyBird.requestFocus();
        
        // 5. Rendre la fenêtre visible (toujours la dernière étape pour l'affichage)
        frame.setVisible(true);
    }
}