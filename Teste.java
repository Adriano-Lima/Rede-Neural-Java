
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class Teste {

    public static void main(String args[]) throws IOException {
        /* "rhythmbox-client --play-pause"
        --next
        ----previous 
        --volume-up 
        --volume-down
        --mute 
        --unmute        
        */        
        Runtime.getRuntime().exec("rhythmbox /media/adriano/ARQUIVOS/Drive/Músicas/Musicas_1");
        //Runtime.getRuntime().exec("vlc N");

    }
}
