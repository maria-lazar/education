import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LinkedList {
    private Node last = new Node(Integer.MIN_VALUE, 0, null);
    private Node first = new Node(Integer.MAX_VALUE, 0, last);

    public void addMonomial(int exp, int coeff) {
        first.lock();
        Node pred = first;
        Node current = pred.getNext();
        current.lock();

        // pred and current are locked
        while (exp < current.getExponent()) {
            Node oldPred = pred;
            pred = current;
            current = pred.getNext();
            oldPred.unlock();
            current.lock();
        }
        // update current
        if (current != last && current.getExponent() == exp) {
            current.setCoefficient(current.getCoefficient() + coeff);
            current.unlock();
            pred.unlock();
            return;
        }
        // insert new Node between pred and current
        Node createdNode = new Node(exp, coeff, current);
        pred.setNext(createdNode);

        // unlock pred and current
        pred.unlock();
        current.unlock();
    }

    public void addMonomialSeq(int exp, int coeff) {
        Node node = first;
        while (node.getNext() != null && exp < node.getNext().getExponent()) {
            node = node.getNext();
        }
        if (node.getNext() != last && node.getNext().getExponent() == exp) {
            node.getNext().setCoefficient(node.getNext().getCoefficient() + coeff);
            return;
        }
        Node createdNode = new Node(exp, coeff, node.getNext());
        node.setNext(createdNode);
    }

    public void writeListToFile(String fileName) {
        File file = new File(fileName);
        try {
            FileWriter fw = new FileWriter(file);
            Node node = first;
            while (node != null) {
                if (node.getCoefficient() != 0) {
                    fw.write(node.getCoefficient() + " * x^" + node.getExponent() + "\n");
                }
                node = node.getNext();
            }
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
