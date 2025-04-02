package oscar.medina.galvez.engine.GameObjects;

public abstract class Collider extends Component {
    public abstract boolean collidesWith(Collider other);
}
