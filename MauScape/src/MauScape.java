import javax.print.DocFlavor.STRING;

import extensions.CSVFile;

class MauScape extends Program { 
    
    Souris creerSouris(String nom){
        Souris s = new Souris();
        s.pv = 15;
        s.posX = 1;
        s.posY = 1;
        s.nom = nom;
        s.degat = 5;
        return s;
    }

    void testCreerSouris() {
        Souris souris = creerSouris("Jerry");
        assertTrue(souris != null);
        assertTrue(souris.pv == 100);
        assertTrue(souris.posX == 1 && souris.posY == 1);
        assertTrue("Jerry".equals(souris.nom));
    }

    Monstre creerMonstre(int etage){
        Monstre m = new Monstre();
        m.pv = etage*10;
        return m;
    }

    void testCreerMonstre() {
        Monstre monstre = creerMonstre(5);
        assertTrue(monstre != null );
        assertTrue(monstre.pv == 50);
    }

    void afficherCarte(String[][] carte, int posX, int posY){
        for(int i=0; i<length(carte); i++){
            String ligne ="" ;
            for(int j=0; j<length(carte[i]); j++){
                if(i == posX && j == posY){
                    ligne += "@"; 
                }else{
                    ligne += carte[i][j];
                }
            }
            println(ligne);
        }
    }

    void melangerTableau(String[] tableau){
        for(int i = tableau.length - 1; i > 0; i--){
            int index = (int)(Math.random() * (i + 1));
            String temp = tableau[index];
            tableau[index] = tableau[i];
            tableau[i] = temp;
        }
    }

    boolean estMort(Monstre m){
        return(m.pv <=0);
    }

    boolean estMort(Souris s){
        return(s.pv <=0);
    }

    void afficherPV(Souris s, Monstre m){
        println("\n");
        println("Vos PV : " + s.pv );
        println("PV du Monstre :" + m.pv);
        println("\n");
    }

    int combat(Souris s, String[][] carte, int nbmonstre, int etage) {
        afficherEnteteCombat();
        
        Monstre m = creerMonstre(etage);
        afficherPV(s, m);
        int difficulte;
        String d;
        m.degat = 4;
        while(!estMort(s) && !estMort(m)){
            do {
                println("Choisissez la difficulté de la question : \n1. Facile\n2. Moyen\n3. Difficile");
                println("Entrez un chiffre entre 1 et 3 :");
                
                try {
                    d = readString();
                    difficulte = Integer.parseInt(d);
                    
                    if (difficulte >= 1 && difficulte <= 3) {
                        s.degat = 3 + (2 * difficulte);
                        break;  // Sort de la boucle si l'entrée est valide
                    } else {
                        println("Veuillez entrer un nombre entre 1 et 3");
                    }
                } catch (NumberFormatException e) {
                    println("Erreur : Veuillez entrer un nombre valide");
                }
            } while (true);

            println("\n");
            println("Vos dégats :" + s.degat);
            println("\n");

            String[][] questionChoisie = tabQuestion(difficulte + ".csv");
            int indexQuestion = (int) (Math.random() * questionChoisie.length);
            String[] question = questionChoisie[indexQuestion];
        
            println(question[0]);
            String[] reponses = { question[1], question[2], question[3], question[4] };
            melangerTableau(reponses);
        
            for (int i = 0; i < reponses.length; i++) {
                println((i + 1) + ". " + reponses[i]);
            }
        
            int reponseUtilisateur;
            do {
                println("Quel numéro de réponse ? (entre 1 et 4)");
                reponseUtilisateur = readInt();
            } while (reponseUtilisateur > 4 || reponseUtilisateur < 1);
        
            if (reponses[reponseUtilisateur - 1].equals(question[1])) {
                text("green");
                println("Bravo ! Vous infligez des dégats ");
                carte[s.posX][s.posY] = " ";
                degatInfligés(s, m);
               
            } else {

                degatSubis(s, m);
                text("red");
                println("Mauvaise réponse ! Vous perdez des dégats");
                
                
            }
            text("white");
            afficherPV(s, m);
        }
        if(estMort(s)){
            text("red");
            println("Le monstre vous a battu ! Vous retenterez votre chance...");
            
        }else{
            text("vert");
            println("Bravo vous avez battu le monstre !");
            nbmonstre--;
        }
        s.pv = 15;
        text("white");
        return nbmonstre;
    }


