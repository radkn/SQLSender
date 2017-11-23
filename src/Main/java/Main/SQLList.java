package Main;

import java.util.ArrayList;

public class SQLList extends ArrayList<ISendable> implements ISendable{


    @Override
    public String toJSON() {
        String json = new String("[");

        for(ISendable line : this){
            json += line.toJSON()+",";
        }
        json = json.substring(0,json.length()-1)+"]";
        return json;
    }
}
