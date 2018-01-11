package DAO;

import Main.ISendable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * List class to transform list of HashMap object to JSON
 */
public class HeatMapsPars extends ArrayList<ISendable> implements ISendable{


    /**
     * @return string part of JSON, which have records sort by uid
     */
    private List<String> heatsToString(){
        String json = new String("[");
        List<String> jsonList = new ArrayList<>();
        int i = 0;
        while (this.size() !=0) {
            HeatMap hm = (HeatMap) this.get(0);

            jsonList.add("{\"uid\":"+"\""+hm.getUid()+"\","+"\"points\":"+"[");

            while (  1 <= this.size()&& hm.getUid().equals(((HeatMap)this.get(0)).getUid())){
                if(1 < this.size()&&hm.getUid().equals(((HeatMap)this.get(1)).getUid()))
                    jsonList.set(i, jsonList.get(i) + (this.get(0)).toJSON() + ",");
                else
                    jsonList.set(i, jsonList.get(i) + (this.get(0)).toJSON()+"]}");
                    this.remove(0);
            }
            i++;
        }
        return jsonList;
    }

    @Override
    public String toJSON() {
        String json = new String("[");
        List<String> str = heatsToString();
        for(String s : str){
            json += s+",";
        }
        json = json.substring(0,json.length()-1)+"]";
        return json;
    }
}
