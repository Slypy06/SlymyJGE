package fr.slypy.slymyjge.animations.framed;

import org.lwjgl.util.vector.Vector2f;

import fr.slypy.slymyjge.graphics.shape.Shape;

public abstract class AnimationFrame {

    private long speed = 50000000;

    public abstract Shape getShape(Vector2f position, Vector2f size);

    public float getSpeed() {

        return (long) (1000000000D / speed);

    }

    public void setSpeed(float speed) {

        this.speed = (long) (1000000000D / speed);

    }

}