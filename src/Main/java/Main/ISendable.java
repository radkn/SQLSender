package Main;

public interface ISendable {
    /**
     * Makes from all obtained from DB data JSON-string
     * @return string in JSON format
     */
    String toJSON();
}
