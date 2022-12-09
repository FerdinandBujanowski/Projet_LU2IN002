public class QueenAlreadyExistsException extends Exception {
    public QueenAlreadyExistsException() {
        super("il y a déjà une reine sur le terrain");
    }
}
