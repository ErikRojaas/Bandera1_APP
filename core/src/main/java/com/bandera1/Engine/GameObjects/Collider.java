package com.bandera1.Engine.GameObjects;

import com.badlogic.gdx.math.Vector2;

public abstract class Collider extends Component {
    public abstract boolean collidesWith(Collider other);
    public abstract boolean isInside(Vector2 point);
}
