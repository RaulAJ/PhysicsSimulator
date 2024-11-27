package simulator.factories;

import org.json.JSONObject;

import java.util.*;

public class BuilderBasedFactory<T> implements Factory<T>{
    private Map<String, Builder<T>> _builders;
    private List<JSONObject> _buildersInfo;

    public BuilderBasedFactory(){
        //Create hashmap for _builders, a linkedList _buildersInfo
        this._builders = new HashMap<>();
        this._buildersInfo = new LinkedList<>();
    }

    public BuilderBasedFactory(List<Builder<T>> builders){
        this();
        // call addBuilder(b) for each builder in b builder
        for (Builder<T> builder: builders)
        {
            this.addBuilder(builder);
        }
        //...
    }
    public void addBuilder(Builder<T> b){
        //add an entry "b.getTag -> b" to _builders.
        //...
        _builders.put(b.getTypeTag(), b);
        //add b.getInfo() to _buildersInfo
        //...
        _buildersInfo.add(b.getInfo());
    }

    @Override
    public T createInstance(JSONObject info) throws IllegalArgumentException{
        if (info == null) {
            throw new IllegalArgumentException("Invalid value for createInstance: null");
        }
        // Search for a builder with a tag equals to info . getString("type"), call its
        // createInstance method and return the result if it is not null . The value you
        // pass to createInstance is :
        //
        // info . has("data") ? info . getJSONObject("data") : new getJSONObject()

        for(Map.Entry<String, Builder<T>> entry : _builders.entrySet())
        {

            if (entry.getKey().equals(info.getString("type")))
            {
                T result = entry.getValue().createInstance(info.has("data") ? info.getJSONObject("data") : new JSONObject());
                if (result != null)
                    return result;
            }
        }
        // If no builder is found or thr result is null ...
        throw new IllegalArgumentException("Invalid value for createInstance: " + info.toString());
    }

    @Override
    public List<JSONObject> getInfo() {
        return Collections.unmodifiableList(_buildersInfo);
    }
}