    void degatSubis(Souris s, Monstre m){
        s.pv -= m.degat;
    }

    void degatInfligés(Souris s, Monstre m){
        m.pv -= s.degat;
    }
    
    void afficherEnteteCombat() {
        println("|------------------------------------- MODE COMBAT -----------------------------------|");
        println("|                                                                                     |");
        println("|      (\\__/)                                                                        |");
        println("|      ( •_•)             Vous venez d'entrer en mode combat !           @..@         |");
        println("|       > ^ <                                                           (____)        |");
        println("|       SOURIS                                                           <||>         |");
        println("|                                                                         vv          |");
        println("|-------------------------------------------------------------------------------------|");
    }
    


    void deplacementSouris(Souris s, String[][] carte) {
        String d;
        println("Que souhaitez-vous comme déplacement ? (Z, Q, S, D)");
        d = readString();
        
        if(equals(d, "")){
            println("*Déplacement impossible, essayez un autre*");
        }else if (equals(d, "d") && (equals(carte[s.posX][s.posY + 1], " ") 
                         || equals(carte[s.posX][s.posY + 1], "!")
                         || equals(carte[s.posX][s.posY + 1], "E"))) {
            s.posY += 1;
            println("*Déplacement vers la droite*");
        } else if (equals(d, "q") && (equals(carte[s.posX][s.posY - 1], " ") 
                                || equals(carte[s.posX][s.posY - 1], "!")
                                || equals(carte[s.posX][s.posY - 1], "E"))) {
            s.posY -= 1;
            println("*Déplacement vers la gauche*");
        } else if (equals(d, "s") && (equals(carte[s.posX + 1][s.posY], " ") 
                                || equals(carte[s.posX + 1][s.posY], "!")
                                || equals(carte[s.posX + 1][s.posY], "E"))) {
            s.posX += 1;
            println("*Déplacement vers le bas*");
        } else if (equals(d, "z") && (equals(carte[s.posX - 1][s.posY], " ") 
                                || equals(carte[s.posX - 1][s.posY], "!")
                                || equals(carte[s.posX - 1][s.posY], "E"))) {
            s.posX -= 1;
            println("*Déplacement vers le haut*");
        }else {
            println("*Déplacement impossible*");
        }
    }
    
    String[][] tabQuestion(String niveau){
        CSVFile fichier = loadCSV("../ressources/Q" + niveau);

        // Vérifier si le fichier est vide
        int rows = rowCount(fichier);
        int cols = columnCount(fichier);
        
        // Initialiser et remplir le tableau
        String[][] question = new String[rows][cols];
        for (int i = 0; i < question.length; i++) {
            for (int j = 0; j < question[0].length; j++) {
                question[i][j] = getCell(fichier, i, j);
            }
        }
        return question;
    }
    

