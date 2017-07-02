/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2016 Caprica Software Limited.
 */

package sjs.fy.opt.api.view;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CanvasVideoSurface;

public class CaptureTest {

    private final JFrame frame;
    private final JPanel contentPane;
    private final Canvas canvas;
    private final MediaPlayerFactory factory;
    private final EmbeddedMediaPlayer mediaPlayer;
    private final CanvasVideoSurface videoSurface;

    public static void main(final String[] args) {
        new NativeDiscovery().discover();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String murl = "rtsp://admin:admin123@192.168.1.64";
                new CaptureTest().start(murl);
            }
        });
    }

    public CaptureTest() {
        canvas = new Canvas();
        canvas.setBackground(Color.black);

        contentPane = new JPanel();
        contentPane.setBackground(Color.black);
        contentPane.setLayout(new BorderLayout());
        contentPane.add(canvas, BorderLayout.CENTER);

        frame = new JFrame("Capture");
        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(50, 50);
        frame.setSize(800, 600);

        factory = new MediaPlayerFactory();
        mediaPlayer = factory.newEmbeddedMediaPlayer();

        videoSurface = factory.newVideoSurface(canvas);

        mediaPlayer.setVideoSurface(videoSurface);
    }

    private void start(String mrl) {
        frame.setVisible(true);
        String fileName = "D:/tmp/123123.mpg";
        // Tweak the options depending on your encoding requirements and audio
        // capture device (ALSA is not likely to work on Windows of course)
        String[] options = {":sout=#transcode{vcodec=mp4v,vb=4096,scale=1,acodec=mpga,ab=128,channels=2,samplerate=44100}:duplicate{dst=file{dst=" + fileName + "},dst=display}", ":input-slave=alsa://hw:0,0"};

        mediaPlayer.playMedia(mrl, options);
    }
}