package Javacode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

/*
 * TP3 Hangman - Used with file as database.
 * 
 * Version : 1.9 Fully Working Version (FWV) (Working Game - Working Word Looker - Working Word Adder - Working Word Deleter - Code Optimisation - Variable Name reworked).
 * Creation date : 22-11-10
 * Last Modification : 22-11-15
 *  
 * By William Beaudin.
 */
public class Main {

    public static void main(String[] args) {

        while(true){ 

            Scanner input = new Scanner(System.in);
            //Variable Init

            String[] hangmanPrintArray; //Hangman Array
            char[] wordArray; //ChosenWord as array
            char[] letterAlreadyEnter = new char[26]; //Array to stock any letter enter by the user during the game
            hangmanPrintArray = new String[10]; //Initialize the hangman array length
            String chosenWord; //Choose word
            char letterInput; //Player input
            int tryLeft = 10; //Difficulty #number of try
            int countX; //Count for word verification

            //Populate hangman String Array with empty String.
            Arrays.fill(hangmanPrintArray, " ");

            while(true){ //Loop menu
                System.out.println("\nWelcome to the Hangman Game!\n");

                int x = printMenu();

                if(x == 1){
                    chosenWord = getRandomWordFromFile(chooseFileToRWord());
                    break;
                }
                else{
                    switch (x) {
                        case 2 -> wordAdderMenu();
                        case 3 -> wordDeleterMenu();
                        case 4 -> seeWordMenu();
                        case 5 -> {
                            System.out.println("Thanks for playing! See you soon :)");
                            System.exit(1);
                        }
                        default -> System.out.println("Error...");
                    }
                }
            }
            
            wordArray = new char[chosenWord.length()]; //Array for display
            //Populate wordArray
            Arrays.fill(wordArray, '_');


            System.out.println("\nThe choose word is " + chosenWord.length() + " letters long");
            System.out.println("\nYou have " + tryLeft + " try to find the word\n");

            printHangman(tryLeft, hangmanPrintArray);
            System.out.println();
            printWord(wordArray);

            while(true){

                countX = 0;

                System.out.print("\nEnter first letter : ");
                try {
                    letterInput = input.nextLine().charAt(0);
                } catch (Exception e) {
                    letterInput = ' ';
                }
                
                for (int i = 0; i < wordArray.length; i++) { //Put letter instead of '_' at right index if found in chosenWord
                    if(chosenWord.charAt(i) == letterInput) {
                        wordArray[i] = letterInput;
                    }
                    else{
                        countX++;
                    }
                }

                if(countX == wordArray.length){
                    tryLeft--;
                }

                if(!isLetterAlreadyInArray(letterAlreadyEnter, letterInput)) {
                    tryLeft++;
                    System.out.println("\nLetter already used!");
                    
                    if(countX != wordArray.length){
                        tryLeft--;
                    }
                }

                printHangman(tryLeft, hangmanPrintArray);
                System.out.println();
                
                if(tryLeft != 0){
                    System.out.println("You have " + tryLeft + " try left to find the word\n");
                    printWord(wordArray);
                }
                
                if(Arrays.equals(wordArray, chosenWord.toCharArray())) {
                    System.out.println("Congrats, you find the word!\n\n\n"); 
                    System.out.println("You're final score is : " + endGameScore(chosenWord, tryLeft));
                    break;
                }
                
                if(tryLeft == 0) {
                    System.out.println("You're out of chance to guess, nice try!");
                    System.out.println("The word was : " + chosenWord+ "\n\n\n");
                    break;
                }
            }
        }
    }

    //All function for the game / General function.

    static int endGameScore(String word, int difficulty){ //Score end game
        return word.length() * difficulty;
    }

    static boolean isLetterAlreadyInArray(char[] letterArray, char letterInput) {
        for (int i = 0; i < letterArray.length; i++) {
            if(letterArray[i] == letterInput) {
                return false;
            }
            else if(letterArray[i] == 0) {
                letterArray[i] = letterInput;
                return true;
            }
        }    
        return false;
    }
    
    static int printMenu(){ //Print menu
        Scanner scanner = new Scanner(System.in);
        char menuInput;

        while(true){
            System.out.println("***************Menu***************");
            System.out.println("*                                *");
            System.out.println("* Option #1 : New game           *");
            System.out.println("* Option #2 : Add Word           *");
            System.out.println("* Option #3 : Delete Word        *");
            System.out.println("* Option #4 : See word           *");
            System.out.println("* Option #5 : Leave game         *");
            System.out.println("*                                *");
            System.out.println("* Please Choose an option :      *" );
           

            try {
                menuInput = scanner.nextLine().charAt(0);

                if(Integer.parseInt(Character.toString(menuInput)) <= 5 && Integer.parseInt(Character.toString(menuInput)) >= 1){
                    break;
                }
                else{
                    System.out.println("Enter a valid number between 1 - 5");
                }
            }
            catch (Exception e) {
                System.out.println("Enter a integer");
            }
        }
        return Integer.parseInt(Character.toString(menuInput));
    }