    String[][] creationMap(String Etage){
        // Charger le fichier CSV
        CSVFile fichier = loadCSV("../ressources/Carte" + Etage);

        // Vérifier si le fichier est vide
        int rows = rowCount(fichier);
        int cols = columnCount(fichier);
        
        // Initialiser et remplir le tableau
        String[][] map = new String[rows][cols];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = getCell(fichier, i, j);
            }
        }
        return map;
    }

    void jouerEtage(int etage, String nom){
        println("#------------------------------------------------- Bienvenue dans l'étage numéro " + etage +" -------------------------------------------------#");
        Souris s = creerSouris(nom);
        String e = ""+etage;
        String[][] carte = creationMap(e+".csv");
        afficherCarte(carte, s.posX, s.posY);
        int nbmonstre = etage + 2;
        boolean stop = false;
        println("Nombre de monstre restant : " + nbmonstre);
        String[][] stat = new String[2][4];
        while(stop != true){
            stat[0][0] = "Position X";
            stat[0][1] = "Position Y";
            stat[0][2] = "Nombre de monstre";
            stat[0][3] = "Etage";
        
            stat[1][0] = "" + s.posX;
            stat[1][1] = "" + s.posY;
            stat[1][2] = "" + nbmonstre;
            stat[1][3] = "" + etage;
            println("\n" + "\n"  +"\n" +"\n" +"\n" +"\n" +"\n"+"\n" + "\n" +"\n" +"\n"+"\n" + "\n" +"\n" +"\n" +"\n" +"\n" +"\n" +"\n");
            println("");

            deplacementSouris(s, carte);
            if(equals(carte[s.posX][s.posY], "!")){
                nbmonstre = combat(s, carte, nbmonstre, etage);
                afficherCarte(carte, s.posX, s.posY);
            }else if(equals(carte[s.posX][s.posY],"E")){
                if (nbmonstre !=0) {
                    println("Il vous reste " + nbmonstre + " monstre à tué pour pouvoir sortir");
                }else{
                    println("|------------------------------------- MauScape --------------------------------------|");
                    println("|                                                                                     |");
                    println("|                                                                                     |");
                    println("|                Vous avez tué tout les monstres, vous pouvez sortir !                |");
                    println("|                                                                                     |");
                    println("|                                                                                     |");
                    println("|                                                                                     |");
                    println("|-------------------------------------------------------------------------------------|");
                    stop = true;
                }
            }else{
                println("Nombre de monstre restant : " + nbmonstre);
                afficherCarte(carte, s.posX, s.posY);
            }
    
        }
        println("Sauvegarde en cours");
        save(carte, stat);


    }


    void save(String[][] map, String[][] stat){
        saveCSV(map, "../ressources/SaveCarte.csv");
        saveCSV(stat, "../ressources/SaveStat.csv");
    }


        // Tests pour les fonctions de base
    void testEstMort() {
        Monstre m = new Monstre();
        m.pv = 0;
        assertTrue(estMort(m));
        
        m.pv = 10;
        assertFalse(estMort(m));
        
        Souris s = new Souris();
        s.pv = 0;
        assertTrue(estMort(s));
        
        s.pv = 15;
        assertFalse(estMort(s));
    }

    void testDegatSubis() {
        Souris s = new Souris();
        s.pv = 15;
        Monstre m = new Monstre();
        m.degat = 5;
        
        degatSubis(s, m);
        assertTrue(s.pv == 10);
    }

    void testDegatInfliges() {
        Souris s = new Souris();
        s.degat = 5;
        Monstre m = new Monstre();
        m.pv = 20;
        
        degatInfligés(s, m);
        assertTrue(m.pv == 15);
    }

    void testMelangerTableau() {
        String[] tableau = {"A", "B", "C", "D"};
        String[] original = tableau.clone();
        
        melangerTableau(tableau);
        
        // Vérifier que tous les éléments sont toujours présents
        boolean tousPresents = true;
        for (String s : original) {
            boolean trouve = false;
            for (String melange : tableau) {
                if (s.equals(melange)) {
                    trouve = true;
                    break;
                }
            }
            if (!trouve) {
                tousPresents = false;
                break;
            }
        }
        assertTrue(tousPresents);
    }

    void testCreationMap() {
        String[][] carte = creationMap("1.csv");
        assertTrue(carte != null);
        assertTrue(carte.length > 0);
        assertTrue(carte[0].length > 0);
        
        // Vérifier que la carte contient bien les éléments attendus
        boolean contientMur = false;
        boolean contientEspace = false;
        boolean contientMonstre = false;
        boolean contientSortie = false;
        
        for (int i = 0; i < carte.length; i++) {
            for (int j = 0; j < carte[0].length; j++) {
                if (carte[i][j].equals("#")) contientMur = true;
                if (carte[i][j].equals(" ")) contientEspace = true;
                if (carte[i][j].equals("!")) contientMonstre = true;
                if (carte[i][j].equals("E")) contientSortie = true;
            }
        }
        
        assertTrue(contientMur);
        assertTrue(contientEspace);
        assertTrue(contientMonstre);
        assertTrue(contientSortie);
    }

    void testTabQuestion() {
        String[][] questions = tabQuestion("1.csv");
        assertTrue(questions != null);
        assertTrue(questions.length > 0);
        assertTrue(questions[0].length == 5); // Question + 4 réponses
        
        // Vérifier que chaque ligne a bien une question et 4 réponses
        for (int i = 0; i < questions.length; i++) {
            assertTrue(questions[i][0] != null); // Question
            assertTrue(questions[i][1] != null); // Bonne réponse
            assertTrue(questions[i][2] != null); // Réponse 2
            assertTrue(questions[i][3] != null); // Réponse 3
            assertTrue(questions[i][4] != null); // Réponse 4
        }
    }

    void testDeplacementSouris() {
        Souris s = creerSouris("TestSouris");
        String[][] carte = {
            {"#", "#", "#", "#", "#"},
            {"#", " ", " ", " ", "#"},
            {"#", " ", " ", " ", "#"},
            {"#", " ", " ", "E", "#"},
            {"#", "#", "#", "#", "#"}
        };
        
        // Test déplacement vers la droite
        s.posX = 1;
        s.posY = 1;
        deplacementSouris(s, carte);
        assertTrue(s.posX == 1 && s.posY == 2);
        
        // Test collision avec un mur
        s.posX = 1;
        s.posY = 1;
        deplacementSouris(s, carte);
        assertFalse(s.posX == 0 && s.posY == 1);
    }

    void algorithm(){
        println("|------------------------------------- MauScape --------------------------------------|");
        println("|                                                                                     |");
        println("|                                                                                     |");
        println("|                            Bienvenue ! Quel est votre nom ?                         |");
        println("|                                                                                     |");
        println("|               (il est conseiller de mettre le terminal en pleine ecran)             |");
        println("|                                                                                     |");
        println("|-------------------------------------------------------------------------------------|");

        String nom = readString();
        if(equals(nom, "")){
            nom = "Bertrand";
            println("Bon, on t'appelera " + nom);
        }

        println("D'accord, alors "+ nom + " , tu es une petite souris (@) qui doit atteindre le grenier d'un manoir pour y retrouver son fromage !");
        println("Pour cela tu vas traverser plusieurs étages");
        println("Ton but est d'éléminer tout les monstres (!) sur tout passage, sinon, la porte ne pourra pas s'ouvrir...");

        println("Voici les commandes :");
        println("Z pour aller en haut");
        println("Q pour aller à gauche");
        println("S pour aller en bas");
        println("D pour aller à droite");

        println("\n" + "\n" +"\n" +"\n" +"\n" +"\n");

        println("Appuyez sur entrer");
        String enter = readString();  // Lecture initiale

        // On entre dans la boucle seulement si l'utilisateur n'a pas appuyé sur "Entrée"
        while (enter != null && !enter.isEmpty()) {
            println("Appuyez sur entrer");
            enter = readString();  // On lit à nouveau l'entrée
        }


        int etage = 1;
        while(etage <=3){
            jouerEtage(etage, nom);
            etage ++;
        }

        println("      .-\"\"\"\"\"\"-.");
        println("     /  ^  ^    \\");
        println("    /  (o)  (o)   \\");
        println("   :      ∆        :");
        println("    \\   '-...-'   /");
        println("     '-._______.-'");

        println("------------------------------------------------------");
        println("       🎉 Vous êtes dans le grenier ! 🎉              ");
        println("    Félicitations, vous avez terminé Mauscape !       ");
        println("------------------------------------------------------");
        println("            Merci d'avoir joué à Mauscape !          ");



        
    
    }
}