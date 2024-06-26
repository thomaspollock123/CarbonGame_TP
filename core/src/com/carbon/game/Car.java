package com.carbon.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;

import java.util.Arrays;
import java.util.Random;

public class Car extends FreeRoam {
    //constants
    public static final int CAB_CARBON = 10;
    private final Player player;
    private final Map map;
    //texture
    public Texture img = new Texture(Gdx.files.internal("testShapes/car_final.png"));
    public boolean called = false;
    public boolean hidden = false;
    public Sound Car_Horn = Gdx.audio.newSound(Gdx.files.internal("SFX/Bus_Horn.wav"));

    public Car(int x, int y, Player p, Map m) {
        super(x, y);
        player = p;
        map = m;
        park(1);
    }

    public void resetPos() {
        setCell(cellX, cellY);
        move = false;
        path.clear();
    }

    public void arriveAtTarget() {
        super.arriveAtTarget();
        if (called) {
            Player.carbon += CAB_CARBON;
        }
    }

    protected void arrived() {
        if (called) {
            checkForPlayer();
        } else {
            Random random = new Random();
            int randomIndex = random.nextInt(5,10);
            park(randomIndex);
        }
    }

    private void checkForPlayer() {
        if (called && cellX == player.cellX && cellY == player.cellY) {
            pickUpPlayer();
        }
    }

    public void pickUpPlayer() {
        player.mode = 3;
        hidden = true;
        player.car = this;
        player.img = this.img;
        player.carCalled = false;
        Car_Horn.play();
    }
    public void dropOff() {
        setCell(player.cellX, player.cellY);
        hidden = false;
        player.car = null;
        park(3);
    }

    private void park(float time) {
        Timer timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run () {
                drive();
            }
        }, (float) time, 0, 0);
    }

    private void drive() {
        if (called) {
            return;
        }
        int [] dest = map.randomTile();
        System.out.println(Arrays.toString(dest));
        if (dest[0] == cellX && dest[1] == cellY) {
            park(1);
        } else {
            setPath(map.carPath(cellX, cellY, dest[0], dest[1]));
        }
    }

    public void dispose() {
        img.dispose();
        Car_Horn.dispose();
    }
}
