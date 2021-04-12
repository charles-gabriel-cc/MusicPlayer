import sun.awt.windows.ThemeReader;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MusicPlayer extends JFrame {

    private String playlistText = "";

    private JLabel labelFileName = new JLabel("Playing Music:");
    private JLabel labelTimeCounter = new JLabel("00:00:00");
    private JLabel labelDuration = new JLabel("00:00:00");

    private JButton buttonPlay = new JButton("Play");
    private JButton buttonStop = new JButton("Stop");
    private JButton buttonPause = new JButton("Pause");
    private JButton buttonNext = new JButton("Next");
    private JButton buttonPrevious = new JButton("Previous");

    private JButton buttonAdd = new JButton("Add");
    private JTextField textAdd = new JTextField();

    private JButton buttonRemove = new JButton("Remove");
    private JTextField textRemove = new JTextField();

    private JSlider sliderTime = new JSlider();

    private JTextArea musicasDisponiveis = new JTextArea();
    private JScrollPane disponiveisScroll = new JScrollPane();
    private JLabel legend1 = new JLabel();

    private JTextArea playlist = new JTextArea();
    private JScrollPane playlistScroll = new JScrollPane();
    private JLabel legend2 = new JLabel();

    // Arquitetura

    private Player player = new Player();
    private Thread playbackThread;
    private Thread controlThread;
    private Timer timer;
    private boolean isPlaying = false;
    private boolean isPause = false;
    private boolean hasStarted = false;
    private boolean needPrev = false;

    Music music1 = new Music("0 - Music0", 20);
    Music music2 = new Music("1 - Music1", 90);
    Music music3 = new Music("2 - Music2", 100);
    Music music4 = new Music("3 - Music3", 210);
    Music music5 = new Music("4 - Music4", 160);

    Vector<Music> playlistVector = new Vector<Music>(1);
    private Vector<Music> disponiveisVector = new Vector<Music>(1);

    public void setDisponiveisVector(Vector<Music> disponiveisVector) {
        this.disponiveisVector = disponiveisVector;
    }

    public MusicPlayer() {
        super("MusicPlayer");
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.anchor = GridBagConstraints.WEST;

        Vector<Music> aux = new Vector<Music>(1);
        aux.add(music1);
        aux.add(music2);
        aux.add(music3);
        aux.add(music4);
        aux.add(music5);
        setDisponiveisVector(aux);

        buttonPlay.setFont(new Font("Sans", Font.BOLD, 14));
        buttonPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPlayAction(evt);
            }
        });

        buttonStop.setFont(new Font("Sans", Font.BOLD, 14));
        buttonStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStopAction(evt);
            }
        });
        buttonStop.setEnabled(false);

        buttonPause.setFont(new Font("Sans", Font.BOLD, 14));
        buttonPause.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPauseAction(evt);
            }
        });
        buttonPause.setEnabled(false);

        buttonNext.setFont(new Font("Sans", Font.BOLD, 14));
        buttonNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonNextAction(evt);
            }
        });
        buttonNext.setEnabled(false);

        buttonPrevious.setFont(new Font("Sans", Font.BOLD, 14));
        buttonPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPreviousAction(evt);
            }
        });
        buttonPrevious.setEnabled(false);

        buttonAdd.setFont(new Font("Sans", Font.BOLD, 14));
        buttonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAddAction(evt);
            }
        });

        buttonRemove.setFont(new Font("Sans", Font.BOLD, 14));
        buttonRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRemoveAction(evt);
            }
        });

        labelTimeCounter.setFont(new Font("Sans", Font.BOLD, 12));
        labelDuration.setFont(new Font("Sans", Font.BOLD, 12));

        sliderTime.setPreferredSize(new Dimension(400, 20));
        sliderTime.setEnabled(true);
        sliderTime.setValue(0);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        add(labelFileName, constraints);

        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        add(labelTimeCounter, constraints);

        constraints.gridx = 1;
        add(sliderTime, constraints);

        constraints.gridx = 2;
        add(labelDuration, constraints);

        musicasDisponiveis.setEditable(false);
        musicasDisponiveis.setColumns(20);
        musicasDisponiveis.setRows(5);
        disponiveisScroll.setViewportView(musicasDisponiveis);

        musicasDisponiveis.append(music1.getName() + "\n");
        musicasDisponiveis.append(music2.getName() + "\n");
        musicasDisponiveis.append(music3.getName() + "\n");
        musicasDisponiveis.append(music4.getName() + "\n");
        musicasDisponiveis.append(music5.getName() + "\n");

        playlist.setEditable(false);
        playlist.setColumns(20);
        playlist.setRows(5);
        playlistScroll.setViewportView(playlist);

        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 100));

        JPanel buttonPanel1 = new JPanel();
        BoxLayout boxLayout1 = new BoxLayout(buttonPanel1, BoxLayout.Y_AXIS);
        buttonPanel1.setLayout(boxLayout1);
        buttonPanel1.add(Box.createVerticalGlue());
        buttonPanel1.add(buttonPlay);
        buttonPanel1.add(buttonPause);
        panelButtons.add(buttonPanel1);

        JPanel buttonPanel3 = new JPanel();
        BoxLayout boxLayout3 = new BoxLayout(buttonPanel3, BoxLayout.Y_AXIS);
        buttonPanel3.setLayout(boxLayout3);
        buttonPanel3.add(Box.createVerticalGlue());
        buttonPanel3.add(buttonNext);
        buttonPanel3.add(buttonPrevious);
        panelButtons.add(buttonPanel3);

        JPanel buttonPanel4 = new JPanel();
        BoxLayout boxLayout4 = new BoxLayout(buttonPanel4, BoxLayout.Y_AXIS);
        buttonPanel4.setLayout(boxLayout4);
        buttonPanel4.add(Box.createVerticalGlue());
        buttonPanel4.add(buttonAdd);
        buttonPanel4.add(textAdd);
        textAdd.setText("Choose the number of a song");//Digite aqui
        legend1.setText("Available songs:");
        buttonPanel4.add(legend1);
        buttonPanel4.add(disponiveisScroll);
        panelButtons.add(buttonPanel4);

        JPanel buttonPanel5 = new JPanel();
        BoxLayout boxLayout5 = new BoxLayout(buttonPanel5, BoxLayout.Y_AXIS);
        buttonPanel5.setLayout(boxLayout5);
        buttonPanel5.add(Box.createVerticalGlue());
        buttonPanel5.add(buttonRemove);
        buttonPanel5.add(textRemove);
        textRemove.setText("Choose the number of a song");// Digite aqui
        legend2.setText("Playlist:");
        buttonPanel5.add(legend2);
        buttonPanel5.add(playlistScroll);
        panelButtons.add(buttonPanel5);

        constraints.gridwidth = 3;
        constraints.gridx = 0;
        constraints.gridy = 2;
        add(panelButtons, constraints);

        pack();
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void playBack() {
        buttonStop.setEnabled(true);
        buttonPlay.setEnabled(false);
        buttonNext.setEnabled(true);
        buttonPrevious.setEnabled(true);
        isPlaying = true;
        timer = new Timer(labelTimeCounter, sliderTime, labelFileName, labelDuration);
        playbackThread = new Thread(new Runnable() {
            @Override
            public void run() {
                buttonPause.setEnabled(true);
                player.load(playlistVector);
                labelFileName.setText("Playing now: " + playlistVector.get(0).getName());
                sliderTime.setMaximum((int) player.getClipSecondLength(0));
                labelDuration.setText(player.getClipLengthString(0));
                try {
                    player.play(timer);
                } catch (IOException ex) {
                    Logger.getLogger(MusicPlayer.class.getName()).log(Level.SEVERE, null, ex);
                }
                resetControls();
            }
        });

        playbackThread.start();
    }

    private void resetControls() {
        buttonPause.setEnabled(false);
        buttonNext.setEnabled(false);
        buttonPrevious.setEnabled(false);
        buttonPlay.setEnabled(true);
        buttonStop.setEnabled(false);
        isPlaying = false;
    }

    private void stopPlaying() {
        isPause = false;
        isPlaying = false;
        buttonPause.setEnabled(false);
        buttonNext.setEnabled(false);
        buttonPrevious.setEnabled(false);
        timer.reset();
        player.stop();
        playbackThread.interrupt();
    }

    private void pausePlaying() {
        isPause = true;
        player.pause();
        timer.pauseTimer();
    }

    private void resumePlaying() {
        isPause = false;
        player.resume();
        timer.resumeTimer();
    }

    private void nextPlay() {
        player.stop();
        isPlaying = false;
        playbackThread.interrupt();
        controlThread.interrupt();
        buttonPlay.setText("Stop");
        buttonPlay.setEnabled(true);
        buttonPause.setText("Pause");
        buttonPause.setEnabled(true);
        player.resume();
        isPlaying = true;
    }

    private void prevPlay() {
        needPrev = true;
        player.stop();
        isPlaying = false;
        playbackThread.interrupt();
        controlThread.interrupt();
        buttonPlay.setText("Stop");
        buttonPlay.setEnabled(true);
        buttonPause.setText("Pause");
        buttonPause.setEnabled(true);
        player.resume();
        isPlaying = true;
    }

    //Chamada de função dos botões
    private void buttonPlayAction(java.awt.event.ActionEvent evt) {
        playBack();
    }

    private void buttonStopAction(java.awt.event.ActionEvent evt){
        stopPlaying();
    }

    private void buttonPauseAction(java.awt.event.ActionEvent evt) {
        if(isPause) {
            resumePlaying();
        } else {
            pausePlaying();
        }
    }

    private void buttonResumeAction(java.awt.event.ActionEvent evt) {
        resumePlaying();
    }

    private void buttonNextAction(java.awt.event.ActionEvent evt) {
        nextPlay();
    }

    private void buttonPreviousAction(java.awt.event.ActionEvent evt) {
        prevPlay();
    }

    private void buttonAddAction(java.awt.event.ActionEvent evt) {
        if(!textAdd.getText().isEmpty()){
            String text = textAdd.getText();
            try{
                int index = Integer.parseInt(text);
                if(index < 5) {
                    Music ins = disponiveisVector.get(index);
                    Thread newMusic = new Add(playlistVector, ins.getName(), ins.getDuration());
                    newMusic.start();
                    playlist.append(ins.getName() + "\n");
                }
            } catch (Exception e) {
                System.out.print("Try a music number\n");
            };
        }
    }

    private void buttonRemoveAction(java.awt.event.ActionEvent evt) {
        if(!textRemove.getText().isEmpty()){
            String text = textRemove.getText();
            try{
                int index  = Integer.parseInt(text);
                if(index < 5) {
                    Music ins = disponiveisVector.get(index);
                    Thread deleteMusic = new Delete(ins.getName(), playlistVector, playlistText);
                    deleteMusic.start();
                    playlist.setText("");
                    for(int i = 0; i < playlistVector.size(); i++) {
                        playlist.append(playlistVector.get(i).getName() + "\n");
                    }
                }
            } catch (Exception e) {
                System.out.print("Try a music number\n");
            }
        }
    }

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MusicPlayer().setVisible(true);
            }
        });
    }
}

