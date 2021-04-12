import java.util.Vector;
import java.util.concurrent.*;

public class Add extends Thread{
    private Vector<Music> musics;
    private String name;
    private int duration;

    public Add(Vector<Music> $musicsList,String name, int duration) {
        this.musics = $musicsList;
        this.name = name;
        this.duration = duration;
    }

    @Override
    public void run() {
        this.musics.add(new Music(this.name, this.duration));
        System.out.println("ADDED");
    }
}
