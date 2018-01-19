package pacman;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JPanel;

public class Plansza {
    private final int CZEKAJ_DUCH_CYKL=5;//opuźnienie duchów na "skrzyżowaniach"
    private final int SZEROKOSC_PLANSZA=19;//szerokosc planszy
    private final int WYSOKOSC_PLANSZA=22;//wysokosc planszy
    private final int ROZMIAR_GRAFIKA=15;//rozmiar obrazków w px
    private final int LICZBA_POZIOM=2;//liczba poziomów
    private final int[] PUNKTY_MAX={156,193};//liczba wszystkich punktów
    private int kierunekPacman;//kierunek poruszania się pacmana
    private int poziom;//numer planszy
    private int liczbaDuch;//liczba duchów
    private int pozycjaPacman;//początkowa pozycja gracza
    private int staraPozycjaPacman;//poprzednia pozycja gracza
    private int nastepnyKierunekPacman=5;//przechowuje nastepny kierunek Pacmana, 5-brak następnego kierunku
    private boolean start=false;//czy gra wystartowała
    public int gra=1;//stan gry|0-przegrana|1-gra|2-wygrana
    private int[] czekajDuch=new int[4];//przechowuje liczbe cykli, w których duch "zastanawiał się"
    private int[] spijDuch=new int[4];
    private int[] pozycjaDuch=new int[4];//pozycje duchów
    private int[] staraPozycjaDuch=new int[4];//poprzednie pozycje duchów
    private int[] kierunekDuch=new int[4];//kierunek poruszania się duchów
    private int[] historiaDuch=new int[4];//typy pól, na którch są duchy
    private int[] tempDuch=new int[4];//tablica pomocnicza do sprawdzania możliwych dróg dla duchów
    private int[] tempPacman=new int[4];//tablica pomocnicza do sprawdzania możliwych dróg dla Pacmana
    private int[][] Plansza=new int[][]{//plansze
        new int[]{//poziom 1
            17,11,11,11,11,11,11,11,11,22,11,11,11,11,11,11,11,11,18,
            10,8,8,8,8,8,8,8,8,10,8,8,8,8,8,8,8,8,10,
            10,8,17,18,8,17,11,18,8,10,8,17,11,18,8,17,18,8,10,
            10,8,16,19,8,16,11,19,8,14,8,16,11,19,8,16,19,8,10,
            10,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,10,
            10,8,15,13,8,12,8,15,11,22,11,13,8,12,8,15,13,8,10,
            10,8,8,8,8,10,8,8,8,10,8,8,8,10,8,8,8,8,10,
            16,11,11,18,8,21,11,13,9,14,9,15,11,23,8,17,11,11,19,
            9,9,9,10,8,10,9,9,9,9,9,9,9,10,8,10,9,9,9,
            11,11,11,19,8,14,9,17,13,9,15,18,9,14,8,16,11,11,11,
            9,9,9,9,8,9,9,10,9,9,9,10,9,9,8,9,9,9,9,
            11,11,11,18,8,12,9,16,11,11,11,19,9,12,8,17,11,11,11,
            9,9,9,10,8,10,9,9,9,9,9,9,9,10,8,10,9,9,9,
            17,11,11,19,8,14,9,15,11,22,11,13,9,14,8,16,11,11,18,
            10,8,8,8,8,8,8,8,8,10,8,8,8,8,8,8,8,8,10,
            10,8,15,18,8,15,11,13,8,14,8,15,11,13,8,17,11,8,10,
            10,8,8,10,8,8,8,8,8,9,8,8,8,8,8,10,8,8,10,
            21,13,8,14,8,12,8,15,11,22,11,13,8,12,8,14,8,15,23,
            10,8,8,8,8,10,8,8,8,10,8,8,8,10,8,8,8,8,10,
            10,8,15,11,11,20,11,13,8,14,8,15,11,20,11,11,13,8,10,
            10,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,8,10,
            16,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,11,19
        },
        new int[]{//poziom 2
            17,11,11,11,11,22,11,11,11,11,11,11,11,11,22,11,11,11,18,
            10,8,8,8,8,10,8,8,8,8,8,8,8,8,10,8,8,8,10,
            10,8,17,13,8,14,8,15,11,11,11,11,13,8,14,8,12,8,10,
            10,8,10,8,8,8,8,8,8,8,8,8,8,8,8,8,10,8,10,
            10,8,14,8,15,11,13,8,17,11,11,18,8,15,11,11,19,8,10,
            10,8,8,8,8,8,8,8,10,9,9,10,8,8,8,8,8,8,10,
            10,8,12,8,15,11,13,8,14,9,15,19,8,15,11,11,18,8,10,
            10,8,10,8,8,8,8,8,8,8,8,8,8,8,8,8,10,8,10,
            10,8,16,13,8,12,8,15,11,11,11,11,13,8,12,8,14,8,10,
            10,8,8,8,8,10,8,8,8,8,8,8,8,8,10,8,8,8,10,
            16,11,11,11,11,23,8,17,11,11,11,11,18,8,21,11,11,11,19,
            17,11,11,11,11,23,8,16,11,11,11,11,19,8,21,11,11,11,18,
            10,8,8,8,8,10,8,8,8,8,8,8,8,8,10,8,8,8,10,
            10,8,17,13,8,14,8,15,11,11,11,11,13,8,14,8,12,8,10,
            10,8,10,8,8,8,8,8,8,8,8,8,8,8,8,8,10,8,10,
            10,8,14,8,15,11,13,8,17,13,9,12,8,15,11,11,19,8,10,
            10,8,8,8,8,8,8,8,10,9,9,10,8,8,8,8,8,8,10,
            10,8,12,8,15,11,13,8,16,11,11,19,8,15,11,11,18,8,10,
            10,8,10,8,8,8,8,8,8,8,8,8,8,8,8,8,10,8,10,
            10,8,16,13,8,12,8,15,11,11,11,11,13,8,12,8,14,8,10,
            10,8,8,8,8,10,8,8,8,8,8,8,8,8,10,8,8,8,10,
            16,11,11,11,11,20,11,11,11,11,11,11,11,11,20,11,11,11,19
        }
    };
    
