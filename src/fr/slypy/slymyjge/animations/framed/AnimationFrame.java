package fr.slypy.slymyjge.animations.framed;

import org.lwjgl.util.vector.Vector2f;

import fr.slypy.slymyjge.graphics.shape.Shape;

public abstract class AnimationFrame {

    private float speed = 1;

    public abstract Shape getShape(Vector2f position, Vector2f size);

    public float getSpeed() {

        return speed;

    }

    public void setSpeed(float speed) {

        this.speed = speed;

    }

}