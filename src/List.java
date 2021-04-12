import java.util.Vector;
import java.util.concurrent.*;

public class List extends Thread{
    private Vector<Music> musics;

    public List(Vector<Music> musics){
        this.musics = musics;
    }

    @Override
    public void run() {
        for(int i = 0; i < musics.size(); i++) {
            System.out.println(i +": "+this.musics.get(i).getName());
        }
        if(musics.isEmpty()){
            System.out.println("LISTA DE REPRODUÇÃO VAZIA");
        }
    }
}