    static int chooseFileToRWord(){
        Scanner scanner = new Scanner(System.in);
        char menuInput2;

        while (true) {
            System.out.println("New game... Loading... \n");

        System.out.println("Please select level difficulty.");
        System.out.println("We highly recommend you to choose between easy and medium as hard and apocalypse speak for themselves.");

        System.out.println("**************Difficulty**************");
            System.out.println("*                                *");
            System.out.println("* Option #1 : Easy               *");
            System.out.println("* Option #2 : Medium             *");
            System.out.println("* Option #3 : Hard               *");
            System.out.println("* Option #4 : Apocalypse         *");
            System.out.println("*                                *");
            System.out.println("* Please Choose an option :      *" );

            try {
                menuInput2 = scanner.nextLine().charAt(0);

                if(Integer.parseInt(Character.toString(menuInput2)) <= 4 && Integer.parseInt(Character.toString(menuInput2)) >= 1){
                    if(Integer.parseInt(Character.toString(menuInput2)) == 4){
                        System.out.println("\nGood luck mate!");
                    }
                    break;
                }
                else{
                    System.out.println("Enter a valid number between 1 - 5");
                }
            }
            catch (Exception e) {
                System.out.println("Enter a integer");
            }
        }
        return Integer.parseInt(Character.toString(menuInput2));
    }

    static String getRandomWordFromFile(int difficulty){

        int i = 0;
        String word=null;
        File myFile=null;

        switch (difficulty) {
            case 1 -> //6 characters max!
                    myFile = new File("../TP3/database/easy.txt");
            case 2 -> //8 characters max
                    myFile = new File("../TP3/database/medium.txt");
            case 3 -> //10 characters max
                    myFile = new File("../TP3/database/hard.txt");
            case 4 -> //More than 10 characters with max of 25 characters
                    myFile = new File("../TP3/database/Apocalypse.txt");
            default -> {
                System.out.println("Oops! Somethings went wrong... We'll investigate the error and comeback to you shortly.");
                System.exit(0);
            }
        }
            try {
                Scanner myReader = new Scanner(myFile);
                    while (myReader.hasNextLine()) {
                        i++;
                        myReader.nextLine();
                    }
                myReader.close();

                int j = (int)(Math.random()*i);

                try {
                    word = Files.readAllLines(Paths.get(myFile.toURI())).get(j); //J'AI CHERCHER LONGTEMPS POUR SA!!!!
                } 
                catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                System.out.println("An error occurred.");
            }
                

        return word;
    }

    static void printWord(char[] wordArray) { //Print Word
        for (char c : wordArray) {
            System.out.print(c + " ");
        }

        System.out.println();
    }

    static void printHangman(int tryLeft, String[]hP){ //Used to printHangman depending on how much try we have left.
        
        //hP = hangmanPart
        switch (tryLeft) { //Adjust Hangman depending on X value
            case 10:
                //Do nothing
                break;
            case 9:
                hP[0] = "^";
                break;
            case 8:
                hP[1] = "0";
                break;
            case 7:
                hP[3] = "|";
                break;
            case 6:
                hP[2] = "/";
                break;
            case 5:
                hP[4] = "\\";
                break;
            case 4:
                hP[5] = "|";
                break;
            case 3:
                hP[6] = "/";
                break;
            case 2:
                hP[7] = "\\";
                break;
            case 1:
                hP[8] = "-";
                break;
            case 0:
                hP[9] = "-";
                break;
            default:
                System.out.println("Error");
                break;
        }
        System.out.println("________");
        System.out.println("|       |");
        System.out.println("|       " + hP[0]);
        System.out.println("|       " + hP[1]);
        System.out.println("|      " + hP[2] + hP[3] + hP[4]);
        System.out.println("|       " + hP[5]);
        System.out.println("|      " + hP[6] + " " + hP[7]);
        System.out.println("|     " + hP[8] + " " + " " + " " + hP[9]);
    }