    private String[] nazwy={//tablica z nazwami obrazków
        "res/pacman/pacmanW.jpg", //kod 0
        "res/pacman/pacmanD.jpg", //kod 1
        "res/pacman/pacmanS.jpg", //kod 2
        "res/pacman/pacmanA.jpg", //kod 3
        "res/duch/duch1.jpg", //kod 4
        "res/duch/duch2.jpg", //kod 5
        "res/duch/duch3.jpg", //kod 6
        "res/duch/duch4.jpg", //kod 7
        "res/tlo/tlo1.jpg", //kod 8, tło z punktem
        "res/tlo/tlo2.jpg", //kod 9, puste tło
        "res/sciana/sciana1.jpg", //kod 10
        "res/sciana/sciana2.jpg", //kod 11
        "res/sciana/sciana3.jpg", //kod 12
        "res/sciana/sciana4.jpg", //kod 13
        "res/sciana/sciana5.jpg", //kod 14
        "res/sciana/sciana6.jpg", //kod 15
        "res/sciana/sciana7.jpg", //kod 16
        "res/sciana/sciana8.jpg", //kod 17
        "res/sciana/sciana9.jpg", //kod 18
        "res/sciana/sciana10.jpg", //kod 19
        "res/sciana/sciana11.jpg", //kod 20
        "res/sciana/sciana12.jpg", //kod 21
        "res/sciana/sciana13.jpg", //kod 22
        "res/sciana/sciana14.jpg", //kod 23
        "res/sciana/sciana15.jpg" //kod 24
    };
    private Image obrazy[]=new Image[nazwy.length];//tablica z obrazami
    
