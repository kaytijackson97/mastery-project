package domain;

import java.util.ArrayList;

public class Response {

    private ArrayList<String> messages = new ArrayList<>();

    public boolean isSuccess() {
        return messages.size() == 0;
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public void addErrorMessage(String message) {
        messages.add(message);
    }
}
