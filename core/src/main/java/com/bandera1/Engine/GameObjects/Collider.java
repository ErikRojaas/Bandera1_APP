package com.bandera1.Engine.GameObjects;

public abstract class Collider extends Component {
    public abstract boolean collidesWith(Collider other);
}