    static File returnFileToWordFunctions(String word){ //Return the file the program will use depending on the word length

        File myFile;
        String expression = "([a-zA-Z0-9]+)|(['()+,\\-.=]+)";

        if(word.isEmpty() || !word.matches(expression)){
            System.out.println("\nError... Word not valid\n");
            myFile = new File("../TP3/database/NotAccepted.txt");
        }
        else if(word.length() >= 12) { //Apocalypse
            myFile = new File("../TP3/database/Apocalypse.txt");
            
        }
        else if(word.length() <= 10 && word.length() > 8) { //Hard
            myFile = new File("../TP3/database/hard.txt");
            
        }
        else if(word.length() <= 8 && word.length() > 6) { //Medium
            myFile = new File("../TP3/database/medium.txt");
            
        }
        else if(word.length() <= 6 && word.length() > 1) { //Easy
            myFile = new File("../TP3/database/easy.txt");
            
        }
        else{
            System.out.println("Word not valid");
            return null;
        }
        return myFile;
    }

    //All function for option 2

    static void wordAdderMenu(){

        Scanner input = new Scanner(System.in);
        String rightWord;

        System.out.println("Hey, welcome to Word adder.");
        System.out.println("Tell me the word you want to add.");

        System.out.print("Word : ");

        rightWord = input.nextLine();

        System.out.println("We are sending you're word right away!");

        addWordFromUserInput(returnFileToWordFunctions(rightWord), rightWord);
    }
    
    static void addWordFromUserInput(File myFile, String word){
        Charset charset = StandardCharsets.UTF_8;
        try {
            FileWriter fWriter = new FileWriter(myFile , true);
            try (PrintWriter pWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(myFile, true), charset)))) {
                pWriter.println(word.toLowerCase());
                pWriter.close();
                fWriter.close();
            }
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    //All function for option 3

    static void wordDeleterMenu(){

        Scanner input = new Scanner(System.in);
        String rightWord;

        System.out.println("Hey, welcome to Word Deleter.");
        System.out.println("Tell me the word you want to delete.");

        System.out.print("Word : ");

        rightWord = input.nextLine();

        System.out.println("We are deleting you're word right away!");

        try {
            deleteWordFromUserInput(returnFileToWordFunctions(rightWord), rightWord);
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    static void deleteWordFromUserInput(File myFile, String word) throws Exception{
        File tempFile = new File("TempWordlist.txt");

        BufferedReader reader = new BufferedReader(new FileReader(myFile));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
        String currentLine;

        while((currentLine = reader.readLine()) != null){
            String trimmedLine = currentLine.trim();
            if(trimmedLine.equals(word)) continue;
            writer.write(currentLine + "\n");
        }
        reader.close();
        writer.close();
        myFile.delete();
        tempFile.renameTo(myFile);
    }

    //All function for option 4

    static void seeWordMenu(){
        System.out.println("\n\nHey! Welcome to the word looker");
        Scanner scanner = new Scanner(System.in);
        char menuInput3;

        while(true){

        System.out.println("*************WORD LOOKER**************");
            System.out.println("*                                *");
            System.out.println("* Option #1 : See Easy.txt       *");
            System.out.println("* Option #2 : See Medium.txt     *");
            System.out.println("* Option #3 : See Hard.txt       *");
            System.out.println("* Option #4 : See Apocalypse.txt *");
            System.out.println("* Option #5 : See All            *");
            System.out.println("*                                *");
            System.out.println("* Please Choose an option :      *" );
    
        try {
            menuInput3 = scanner.nextLine().charAt(0);

            if(Integer.parseInt(Character.toString(menuInput3)) <= 5 && Integer.parseInt(Character.toString(menuInput3)) >= 1){
                break;
            }
            else{
                System.out.println("Enter a valid number between 1 - 5");
            }
        }
        catch (Exception e) {
            System.out.println("Enter a integer");
        }
    }

        switch (menuInput3) {
            case '1' -> seeWordtxt(new File("../TP3/database/easy.txt"));
            case '2' -> seeWordtxt(new File("../TP3/database/medium.txt"));
            case '3' -> seeWordtxt(new File("../TP3/database/hard.txt"));
            case '4' -> seeWordtxt(new File("../TP3/database/Apocalypse.txt"));
            case '5' -> {
                System.out.println("***All Words***\n");
                System.out.println("\nEasy word : \n");
                seeWordtxt(new File("../TP3/database/easy.txt"));
                System.out.println("\nMedium word : \n");
                seeWordtxt(new File("../TP3/database/medium.txt"));
                System.out.println("\nHard word : \n");
                seeWordtxt(new File("../TP3/database/hard.txt"));
                System.out.println("\nApocalypse word : \n");
                seeWordtxt(new File("../TP3/database/Apocalypse.txt"));
                System.out.println("\n");
            }
            default -> seeWordMenu();
        }
    }

    static void seeWordtxt(File myFile) {

        int i = 0;
        String data;
        try {
            Scanner myReader = new Scanner(myFile);
            while (myReader.hasNextLine()) {
                i++;
                data = myReader.nextLine();
                System.out.println(i+" - "+data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error as occurred");
        }
    }
}