    public Plansza(){//konstruktor klasy Plansza
        for(int i=0;i<nazwy.length;i++){//pobieranie obrazków do tablicy
            obrazy[i]=Toolkit.getDefaultToolkit().getImage(nazwy[i]);
        }
        startGra();
        ustawPlansza();
    }
    private void ustawPlansza(){//ustawia pacmana i duchy na planszy
        Plansza[poziom][pozycjaDuch[0]]=4;
        Plansza[poziom][pozycjaDuch[1]]=5;
        Plansza[poziom][pozycjaDuch[2]]=6;
        Plansza[poziom][pozycjaDuch[3]]=7;
        Plansza[poziom][pozycjaPacman]=kierunekPacman;
    }
    private void zmienPlansza()//aktualizuje obrazek pacmana na planszy
    {   
        if(staraPozycjaPacman==pozycjaPacman){
            return;
        }
        if(Plansza[poziom][pozycjaPacman]>=10){
            pozycjaPacman=staraPozycjaPacman;
            return;
        }
        if(Plansza[poziom][pozycjaPacman]==8 
                || Plansza[poziom][pozycjaPacman]==9){
            Plansza[poziom][staraPozycjaPacman]=9;
            Plansza[poziom][pozycjaPacman]=kierunekPacman;
        }
    }
    private void zmienPlanszaDuch(int i){//aktualizuje obrazek ducha na planszy
        if(staraPozycjaDuch[i]==pozycjaDuch[i]){
            return;
        }
        if(Plansza[poziom][pozycjaDuch[i]]>=10){
            pozycjaDuch[i]=staraPozycjaDuch[i];
            zmienKierunekDuch(i);
            return;
        }
        if(Plansza[poziom][pozycjaDuch[i]]==8
                || Plansza[poziom][pozycjaDuch[i]]==9){
            Plansza[poziom][staraPozycjaDuch[i]]=historiaDuch[i];
            boolean test=true;
            for(int j=0;j<i;j++){
                if(Plansza[poziom][pozycjaDuch[i]]==Plansza[poziom][pozycjaDuch[j]] && i!=j){
                    if(Plansza[poziom][pozycjaDuch[i]]>=4 || Plansza[poziom][pozycjaDuch[i]]<=8){
                        historiaDuch[i]=historiaDuch[j];
                    }
                    test=false;
                }
            }
            if(test){
                historiaDuch[i]=Plansza[poziom][pozycjaDuch[i]];
            }
            Plansza[poziom][pozycjaDuch[i]]=4+i;
            int j,n;
            n=0;
            sprawdzKierunekDuch(i);
            for(j=0;j<4;j++){
                if(tempDuch[j]==1){
                    n++;
                }
            }
            if(n>2){
                zmienKierunekDuch(i);
            }
        }
    }
    public void rysujPlansza(Graphics2D g,JPanel panel){//rysowanie planszy
        zmienPlansza();
        for(int i=0;i<liczbaDuch;i++){
            zmienPlanszaDuch(i);
        }
        for(int i=0;i<SZEROKOSC_PLANSZA;i++){//Rysowanie obrazów na planszy po zmianie pozycji gracza
            for(int j=0;j<WYSOKOSC_PLANSZA;j++){
                g.drawImage(obrazy[Plansza[poziom][i+SZEROKOSC_PLANSZA*j]],i*ROZMIAR_GRAFIKA,j*ROZMIAR_GRAFIKA,panel);
            }
        }
    }
    public void zmnienKierunekPacman(int nowyKierunekPacman){//zmienia kierunek poruszania sie pacmana po naciśnięciu klawiszy sterowania
        switch(nowyKierunekPacman){
            case 0:
                if(Plansza[poziom][pozycjaPacman-SZEROKOSC_PLANSZA]==8
                        || Plansza[poziom][pozycjaPacman-SZEROKOSC_PLANSZA]==9){
                    kierunekPacman=0;
                    if(!start){
                        start=true;
                    }
                }
                else{
                    nastepnyKierunekPacman=0;
                }
                break;
            case 1:
                if(Plansza[poziom][pozycjaPacman+1]==8
                        || Plansza[poziom][pozycjaPacman+1]==9){
                    kierunekPacman=1;
                    if(!start){
                        start=true;
                    }
                }
                else{
                    nastepnyKierunekPacman=1;
                }
                break;
            case 2:
                if(Plansza[poziom][pozycjaPacman+SZEROKOSC_PLANSZA]==8
                        || Plansza[poziom][pozycjaPacman+SZEROKOSC_PLANSZA]==9){
                    kierunekPacman=2;
                    if(!start){
                        start=true;
                    }
                }
                else{
                    nastepnyKierunekPacman=2;
                }
                break;
            case 3:
                if(Plansza[poziom][pozycjaPacman-1]==8
                        || Plansza[poziom][pozycjaPacman-1]==9){
                        kierunekPacman=3;
                    if(!start){
                        start=true;
                    }
                }
                else{
                    nastepnyKierunekPacman=3;
                }
                break;
        }
        Plansza[poziom][pozycjaPacman]=kierunekPacman;
    }
    public void update(){//aktualizacja gry, koleiny cykl
        if(start){
            sprawdzPunkty();
            sprawdzPrzegrana();
            czyZmienicKierunekPacman();
            przesunPacman();
            switch(liczbaDuch){
                case 4:
                    przesunDuch(3);
                case 3:
                    przesunDuch(2);
                case 2:
                    przesunDuch(1);
                case 1:
                    przesunDuch(0);
            }
        }
    }
    private void czyZmienicKierunekPacman(){
        sprawdzKierunekPacman();
        int n=0;
        for(int j=0;j<4;j++){//zlicza możliwe drogi Pacmana
            if(tempPacman[j]==1){
                n++;
            }
        }
        if(n>2){//jeśli "skrzyżowanie"
            if(nastepnyKierunekPacman!=5){
                switch(nastepnyKierunekPacman){
                    case 0:
                        if(Plansza[poziom][pozycjaPacman-SZEROKOSC_PLANSZA]==8
                                || Plansza[poziom][pozycjaPacman-SZEROKOSC_PLANSZA]==9){
                            kierunekPacman=0;
                            nastepnyKierunekPacman=5;
                        }
                        break;
                    case 1:
                        if(Plansza[poziom][pozycjaPacman+1]==8
                                || Plansza[poziom][pozycjaPacman+1]==9){
                            kierunekPacman=1;
                            nastepnyKierunekPacman=5;
                        }
                        break;
                    case 2:
                        if(Plansza[poziom][pozycjaPacman+SZEROKOSC_PLANSZA]==8
                                || Plansza[poziom][pozycjaPacman+SZEROKOSC_PLANSZA]==9){
                            kierunekPacman=2;
                            nastepnyKierunekPacman=5;
                        }
                        break;
                    case 3:
                        if(Plansza[poziom][pozycjaPacman-1]==8
                                || Plansza[poziom][pozycjaPacman-1]==9){
                            kierunekPacman=3;
                            nastepnyKierunekPacman=5;
                        }
                        break;
                }
            }
        }
    }
    private void przesunPacman(){//zmienia pozycja pacmana zgodnie z jego kierunkiem poruszania
        switch(kierunekPacman){
            case 0:
                staraPozycjaPacman=pozycjaPacman;
                pozycjaPacman-=SZEROKOSC_PLANSZA;
                break;
            case 1:
                staraPozycjaPacman=pozycjaPacman;
                if(pozycjaPacman==208 && poziom==0){//teleport
                    pozycjaPacman-=SZEROKOSC_PLANSZA;   
                }
                pozycjaPacman+=1;
                break;
            case 2:
                staraPozycjaPacman=pozycjaPacman;
                pozycjaPacman+=SZEROKOSC_PLANSZA;
                break;
            case 3:
                staraPozycjaPacman=pozycjaPacman;
                if(pozycjaPacman==190 && poziom==0){//teleport
                    pozycjaPacman+=SZEROKOSC_PLANSZA;
                }
                pozycjaPacman-=1;
                break;
        }
    }
    private void przesunDuch(int i){//zmienia pozycje wybranego ducha zgodnie z jego kierunkiem poruszania
        int n,j;
        sprawdzKierunekDuch(i);//sprawdza możliwe drogi
        n=0;
        for(j=0;j<4;j++){//zlicza możliwe drogi
            if(tempDuch[j]==1){
                n++;
            }
        }
        if(n>2){//jesli "skrzyowanie"
            if(czekajDuch[i]==CZEKAJ_DUCH_CYKL){//duch "zastanawia się", którą drogę wybrać
                switch(kierunekDuch[i]){
                    case 0:
                        staraPozycjaDuch[i]=pozycjaDuch[i];
                        pozycjaDuch[i]-=SZEROKOSC_PLANSZA;
                        break;
                    case 1:
                        staraPozycjaDuch[i]=pozycjaDuch[i];
                        pozycjaDuch[i]+=1;
                        break;
                    case 2:
                        staraPozycjaDuch[i]=pozycjaDuch[i];
                        pozycjaDuch[i]+=SZEROKOSC_PLANSZA;
                        break;
                    case 3:
                        staraPozycjaDuch[i]=pozycjaDuch[i];
                        pozycjaDuch[i]-=1;
                        break;
                }
                czekajDuch[i]=0;
            }
            else{
                czekajDuch[i]++;
            }
        }
        else{
            switch(kierunekDuch[i]){
                case 0:
                    staraPozycjaDuch[i]=pozycjaDuch[i];
                    pozycjaDuch[i]-=SZEROKOSC_PLANSZA;
                    break;
                case 1:
                    staraPozycjaDuch[i]=pozycjaDuch[i];
                    pozycjaDuch[i]+=1;
                    break;
                case 2:
                    staraPozycjaDuch[i]=pozycjaDuch[i];
                    pozycjaDuch[i]+=SZEROKOSC_PLANSZA;
                    break;
                case 3:
                    staraPozycjaDuch[i]=pozycjaDuch[i];
                    pozycjaDuch[i]-=1;
                    break;
            }
        }
    }
    private void zmienKierunekDuch(int i){//analizuje i wybiera najlepszy z możliwych kierunków dla wybranego ducha w zależności od pozycji pacmana
        sprawdzKierunekDuch(i);
        int n,j,x,y;
        x=Math.abs(pozycjaPacman%SZEROKOSC_PLANSZA-pozycjaDuch[i]%SZEROKOSC_PLANSZA);
        y=Math.abs((int)(pozycjaPacman/SZEROKOSC_PLANSZA)-(int)(pozycjaDuch[i]/SZEROKOSC_PLANSZA));
        for(j=0,n=0;j<4;j++){
            if(tempDuch[j]==1){
                n++;
            }
        }
        if(pozycjaPacman<pozycjaDuch[i]){
            if(tempDuch[2]!=0){
                tempDuch[2]=0;
                n--;
            }
        }else{
            if(tempDuch[0]!=0){
                tempDuch[0]=0;
                n--;
            }
        }
        if(n>1){
            if(pozycjaPacman%SZEROKOSC_PLANSZA<pozycjaDuch[i]%SZEROKOSC_PLANSZA){
                if(tempDuch[1]!=0){
                    tempDuch[1]=0;
                    n--;
                }
            }else{
                if(tempDuch[3]!=0){
                    tempDuch[3]=0;
                    n--;
                }
            }
        }
        if(x==0){
            if(tempDuch[3]==1 && n>1){
                tempDuch[3]=0;
                n--;
            }
            if(tempDuch[1]==1 && n>1){
                tempDuch[1]=0;
                n--;
            }
        }
        if(y==0){
            if(tempDuch[0]==1 && n>1){
                tempDuch[0]=0;
                n--;
            }
            if(tempDuch[2]==1 && n>1){
                tempDuch[2]=0;
                n--;
            }
        }
        for(j=0;j<4;j++){
            if(tempDuch[j]==1){
                kierunekDuch[i]=j;
                return;
            }
        }
    }
    private void sprawdzKierunekDuch(int i){//sprawdza możliwe kierunki dla wybranego ducha
        for(int j=0;j<4;j++){
            tempDuch[j]=0;
        }
        if(Plansza[poziom][pozycjaDuch[i]-SZEROKOSC_PLANSZA]<10){//jeśli pole wyżej nie jest przeszkodą, ustaw 1
            tempDuch[0]=1;
        }
        if(Plansza[poziom][pozycjaDuch[i]+1]<10){//jeśli pole w prawo nie jest przeszkodą, ustaw 1
            tempDuch[1]=1;
        }
        if(Plansza[poziom][pozycjaDuch[i]+SZEROKOSC_PLANSZA]<10){//jeśli pole niżej nie jest przeszkodą, ustaw 1
            if(pozycjaDuch[i]!=161){//nie można wejść do domku duchów
                tempDuch[2]=1;
            }
        }
        if(Plansza[poziom][pozycjaDuch[i]-1]<10){//jeśli pole w lewo nie jest przeszkodą, ustaw 1
            tempDuch[3]=1;
        }  
    }
    private void sprawdzKierunekPacman(){//sprawdza możliwe kierunki dla Pacmana
        for(int j=0;j<4;j++){
            tempPacman[j]=0;
        }
        if(Plansza[poziom][pozycjaPacman-SZEROKOSC_PLANSZA]<10){//jeśli pole wyżej nie jest przeszkodą, ustaw 1
            tempPacman[0]=1;
        }
        if(Plansza[poziom][pozycjaPacman+1]<10){//jeśli pole w prawo nie jest przeszkodą, ustaw 1
            tempPacman[1]=1;
        }
        if(Plansza[poziom][pozycjaPacman+SZEROKOSC_PLANSZA]<10){//jeśli pole niżej nie jest przeszkodą, ustaw 1
            if(pozycjaPacman!=161){//nie można wejść do domku duchów
                tempPacman[2]=1;
            }
        }
        if(Plansza[poziom][pozycjaPacman-1]<10){//jeśli pole w lewo nie jest przeszkodą, ustaw 1
            tempPacman[3]=1;
        }  
    }
    private void sprawdzPrzegrana(){//sprawdza "sptkanie" pacmana z duchami
        for(int i=0;i<liczbaDuch;i++){
            if(pozycjaPacman==pozycjaDuch[i]){//przegrana
               gra=0;
            }
        }
    }
    private void sprawdzPunkty(){//sprawdza pozostałe punkty do zebrania
        int i;
        double pkt=0;
        for(i=0;i<SZEROKOSC_PLANSZA*WYSOKOSC_PLANSZA;i++){//zlicza punkty
            if(Plansza[poziom][i]==8){//kod pola z punktem
                pkt++;
            }
        }
        int[] temp={0,0,0,0};//tablica pomocnicza przechowująca pozycje duchów
        for(i=0;i<4;i++){//sprawdza, czy duch jest na polu z punktem
            temp[i]=pozycjaDuch[i];
            boolean innaPozycja=true;
            for(int j=0;j<4;j++){
                if(pozycjaDuch[i]==temp[j] && i!=j){
                    innaPozycja=false;
                }
            }
            if(innaPozycja){
                pkt++;
            }
        }
        if(liczbaDuch<4){
            switch(liczbaDuch){//zwiększa liczbę duchów, gdy:
                case 1:
                    if(pkt/PUNKTY_MAX[poziom]<=0.75){//pozostało 75% punktów do zebrania
                        liczbaDuch++;
                    }
                    break;
                case 2:
                    if(pkt/PUNKTY_MAX[poziom]<=0.5){//pozostało 75% punktów do zebrania
                        liczbaDuch++;
                    }
                    break;
                case 3:
                    if(pkt/PUNKTY_MAX[poziom]<=0.25){//pozostało 75% punktów do zebrania
                        liczbaDuch++;
                    }
                    break;
            }
        }
        if(pkt==0){//wygrana, jeśli zebrano wszystkie punkty
            if(poziom+1==LICZBA_POZIOM){
                gra=3;
                start=false;
            }else{
                gra=2;
                start=false;
            }
        }
    }
    private void startGra(){//nadaje początkowe wartości
        poziom=0;
        liczbaDuch=1;
        kierunekPacman=1;
        pozycjaPacman=313;
        staraPozycjaPacman=313;
        pozycjaDuch[0]=180;
        pozycjaDuch[1]=199;
        pozycjaDuch[2]=198;
        pozycjaDuch[3]=200;
        staraPozycjaDuch[0]=180;
        staraPozycjaDuch[1]=199;
        staraPozycjaDuch[2]=198;
        staraPozycjaDuch[3]=200;
        kierunekDuch[0]=0;
        kierunekDuch[1]=0;
        kierunekDuch[2]=1;
        kierunekDuch[3]=3;
        historiaDuch[0]=9;
        historiaDuch[1]=9;
        historiaDuch[2]=9;
        historiaDuch[3]=9;
        czekajDuch[0]=0;
        czekajDuch[1]=0;
        czekajDuch[2]=0;
        czekajDuch[3]=0;
    }
    public void nastepnyPoziom(){//ustawia następny poziom
        if(poziom+1<LICZBA_POZIOM){
            poziom++;
            wczytajNastepnyPoziom();
        }
        else{
            gra=3;
        }
    }
    private void wczytajNastepnyPoziom(){
        switch(poziom){
            case 1:
                liczbaDuch=1;
                kierunekPacman=1;
                pozycjaPacman=235;
                staraPozycjaPacman=235;
                pozycjaDuch[0]=104;
                pozycjaDuch[1]=105;
                pozycjaDuch[2]=314;
                pozycjaDuch[3]=313;
                staraPozycjaDuch[0]=104;
                staraPozycjaDuch[1]=105;
                staraPozycjaDuch[2]=314;
                staraPozycjaDuch[3]=313;
                kierunekDuch[0]=2;
                kierunekDuch[1]=3;
                kierunekDuch[2]=0;
                kierunekDuch[3]=1;
                historiaDuch[0]=9;
                historiaDuch[1]=9;
                historiaDuch[2]=9;
                historiaDuch[3]=9;
                czekajDuch[0]=0;
                czekajDuch[1]=0;
                czekajDuch[2]=0;
                czekajDuch[3]=0;
                ustawPlansza();
                break;
        }     
    }
}
