package Main;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.*;


/**
 * Created by July on 10.12.2016.
 */
public class XMLwriterReader<T> {
    String address;
    public XMLwriterReader(String address) {
        this.address = address;
    }

    XStream xstream = new XStream(new DomDriver("UTF-8"));

    /**
     * Writes object into .xml file
     * @param object - the object
     * @param c - class of the object
     * @throws IOException
     */
    public void WriteFile(T object, Class c){
        xstream.alias(c.getClass().getName(), c);

        File f = new File(address);
        File folder = f.getParentFile();

        if (!folder.exists())
            folder.mkdirs();
        //String fPath = f.getPath();
        //String folredPath = folder.getPath();

        ObjectOutputStream out = null;
        try {
            out = xstream.createObjectOutputStream(new FileWriter(this.address));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            out.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //delete this comment

    /**
     * Reads object from .xml file
     * @param c - class of object
     * @return the object
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public T ReadFile(Class c){
        xstream.alias(c.getClass().getName(), c);
        ObjectInputStream in = null;
        try {
            in = xstream.createObjectInputStream(new FileReader(address));
        } catch (IOException e) {
            e.printStackTrace();
        }

        T newObject = null;
        try {
            newObject = (T) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return newObject;
    }
}
